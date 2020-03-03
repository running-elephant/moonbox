package moonbox.network;

import io.netty.channel.Channel;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;
import moonbox.network.client.TransportClient;
import moonbox.network.client.TransportClientFactory;
import moonbox.network.client.TransportResponseHandler;
import moonbox.network.protocol.MessageDecoder;
import moonbox.network.protocol.MessageEncoder;
import moonbox.network.server.RpcHandler;
import moonbox.network.server.TransportChannelHandler;
import moonbox.network.server.TransportRequestHandler;
import moonbox.network.server.TransportServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransportContext {
  private static final Logger logger = LoggerFactory.getLogger(TransportContext.class);

  private final RpcHandler rpcHandler;
  private final boolean closeIdleConnections;

  public TransportContext(
      RpcHandler rpcHandler,
      boolean closeIdleConnections
  ) {
    this.rpcHandler = rpcHandler;
    this.closeIdleConnections = closeIdleConnections;
  }

  private TransportChannelHandler createChannelHandler(Channel channel, RpcHandler rpcHandler) {
    TransportResponseHandler responseHandler = new TransportResponseHandler(channel);
    TransportClient client = new TransportClient(channel, responseHandler);
    TransportRequestHandler requestHandler = new TransportRequestHandler(channel, client,
        rpcHandler);
    return new TransportChannelHandler(client, responseHandler, requestHandler,
        30 * 1000, closeIdleConnections);
  }

  public TransportChannelHandler initializePipeline(SocketChannel channel) {
    return initializePipeline(channel, rpcHandler);
  }

  public TransportChannelHandler initializePipeline(
      SocketChannel channel,
      RpcHandler channelRpcHandler) {

    try {
      TransportChannelHandler channelHandler = createChannelHandler(channel, channelRpcHandler);
      channel.pipeline()
          .addLast(new ProtobufVarint32FrameDecoder())
          .addLast("decoder", MessageDecoder.INSTANCE)
          .addLast(new ProtobufVarint32LengthFieldPrepender())
          .addLast("encoder", MessageEncoder.INSTANCE)
          .addLast("idleStateHandler", new IdleStateHandler(0, 0, 3600))
          .addLast("handler", channelHandler);
      return channelHandler;
    } catch (RuntimeException e) {
      logger.error("Error while initializing netty pipeline", e);
      throw e;
    }
  }

  public TransportServer createServer(String host, int port) {
    return new TransportServer(this, host, port, rpcHandler);
  }

  public TransportClientFactory createClientFactory() {
    return new TransportClientFactory(this);
  }

}
