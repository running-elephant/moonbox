package moonbox.application.interactive.netty

import java.lang.{Boolean => JBoolean, Integer => JInt}
import java.net.InetSocketAddress
import java.util.concurrent.TimeUnit

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel._
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.codec.protobuf.{ProtobufDecoder, ProtobufEncoder, ProtobufVarint32FrameDecoder, ProtobufVarint32LengthFieldPrepender}
import io.netty.handler.codec.serialization.{ClassResolvers, ObjectDecoder, ObjectEncoder}
import io.netty.util.concurrent.DefaultThreadFactory
import moonbox.application.interactive.Runner
import moonbox.common.{MbConf, MbLogging}
import moonbox.message.protobuf.ProtoMessage
import moonbox.protocol.NettyMessageType

import scala.collection.mutable

class DataFetchServer(host: String, port: Int, conf: MbConf, sessionIdToJobRunner: mutable.Map[String, Runner]) extends MbLogging {

  private val protocol = conf.getOption("moonbox.message.protocol").getOrElse("protobuf")
  private var channelFuture: ChannelFuture = _
  private var bootstrap: ServerBootstrap = _

  def this(host: String, conf: MbConf, sessionIdToJobRunner: mutable.Map[String, Runner]) = this(host, 0, conf, sessionIdToJobRunner)

  def start(): Int = {
    val channelHandler = getWorkerHandler(protocol, sessionIdToJobRunner)
    val bossGroup = new NioEventLoopGroup(0, new DefaultThreadFactory(this.getClass, true))
    val workerGroup = bossGroup
    bootstrap = new ServerBootstrap()
      .group(bossGroup, workerGroup)
      .channel(classOf[NioServerSocketChannel])
      .childOption[JBoolean](ChannelOption.SO_KEEPALIVE, true)
      .option[JInt](ChannelOption.SO_BACKLOG, 1024)
      .childOption[JInt](ChannelOption.SO_RCVBUF, 10240)
      .childOption[JInt](ChannelOption.SO_SNDBUF, 10240)
      .childHandler(channelHandler)

    // Bind and start to accept incoming connections.
    logInfo("Starting Data fetch server ...")
    channelFuture = bootstrap.bind(host, port)
    channelFuture.syncUninterruptibly()
    val bindPort = localAddress.getPort
    logInfo(s"Data fetch server is listening on $host:$bindPort.")
    channelFuture.channel.closeFuture.addListener(
      new ChannelFutureListener {
        override def operationComplete(future: ChannelFuture) = stop()
      })
    bindPort
  }

  def stop(): Unit = {
    if (channelFuture != null) {
      channelFuture.channel().close().awaitUninterruptibly(10, TimeUnit.SECONDS)
      channelFuture = null
    }
    if (bootstrap != null && bootstrap.group() != null) {
      bootstrap.group().shutdownGracefully()
    }
    if (bootstrap != null && bootstrap.childGroup() != null) {
      bootstrap.childGroup().shutdownGracefully()
    }
    bootstrap = null
  }

  private def localAddress: InetSocketAddress = channelFuture.channel().localAddress().asInstanceOf[InetSocketAddress]

  private def getWorkerHandler(protocol: String, sessionIdToJobRunner: mutable.Map[String, Runner]): ChannelHandler = {
    NettyMessageType.getMessageType(protocol) match {
      case NettyMessageType.JAVA_MESSAGE =>
        new ChannelInitializer[SocketChannel] {
          override def initChannel(channel: SocketChannel): Unit = {
            channel.pipeline
              .addLast("decoder", new ObjectDecoder(Int.MaxValue, ClassResolvers.cacheDisabled(null)))
              .addLast("encoder", new ObjectEncoder())
              .addLast("handler", new DataFetchServerHandler(sessionIdToJobRunner))
          }
        }
      case NettyMessageType.PROTOBUF_MESSAGE | _ =>
        new ChannelInitializer[SocketChannel] {
          override def initChannel(channel: SocketChannel): Unit = {
            channel.pipeline
              .addLast(new ProtobufVarint32FrameDecoder())
              .addLast("decoder", new ProtobufDecoder(ProtoMessage.getDefaultInstance))
              .addLast(new ProtobufVarint32LengthFieldPrepender())
              .addLast("encoder", new ProtobufEncoder())
              .addLast("handler", new DataFetchServerProtoHandler(sessionIdToJobRunner))
          }
        }
    }
  }
}


