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

package moonbox.grid.deploy2.transport.server

import java.net.InetSocketAddress
import java.util.concurrent.ConcurrentHashMap

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel._
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.codec.serialization.{ClassResolvers, ObjectDecoder, ObjectEncoder}
import io.netty.handler.timeout.ReadTimeoutHandler
import moonbox.common.{MbConf, MbLogging}
import moonbox.grid.deploy2.MbService

class JdbcServer(host: String, port: Int, conf: MbConf, mbService: MbService) extends MbLogging {
  var channel: Channel = _
  var channel2SessionIdAndToken = new ConcurrentHashMap[Channel, (String, String)]
  var bossGroup: NioEventLoopGroup = _
  var workerGroup: NioEventLoopGroup = _
  var readTimeout: Int = 60 * 60 // time unit: s

  def start(): Int = {
    val bossGroup = new NioEventLoopGroup
    val workerGroup = new NioEventLoopGroup
    val b = new ServerBootstrap
    b.group(bossGroup, workerGroup)
      .channel(classOf[NioServerSocketChannel])
      .childHandler(
        new ChannelInitializer[SocketChannel]() {
          override def initChannel(ch: SocketChannel) = {
            ch.pipeline.addLast("decode", new ObjectDecoder(Int.MaxValue, ClassResolvers.cacheDisabled(null)))
              .addLast("encode", new ObjectEncoder)
              .addLast("timeout handler", new ReadTimeoutHandler(readTimeout))
              .addLast("handler", new JdbcServerHandler(channel2SessionIdAndToken, mbService))
          }
        })
      .option[java.lang.Integer](ChannelOption.SO_BACKLOG, 1024)
      .childOption[java.lang.Boolean](ChannelOption.SO_KEEPALIVE, true)
      .childOption[java.lang.Integer](ChannelOption.SO_SNDBUF, 10240)
      .childOption[java.lang.Integer](ChannelOption.SO_RCVBUF, 1024)

    // Bind and start to accept incoming connections.
    val channelFuture = b.bind(host, port)
    channelFuture.syncUninterruptibly()
    logInfo(s"Jdbc server listening on $host:$port ...")
    // Wait until the server socket is closed.
    // In this example, this does not happen, but you can do that to gracefully
    // shut down your server.
    channel = channelFuture.channel()
    channelFuture.channel.closeFuture.addListener(new ChannelFutureListener {
      override def operationComplete(future: ChannelFuture) = {
        channel2SessionIdAndToken.clear()
        stop()
      }
    })
    channelFuture.channel().localAddress().asInstanceOf[InetSocketAddress].getPort
  }

  def stop(): Unit = {
    if (channel != null)
      channel.close()
    if (bossGroup != null)
      bossGroup.shutdownGracefully()
    if (workerGroup != null)
      workerGroup.shutdownGracefully()
  }
}


