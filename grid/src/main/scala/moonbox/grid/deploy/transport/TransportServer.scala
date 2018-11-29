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

package moonbox.grid.deploy.transport

import java.net.InetSocketAddress
import java.util.concurrent.{ConcurrentHashMap, TimeUnit}
import java.lang.{Boolean => JBoolean, Integer => JInt}

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.channel._
import io.netty.handler.codec.serialization.{ClassResolvers, ObjectDecoder, ObjectEncoder}
import io.netty.handler.timeout.ReadTimeoutHandler
import moonbox.common.{MbConf, MbLogging}
import moonbox.grid.config._
import moonbox.grid.deploy.MbService

private[deploy] class TransportServer(host: String,
	port: Int, conf: MbConf, service: MbService) extends MbLogging {
	private val maxRetries: Int = conf.get(PORT_MAX_RETRIES)
	private val channelToToken = new ConcurrentHashMap[Channel, String]()
	private val channelToSessionId = new ConcurrentHashMap[Channel, String]()
	// time unit: s
	private val READ_TIMEOUT: Int = 60 * 60
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
							.addLast("handler", new TransportServerHandler(channelToToken, channelToSessionId, service))
					}
				})
			.option[JInt](ChannelOption.SO_BACKLOG, 1024)
			.childOption[JBoolean](ChannelOption.SO_KEEPALIVE, true)
			.childOption[JInt](ChannelOption.SO_SNDBUF, 10240)
			.childOption[JInt](ChannelOption.SO_RCVBUF, 1024)

		// Bind and start to accept incoming connections.
		logInfo("Starting jdbc server ...")
		for (offset <- 0 to maxRetries) {
			val tryPort = if (port == 0) port else port + offset
			try {
				channelFuture = bootstrap.bind(host, tryPort)
				channelFuture.syncUninterruptibly()
				logInfo(s"Jdbc server is listening on $host:$tryPort.")
				return tryPort
			} catch {
				case e: Exception =>
					if (offset >= maxRetries) {
						throw e
					}
					if (port == 0) {
						logWarning(s"JdbcServer could not bind on a random free pot. Attempting again.")
					} else {
						logWarning(s"JdbcServer could not bind on port $tryPort. " + "" +
							s"Attempting port ${tryPort + 1}")
					}
			}
		}


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
