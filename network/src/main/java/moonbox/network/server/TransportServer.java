package moonbox.network.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import moonbox.network.TransportContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

public class TransportServer implements Closeable {
  private static final Logger logger = LoggerFactory.getLogger(TransportServer.class);

  private TransportContext context;
  private ServerBootstrap bootstrap;
  private ChannelFuture channelFuture;
  private RpcHandler appRpcHandler;
  private String hostToBind;
  private int portToBind = -1;


  public TransportServer(
      TransportContext context,
      String hostToBind,
      int portToBind,
      RpcHandler appRpcHandler) {
    this.context = context;
    this.hostToBind = hostToBind;
    this.portToBind = portToBind;
    this.appRpcHandler = appRpcHandler;
  }

  public int start() throws IOException {
    try {
      this.portToBind = init(hostToBind, portToBind);
    } catch (RuntimeException e) {
      try {
        close();
      } catch (IOException ex) {
        logger.error("IOException should not have been thrown.", ex);
        throw ex;
      }
    }
    return this.portToBind;
  }

  private int init(String hostToBind, int portToBind) {
    EventLoopGroup boosGroup = new NioEventLoopGroup(0, new DefaultThreadFactory(this.getClass(), true));
    EventLoopGroup workerGroup = boosGroup;
    bootstrap = new ServerBootstrap()
        .group(boosGroup, workerGroup)
        .channel(NioServerSocketChannel.class)
        .option(ChannelOption.SO_BACKLOG, 1024)
        .childOption(ChannelOption.SO_KEEPALIVE, true)
        .childOption(ChannelOption.SO_RCVBUF, 10240)
        .childOption(ChannelOption.SO_SNDBUF, 10240)
        .childHandler(new ChannelInitializer<SocketChannel>() {
          @Override
          protected void initChannel(SocketChannel ch) throws Exception {
            context.initializePipeline(ch, appRpcHandler);
          }
        });
    InetSocketAddress address = hostToBind == null ?
        new InetSocketAddress(portToBind): new InetSocketAddress(hostToBind, portToBind);
    channelFuture = bootstrap.bind(address);
    channelFuture.syncUninterruptibly();

    return  ((InetSocketAddress) channelFuture.channel().localAddress()).getPort();
  }

  public int getPort() {
    if (portToBind == -1) {
      throw new IllegalStateException("Server not initialized");
    }
    return portToBind;
  }

  @Override
  public void close() throws IOException {
    if (channelFuture != null) {
      channelFuture.channel().close().awaitUninterruptibly(10, TimeUnit.SECONDS);
      channelFuture = null;
    }
    if (bootstrap != null && bootstrap.group() != null) {
      bootstrap.group().shutdownGracefully();
    }
    if (bootstrap != null && bootstrap.childGroup() != null) {
      bootstrap.childGroup().shutdownGracefully();
    }
    bootstrap = null;
  }

}
