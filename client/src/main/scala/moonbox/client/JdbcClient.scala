package moonbox.client

import java.io.IOException
import java.net.SocketAddress
import java.util.concurrent.atomic.AtomicLong

import io.netty.bootstrap.Bootstrap
import io.netty.channel._
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.handler.codec.serialization.{ClassResolvers, ObjectDecoder, ObjectEncoder}
import io.netty.util.concurrent.EventExecutorGroup
import moonbox.common.MbLogging
import moonbox.common.message.JdbcOutboundMessage

class JdbcClient(host: String, port: Int) extends MbLogging {

  private var channel: Channel = _
  private var handler: JdbcClientHandler = _
  private val messageId = new AtomicLong()
  private var ws: EventExecutorGroup = _
  var connected: Boolean = _
  val DEFAULT_TIMEOUT = 1000 * 15 //ms

  def connect(timeout: Int = DEFAULT_TIMEOUT): Unit = {
    try {
      val workerGroup = new NioEventLoopGroup()
      ws = workerGroup
      val b = new Bootstrap()
      handler = new JdbcClientHandler
      b.group(workerGroup)
        .channel(classOf[NioSocketChannel])
        .option[java.lang.Boolean](ChannelOption.SO_KEEPALIVE, true)
        .option[java.lang.Boolean](ChannelOption.TCP_NODELAY, true)
        .option[java.lang.Integer](ChannelOption.SO_RCVBUF, 10240)
        .handler(new ChannelInitializer[SocketChannel]() {
          override def initChannel(ch: SocketChannel) = {
            ch.pipeline.addLast(new ObjectEncoder, new ObjectDecoder(Int.MaxValue, ClassResolvers.cacheDisabled(null)), handler)
          }
        })
      val cf = b.connect(host, port).syncUninterruptibly()
      if (!cf.await(timeout))
        throw new IOException(s"Connecting to $host timed out (${timeout} ms)")
      else if (cf.cause != null)
        throw new IOException(s"Failed to connect to $host", cf.cause)
      this.channel = cf.channel
      logInfo(s"Connected to ${channel.remoteAddress()}")
      connected = true
    } catch {
      case e: Exception =>
        logError(e.getMessage)
        this.close()
    }
  }

  def getRemoteAddress: SocketAddress = {
    if (channel != null) channel.remoteAddress()
    else throw new ChannelException("channel unestablished")
  }

  def getMessageId(): Long = messageId.getAndIncrement()

  def sendOneWayMessage(msg: Any) = handler.send(msg)

  def sendAndReceive(msg: Any, timeout: Long = DEFAULT_TIMEOUT): JdbcOutboundMessage = handler.sendAndReceive(msg, timeout)

  def sendWithCallback(msg: Any, callback: => JdbcOutboundMessage => Any) = handler.send(msg, callback)

  def isConnected(): Boolean = connected

  def close() = {
    if (channel != null) {
      channel.close()
    }
    if (ws != null) {
      ws.shutdownGracefully()
    }
  }
}

