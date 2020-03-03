package moonbox.network.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import moonbox.network.TransportContext;
import moonbox.network.server.TransportChannelHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicReference;

public class TransportClientFactory {
  private static final Logger logger = LoggerFactory.getLogger(TransportClientFactory.class);

  private TransportContext context;
  private EventLoopGroup workerGroup;

  public TransportClientFactory(TransportContext context) {
    this.context = context;
    this.workerGroup = new NioEventLoopGroup(0, new DefaultThreadFactory(this.getClass(), true));
  }

  public TransportClient createClient(String host, int port, int connectTimeout) throws IOException, InterruptedException {
    final InetSocketAddress address = new InetSocketAddress(host, port);
    logger.debug("Creating new connection to {}", address);

    Bootstrap bootstrap = new Bootstrap();
    bootstrap.group(workerGroup)
        .channel(NioSocketChannel.class)
        .option(ChannelOption.TCP_NODELAY, true)
        .option(ChannelOption.SO_KEEPALIVE, true)
        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimeout)
        .option(ChannelOption.SO_RCVBUF, 10240)
        .option(ChannelOption.SO_SNDBUF, 10240);

    final AtomicReference<TransportClient> clientRef = new AtomicReference<>();
    final AtomicReference<Channel> channelRef = new AtomicReference<>();

    bootstrap.handler(new ChannelInitializer<SocketChannel>() {
      @Override
      public void initChannel(SocketChannel ch) {
        TransportChannelHandler clientHandler = context.initializePipeline(ch);
        clientRef.set(clientHandler.getClient());
        channelRef.set(ch);
      }
    });

    ChannelFuture cf = bootstrap.connect(address);
    if (!cf.await(connectTimeout)) {
      throw new IOException(
          String.format("Connecting to %s timed out (%s ms)", address, connectTimeout));
    } else if (cf.cause() != null) {
      throw new IOException(String.format("Failed to connect to %s", address), cf.cause());
    }

    TransportClient client = clientRef.get();
    Channel channel = channelRef.get();
    assert client != null : "Channel future completed successfully with null client";

    return client;
  }

}
