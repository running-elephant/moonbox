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
import java.util.concurrent.{ConcurrentHashMap, TimeUnit}
import java.lang.{Boolean => JBoolean, Integer => JInt}

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
	private val channelToToken = new ConcurrentHashMap[Channel, String]()
	private val channelToSessionId = new ConcurrentHashMap[Channel, String]()
	private val READ_TIMEOUT: Int = 60 * 60 // time unit: s
	private var channelFuture: ChannelFuture = _
	private var bootstrap: ServerBootstrap = _

	def start(): Int = {
		val bossGroup = new NioEventLoopGroup()
		val workerGroup = new NioEventLoopGroup()
		bootstrap = new ServerBootstrap()
			.group(bossGroup, workerGroup)
		  	.channel(classOf[NioServerSocketChannel])
		  	.childHandler(
				new ChannelInitializer[SocketChannel]() {
			  		override def initChannel(channel: SocketChannel) = {
						channel.pipeline.addLast("decode", new ObjectDecoder(Int.MaxValue, ClassResolvers.cacheDisabled(null)))
				  		.addLast("encode", new ObjectEncoder())
				  		.addLast("timeout handler", new ReadTimeoutHandler(READ_TIMEOUT))
				  		.addLast("handler", new JdbcServerHandler(channelToToken, channelToSessionId, mbService))
			  }
			})
		  .option[JInt](ChannelOption.SO_BACKLOG, 1024)
		  .childOption[JBoolean](ChannelOption.SO_KEEPALIVE, true)
		  .childOption[JInt](ChannelOption.SO_SNDBUF, 10240)
		  .childOption[JInt](ChannelOption.SO_RCVBUF, 1024)

		// Bind and start to accept incoming connections.
		channelFuture = bootstrap.bind(host, port)
		channelFuture.syncUninterruptibly()
		logInfo(s"Jdbc server listening on $host:$port ...")
		// Wait until the server socket is closed.
		// In this example, this does not happen, but you can do that to gracefully
		// shut down your server.
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


