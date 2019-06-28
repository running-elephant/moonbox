---
layout: global
title: Deployment
---

### Preparing the Development Env

- Install Apache Spark 2.2.0 (Moonbox-0.3.0-beta only supports Apache Spark 2.2.0 at present; other versions will be compatible later).

- Install and start MySQL; enable remote access.
- Make sure you have performed SSH login without password on each installation node.

### Download
[moonbox-0.3.0-beta download](https://github.com/edp963/moonbox/releases/tag/0.3.0-beta)

### Decompression

```
tar -zxvf moonbox-assembly_2.11-0.3.0-beta-dist.tar.gz
```

### Modifying the Configuration Files
Configuration files are in the `conf` directory.
- step 1: Modify `slaves`,

    ```
    mv slaves.example slaves
    vim slaves
    ```
    and the following content appears. Change the content to the address that needs to deploy worker node, one address for each row. 
    ```
    localhost
    ```
    请根据实际情况修改为需要部署worker节点的地址, 每行一个地址

- step 2: Modify `moonbox-env.sh`
    ```
    mv moonbox-env.sh.example moonbox-env.sh
    chmod u+x moonbox-env.sh
    vim moonbox-env.sh
    ```
    and the following content appears.
    ```
    export JAVA_HOME=path/to/installed/dir
    export SPARK_HOME=path/to/installed/dir
    export YARN_CONF_DIR=path/to/yarn/conf/dir
    export MOONBOX_SSH_OPTS="-p 22"
    export MOONBOX_HOME=path/to/installed/dir
    # export MOONBOX_LOCAL_HOSTNAME=localhost
    export MOONBOX_MASTER_HOST=localhost
    export MOONBOX_MASTER_PORT=2551
    ```
    Modify it according to your needs.  

- step 3: Modify `moonbox-defaults.conf`  
    ```
    mv moonbox-defaults.conf.example moonbox-defaults.conf
    vim moonbox-defaults.conf
    ```
    and the following content appears, wherein:  
    - catalog

      refers to configuring the storage location of metadata; it must be modified.
    - rest

      refers to configuring REST services; you can modify it according to your needs.
    - tcp

      refers to configuring TCP (JDBC) services; you can modify it according to your needs.
    - local

      refers to configuring Spark obligation in local mode, with array as its value; the number of elements is exactly the number of Spark obligations in local mode enabled by each worker node. You can delete it if don't need it.
    - cluster

      refers to configuring Spark obligation in yarn mode, with array as its value; the number of elements is exactly the number of Spark obligations in yarn mode enabled by each worker node. You can delete it if don't need it.
    ```
    moonbox {
        deploy {
            catalog {
                implementation = "mysql"
                url = "jdbc:mysql://host:3306/moonbox?createDatabaseIfNotExist=true"
                user = "root"
                password = "123456"
                driver = "com.mysql.jdbc.Driver"
            }
            rest {
                enable = true
                port = 9099
                request.timeout = "600s"
                idle.timeout= "600s"
            }
            tcp {
                enable = true
                port = 10010
            }
        }
        mixcal {
            pushdown.enable = true
            column.permission.enable = true
            spark.sql.cbo.enabled = true
            spark.sql.constraintPropagation.enabled = false

            local = [{}]
            cluster = [{
              spark.hadoop.yarn.resourcemanager.hostname = "master"
              spark.hadoop.yarn.resourcemanager.address = "master:8032"
              spark.yarn.stagingDir = "hdfs://master:8020/tmp"
              spark.yarn.access.namenodes = "hdfs://master:8020"
              spark.loglevel = "ERROR"
              spark.cores.max = 2
              spark.yarn.am.memory = "512m"
              spark.yarn.am.cores = 1
              spark.executor.instances = 2
              spark.executor.cores = 1
              spark.executor.memory = "2g"
            }]
        }
    }
    ```
    - optional: 
    if HA or kerberos is configured for HDFS or Yarn, modify relevant part in cluster as follows. (you can refer to the config files of HDFS and Yarn for concrete values)
    
    ```
    #### HDFS HA ####
    spark.hadoop.fs.defaultFS="hdfs://service_name"
    spark.hadoop.dfs.nameservices="service_name"
    spark.hadoop.dfs.ha.namenodes.service_name="xxx1,xxx2"
    spark.hadoop.dfs.namenode.rpc-address.abdt.xxx1="xxx1_host:8020"
    spark.hadoop.dfs.namenode.rpc-address.abdt.xxx2="xxx2_host:8020"
    spark.hadoop.dfs.client.failover.proxy.provider.abdt="org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider"
    spark.yarn.stagingDir = "hdfs://service_name/tmp"
    ```
    ```
    #### HDFS kerberos ####
    dfs.namenode.kerberos.principal = ""
    dfs.namenode.kerberos.keytab = ""
    ```
    ```
    #### YARN HA ####
    spark.hadoop.yarn.resourcemanager.ha.enabled=true
    spark.hadoop.yarn.resourcemanager.ha.rm-ids="yyy1,yyy2"
    spark.hadoop.yarn.resourcemanager.hostname.rm1="yyy1_host"
    spark.hadoop.yarn.resourcemanager.hostname.rm2="yyy2_host"
    ```
    ```
    #### YARN kerberos ####
    spark.yarn.principal = ""
    spark.yarn.keytab = ""
    ```

### Distributing Installation Package  
   Put MySQL JDBC Driver in the libs and runtime directories. Copy the whole Moonbox installation directory to every installation node and make sure the directory location in the nodes is the same as that in the masters.

### Starting Cluster
   Execute  
   ```
   sbin/start-all.sh
   ```
   
   on the master node.
### Stopping Cluster
   Execute
   ```
   sbin/stop-all.sh
   ```
   
   on the master node.

### Checking

   After Starting cluster, you can check whether the cluster is enabled successfully.

   Execute the following command on the master node, and you will get the process of MoonboxMaster:

   ```
   jps | grep Moonbox
   ````
   Execute the following command on the worker node, and you will get the process of MoonboxWorker:
   ```
   jps | grep Moonbox
   ```
   Execute the following command on the worker node, and you will get the process of SparkSubmit:
   ```
   jps -m | grep Spark
   ```
   You can also get cluster information through moonbox-cluster:
   ```
   bin/moonbox-cluster workers
   bin/moonbox-cluster apps
   ```
   If the cluster is enabled successfully, you can refer to **EXAMPLES** and try to use Moonbox; If the cluster fails to enable, you can troubleshoot through logs in the logs directory on the master/worker nodes.
   
