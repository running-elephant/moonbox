package moonbox.network.server;

import com.google.common.base.Throwables;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import moonbox.network.client.ResponseCallback;
import moonbox.network.client.TransportClient;
import moonbox.network.protocol.RequestMessage;
import moonbox.network.protocol.RpcFailure;
import moonbox.network.protocol.RpcRequest;
import moonbox.network.protocol.RpcResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;


public class TransportRequestHandler extends MessageHandler<RequestMessage>  {
  private static final Logger logger = LoggerFactory.getLogger(TransportRequestHandler.class);

  private final Channel channel;

  private final RpcHandler rpcHandler;

  private final TransportClient remoteClient;

  public TransportRequestHandler(Channel channel, TransportClient client, RpcHandler rpcHandler) {
    this.channel = channel;
    this.remoteClient = client;
    this.rpcHandler = rpcHandler;
  }

  @Override
  public void handle(RequestMessage request) throws Exception {
    if (request instanceof RpcRequest) {
      RpcRequest req = (RpcRequest) request;
      try {
        rpcHandler.receive(req.body(), new RpcCallContext(new ResponseCallback() {
          @Override
          public void onSuccess(ByteBuf response) {
            respond(new RpcResponse(req.requestId, response));
          }

          @Override
          public void onFailure(Throwable e) {
            respond(new RpcFailure(req.requestId, e.getMessage() != null ? e.getMessage() : Throwables.getStackTraceAsString(e)));
          }
        }));
      } catch (Exception e) {
        respond(new RpcFailure(req.requestId, e.getMessage() != null ? e.getMessage() : Throwables.getStackTraceAsString(e)));
      } finally {
        req.body().release();
      }
    } else {
      throw new IllegalArgumentException("Unknown request type: " + request);
    }
  }

  private void respond(Object message) {
    SocketAddress socketAddress = channel.remoteAddress();
    channel.writeAndFlush(message).addListener(future -> {
      if (future.isSuccess()) {

      } else {
        channel.close();
      }
    });
  }

  @Override
  public void channelActive() {

  }

  @Override
  public void exceptionCaught(Throwable cause) {

  }

  @Override
  public void channelInactive() {

  }

}
