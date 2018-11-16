/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.hadoop.hdfs.server.namenode.ha;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.CommonConfigurationKeysPublic;
import org.apache.hadoop.hdfs.DFSConfigKeys;
import org.apache.hadoop.hdfs.DFSUtil;
import org.apache.hadoop.hdfs.HAUtil;
import org.apache.hadoop.hdfs.NameNodeProxies;
import org.apache.hadoop.hdfs.server.protocol.NamenodeProtocols;
import org.apache.hadoop.ipc.RPC;
import org.apache.hadoop.net.NetUtils;
import org.apache.hadoop.security.UserGroupInformation;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.*;

/**
 * A FailoverProxyProvider implementation which allows one to configure two URIs
 * to connect to during fail-over. The first configured address is tried first,
 * and on a fail-over event the other address is tried.
 */
public class ConfiguredRemoteFailoverProxyProvider<T> extends
        AbstractNNFailoverProxyProvider<T> {

    private static final Log LOG =
            LogFactory.getLog(ConfiguredRemoteFailoverProxyProvider.class);

    private final Configuration conf;
    private final List<AddressRpcProxyPair<T>> proxies =
            new ArrayList<AddressRpcProxyPair<T>>();
    private final UserGroupInformation ugi;
    private final Class<T> xface;

    public static final String DFS_REMOTE_NAMESERVICES = "dfs.remote.nameservices";

    private int currentProxyIndex = 0;

    public ConfiguredRemoteFailoverProxyProvider(Configuration conf, URI uri,
                                                 Class<T> xface) {
        Preconditions.checkArgument(
                xface.isAssignableFrom(NamenodeProtocols.class),
                "Interface class %s is not a valid NameNode protocol!");
        this.xface = xface;

        this.conf = new Configuration(conf);
        int maxRetries = this.conf.getInt(
                DFSConfigKeys.DFS_CLIENT_FAILOVER_CONNECTION_RETRIES_KEY,
                DFSConfigKeys.DFS_CLIENT_FAILOVER_CONNECTION_RETRIES_DEFAULT);
        this.conf.setInt(
                CommonConfigurationKeysPublic.IPC_CLIENT_CONNECT_MAX_RETRIES_KEY,
                maxRetries);

        int maxRetriesOnSocketTimeouts = this.conf.getInt(
                DFSConfigKeys.DFS_CLIENT_FAILOVER_CONNECTION_RETRIES_ON_SOCKET_TIMEOUTS_KEY,
                DFSConfigKeys.DFS_CLIENT_FAILOVER_CONNECTION_RETRIES_ON_SOCKET_TIMEOUTS_DEFAULT);
        this.conf.setInt(
                CommonConfigurationKeysPublic.IPC_CLIENT_CONNECT_MAX_RETRIES_ON_SOCKET_TIMEOUTS_KEY,
                maxRetriesOnSocketTimeouts);

        try {
            ugi = UserGroupInformation.getCurrentUser();

            Map<String, Map<String, InetSocketAddress>> map = getHaNnRpcAddresses(conf);

            Map<String, InetSocketAddress> addressesInNN = map.get(uri.getHost());

            if (addressesInNN == null || addressesInNN.size() == 0) {
                throw new RuntimeException("Could not find any configured addresses " +
                        "for URI " + uri);
            }

            Collection<InetSocketAddress> addressesOfNns = addressesInNN.values();
            for (InetSocketAddress address : addressesOfNns) {
                proxies.add(new AddressRpcProxyPair<T>(address));
            }

            // The client may have a delegation token set for the logical
            // URI of the cluster. Clone this token to apply to each of the
            // underlying IPC addresses so that the IPC code can find it.
            HAUtil.cloneDelegationTokenForLogicalUri(ugi, uri, addressesOfNns);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Class<T> getInterface() {
        return xface;
    }

    /**
     * Lazily initialize the RPC proxy object.
     */
    @Override
    public synchronized ProxyInfo<T> getProxy() {
        AddressRpcProxyPair<T> current = proxies.get(currentProxyIndex);
        if (current.namenode == null) {
            try {
                current.namenode = NameNodeProxies.createNonHAProxy(conf,
                        current.address, xface, ugi, false, fallbackToSimpleAuth).getProxy();
            } catch (IOException e) {
                LOG.error("Failed to create RPC proxy to NameNode", e);
                throw new RuntimeException(e);
            }
        }
        return new ProxyInfo<T>(current.namenode, current.address.toString());
    }

    @Override
    public synchronized void performFailover(T currentProxy) {
        currentProxyIndex = (currentProxyIndex + 1) % proxies.size();
    }

    /**
     * A little pair object to store the address and connected RPC proxy object to
     * an NN. Note that {@link AddressRpcProxyPair#namenode} may be null.
     */
    private static class AddressRpcProxyPair<T> {
        public final InetSocketAddress address;
        public T namenode;

        public AddressRpcProxyPair(InetSocketAddress address) {
            this.address = address;
        }
    }

    /**
     * Close all the proxy objects which have been opened over the lifetime of
     * this proxy provider.
     */
    @Override
    public synchronized void close() throws IOException {
        for (AddressRpcProxyPair<T> proxy : proxies) {
            if (proxy.namenode != null) {
                if (proxy.namenode instanceof Closeable) {
                    ((Closeable) proxy.namenode).close();
                } else {
                    RPC.stopProxy(proxy.namenode);
                }
            }
        }
    }

    /**
     * Logical URI is required for this failover proxy provider.
     */
    @Override
    public boolean useLogicalURI() {
        return true;
    }

    private static Map<String, Map<String, InetSocketAddress>> getHaNnRpcAddresses(
            Configuration conf) {
        Collection<String> remoteNameserviceIds = conf.getTrimmedStringCollection(DFS_REMOTE_NAMESERVICES);
        Map<String, Map<String, InetSocketAddress>> ret = Maps.newLinkedHashMap();
        for (String nsId : emptyAsSingletonNull(remoteNameserviceIds)) {
            Map<String, InetSocketAddress> isas =
                    getAddressesForNameserviceId(conf, nsId, null, DFSConfigKeys.DFS_NAMENODE_RPC_ADDRESS_KEY);
            if (!isas.isEmpty()) {
                ret.put(nsId, isas);
            }
        }
        return ret;
    }

    private static Collection<String> emptyAsSingletonNull(Collection<String> coll) {
        if (coll == null || coll.isEmpty()) {
            return Collections.singletonList(null);
        } else {
            return coll;
        }
    }

    private static Map<String, InetSocketAddress> getAddressesForNameserviceId(
            Configuration conf, String nsId, String defaultValue,
            String... keys) {
        Collection<String> nnIds = DFSUtil.getNameNodeIds(conf, nsId);
        Map<String, InetSocketAddress> ret = Maps.newHashMap();
        for (String nnId : emptyAsSingletonNull(nnIds)) {
            String suffix = concatSuffixes(nsId, nnId);
            String address = getConfValue(defaultValue, suffix, conf, keys);
            if (address != null) {
                InetSocketAddress isa = NetUtils.createSocketAddr(address);
                if (isa.isUnresolved()) {
                    LOG.warn("Namenode for " + nsId +
                            " remains unresolved for ID " + nnId +
                            ".  Check your hdfs-site.xml file to " +
                            "ensure namenodes are configured properly.");
                }
                ret.put(nnId, isa);
            }
        }
        return ret;
    }

    /**
     * Concatenate list of suffix strings '.' separated
     */
    private static String concatSuffixes(String... suffixes) {
        if (suffixes == null) {
            return null;
        }
        return Joiner.on(".").skipNulls().join(suffixes);
    }

    /**
     * Given a list of keys in the order of preference, returns a value
     * for the key in the given order from the configuration.
     *
     * @param defaultValue default value to return, when key was not found
     * @param keySuffix    suffix to add to the key, if it is not null
     * @param conf         Configuration
     * @param keys         list of keys in the order of preference
     * @return value of the key or default if a key was not found in configuration
     */
    private static String getConfValue(String defaultValue, String keySuffix,
                                       Configuration conf, String... keys) {
        String value = null;
        for (String key : keys) {
            key = addSuffix(key, keySuffix);
            value = conf.get(key);
            if (value != null) {
                break;
            }
        }
        if (value == null) {
            value = defaultValue;
        }
        return value;
    }

    /**
     * Add non empty and non null suffix to a key
     */
    private static String addSuffix(String key, String suffix) {
        if (suffix == null || suffix.isEmpty()) {
            return key;
        }
        assert !suffix.startsWith(".") :
                "suffix '" + suffix + "' should not already have '.' prepended.";
        return key + "." + suffix;
    }

}
