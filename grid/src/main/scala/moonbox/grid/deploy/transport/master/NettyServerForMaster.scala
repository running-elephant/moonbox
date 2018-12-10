/*-
 * <<
 * Moonbox
 * ==
 * Copyright (C) 2016 - 2018 EDP
 * ==
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * >>
 */

package moonbox.grid.deploy.transport.master.orignal

import java.lang.{Boolean => JBoolean, Integer => JInt}
import java.net.InetSocketAddress
import java.util.concurrent.{ConcurrentHashMap, TimeUnit}

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel._
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.util.concurrent.DefaultThreadFactory
import moonbox.common.{MbConf, MbLogging}
import moonbox.grid.deploy.MbService
import moonbox.grid.deploy.transport.handler.{Handlers, JavaMessage, NettyMessageType, ProtobufMessage}

class NettyServer(host: String, port: Int, conf: MbConf, mbService: MbService) extends MbLogging {
  private val channelToToken = new ConcurrentHashMap[Channel, String]()
  private val channelToSessionId = new ConcurrentHashMap[Channel, String]()
  private var channelFuture: ChannelFuture = _
  private var bootstrap: ServerBootstrap = _
//  private var protocol = conf.getOption("message.protocol").getOrElse("java")
  private var protocol = conf.getOption("message.protocol").getOrElse("protobuf")

  def start(): Int = {
    val messageType: NettyMessageType = protocol match {
      case "protobuf" => ProtobufMessage
      case _ => JavaMessage
    }
    val channelHandler = Handlers.getMasterHandler(messageType, channelToToken, channelToSessionId, mbService)
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
    channelFuture = bootstrap.bind(new InetSocketAddress(host, port))
    channelFuture.syncUninterruptibly()
    logInfo(s"Jdbc server listening on $host:$port ...")
    channelFuture.channel.closeFuture.addListener(new ChannelFutureListener {
      override def operationComplete(future: ChannelFuture) = {
        channelToToken.clear()
        channelToSessionId.clear()
        stop()
      }
    })
    channelFuture.channel().localAddress().asInstanceOf[InetSocketAddress].getPort
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
}


