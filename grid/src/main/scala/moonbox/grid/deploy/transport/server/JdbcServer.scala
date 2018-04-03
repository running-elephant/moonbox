package moonbox.grid.deploy.transport.server

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.channel.{Channel, ChannelInitializer, ChannelOption}
import io.netty.handler.codec.serialization.{ClassResolvers, ObjectDecoder, ObjectEncoder}
import io.netty.util.concurrent.{Future, GenericFutureListener}
import moonbox.common.{MbConf, MbLogging}
import moonbox.grid.deploy.MbService

class JdbcServer(host: String, port: Int, conf: MbConf, mbService: MbService) extends MbLogging {
  var channel: Channel = _

  def start0(): Unit = {
    val bossGroup = new NioEventLoopGroup
    val workerGroup = new NioEventLoopGroup
    try {
      val b = new ServerBootstrap
      b.group(bossGroup, workerGroup)
        .channel(classOf[NioServerSocketChannel])
        .childHandler(
          new ChannelInitializer[SocketChannel]() {
            override def initChannel(ch: SocketChannel) = {
              ch.pipeline.addLast("decode", new ObjectDecoder(ClassResolvers.cacheDisabled(null)))
              ch.pipeline.addLast("encode", new ObjectEncoder)
              ch.pipeline.addLast("handler", new JdbcServerHandler(mbService))
            }
          })
        .option[java.lang.Integer](ChannelOption.SO_BACKLOG, 128)
        .childOption[java.lang.Boolean](ChannelOption.SO_KEEPALIVE, true)
        .childOption[java.lang.Integer](ChannelOption.SO_SNDBUF, 1024)
        .childOption[java.lang.Integer](ChannelOption.SO_RCVBUF, 1024)

      // Bind and start to accept incoming connections.
      val f = b.bind(host, port).sync
      logInfo(s"Jdbc server listening on $host:$port ...")
      // Wait until the server socket is closed.
      // In this example, this does not happen, but you can do that to gracefully
      // shut down your server.
      channel = f.channel()
      channel.closeFuture.addListener(new GenericFutureListener[Future[_ >: Void]] {
        override def operationComplete(future: Future[_ >: Void]) = {
          if (future.isSuccess)
            logInfo("Jdbc server tear down ...")
        }
      }).sync()
    } catch {
      case e: Exception => e.printStackTrace()
    } finally {
      workerGroup.shutdownGracefully()
      bossGroup.shutdownGracefully()
    }
  }

  def start(): Unit = {
    new Thread {
      override def run() = start0()
    }.start()
  }
}

