package moonbox.jdbc;

import com.google.common.base.Throwables;
import com.google.common.util.concurrent.SettableFuture;
import com.google.protobuf.Message;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import moonbox.jdbc.util.Utils;
import moonbox.network.TransportContext;
import moonbox.network.client.ResponseCallback;
import moonbox.network.client.TransportClient;
import moonbox.network.client.TransportClientFactory;
import moonbox.network.server.NoOpRpcHandler;
import moonbox.network.util.JavaUtils;
import moonbox.protocol.protobuf.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class MoonboxClient {
  private TransportClient client;

  private final String masterHost;
  private final int masterPort;
  private final int connectTimeout;
  private final String user;
  private final String password;
  private String host;
  private int port;
  private final String appType;
  private final String appName;
  private final String sessionId;
  private SessionPB session;
  private final Map<String, String> config;
  private SettableFuture<ByteBuf> result;


  public MoonboxClient(String masterHost,
                       int masterPort,
                       int connectTimeout,
                       String user,
                       String password,
                       String appType,
                       String appName,
                       Map<String, String> config) throws Exception {
    this.masterHost = masterHost;
    this.masterPort = masterPort;
    this.connectTimeout = connectTimeout;
    this.user = user;
    this.password = password;
    this.appType = appType;
    this.appName = appName;
    this.config = config;
    this.client = createClient();
    this.sessionId = opensession().getSessionId();
  }

  public String getMasterHost() {
    return this.masterHost;
  }

  public int getMasterPort() {
    return this.masterPort;
  }

  public String getInternalHost() {
    return this.host;
  }

  public int getInternalPort() {
    return this.port;
  }

  private TransportClient createClient() throws Exception {
    TransportClientFactory clientFactory = new TransportContext(new NoOpRpcHandler(), true).createClientFactory();
    TransportClient clientToMaster = clientFactory.createClient(masterHost, masterPort, connectTimeout);
    AccessRequestPB.Builder builder = AccessRequestPB.newBuilder();
    builder.setUsername(user)
        .setPassword(password)
        .setAppType(appType);
    if (appName != null) {
      builder.setAppName(appName);
    }
    AccessRequestPB accessRequestPB = builder.build();
    ByteBuf byteBuf = clientToMaster.sendSync(Utils.messageToByteBuf(accessRequestPB), connectTimeout);
    AccessResponsePB accessResponsePB =
        AccessResponsePB
            .getDefaultInstance()
            .getParserForType()
            .parseFrom(Utils.byteBufToByteArray(byteBuf));
    clientToMaster.close();
    HostPortPB hostPort = accessResponsePB.getHostPort();
    this.session = accessResponsePB.getSession();
    this.host = hostPort.getHost();
    this.port = hostPort.getPort();
    return clientFactory.createClient(host, port, connectTimeout);
  }

  private OpenSessionResponsePB opensession() throws Exception {
    OpenSessionRequestPB openSessionRequestPB = OpenSessionRequestPB.newBuilder().setSession(session).putAllConfig(config).build();
    AppRequestMessage appRequestPB = AppRequestMessage.newBuilder().setOpenSession(openSessionRequestPB).build();
    ByteBuf byteBuf = client.sendSync(Utils.messageToByteBuf(appRequestPB), connectTimeout);
    return OpenSessionResponsePB
        .getDefaultInstance()
        .getParserForType()
        .parseFrom(Utils.byteBufToByteArray(byteBuf));
  }

  private CloseSessionResponsePB closesession() throws Exception {
    CloseSessionRequestPB closeSessionRequestPB = CloseSessionRequestPB.newBuilder().setSessionId(sessionId).build();
    AppRequestMessage appRequestPB = AppRequestMessage.newBuilder().setCloseSession(closeSessionRequestPB).build();
    ByteBuf byteBuf = client.sendSync(Utils.messageToByteBuf(appRequestPB), connectTimeout);
    return CloseSessionResponsePB
        .getDefaultInstance()
        .getParserForType()
        .parseFrom(Utils.byteBufToByteArray(byteBuf));
  }

  private ExecutionResultPB execute(Message request, int queryTimeout) {
    result = SettableFuture.create();
    client.send(Utils.messageToByteBuf(request), new ResponseCallback() {
      @Override
      public void onSuccess(ByteBuf response) {
        result.set(response);
      }

      @Override
      public void onFailure(Throwable e) {
        result.setException(e);
      }
    });
    try {
      ByteBuf byteBuf = result.get(queryTimeout, TimeUnit.SECONDS);
      return ExecutionResultPB.getDefaultInstance()
          .getParserForType()
          .parseFrom(Utils.byteBufToByteArray(byteBuf));
    } catch (ExecutionException e) {
      throw Throwables.propagate(e.getCause());
    } catch (Exception e) {
      throw Throwables.propagate(e);
    }
  }

  public ExecutionResultPB execute(List<String> sqls, int maxRows, int fetchSize, int queryTimeout) {
    ExecutionRequestPB requestPB =
        ExecutionRequestPB.newBuilder()
            .setMaxRows(maxRows)
            .setFetchSize(fetchSize)
            .setSessionId(sessionId)
            .addAllSqls(sqls)
            .build();
    return execute(AppRequestMessage.newBuilder().setExecute(requestPB).build(), queryTimeout);
  }

  public ExecutionResultPB next(int queryTimeout) {
    ExecutionResultRequestPB requestPB = ExecutionResultRequestPB.newBuilder().setSessionId(sessionId).build();
    return execute(AppRequestMessage.newBuilder().setExecuteResult(requestPB).build(), queryTimeout);
  }

  public void cancel() {
    ExecutionCancelRequestPB requestPB = ExecutionCancelRequestPB.newBuilder().setSessionId(sessionId).build();
    client.sendSync(Utils.messageToByteBuf(AppRequestMessage.newBuilder().setExecteCancel(requestPB).build()), connectTimeout);
    if (!result.isDone()) {
      result.cancel(true);
    }
  }

  public void close() {
    if (client != null && client.isActive()) {
      try {
        closesession();
        client.close();
      } catch (ExecutionException e) {
        throw Throwables.propagate(e.getCause());
      } catch (Exception e) {
        throw Throwables.propagate(e);
      }
    }
  }

  public boolean isClose() {
    return !client.isActive();
  }

}
