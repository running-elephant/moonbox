/*-
 * <<
 * Moonbox
 * ==
 * Copyright (C) 2016 - 2019 EDP
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

import java.lang.{Boolean => JBoolean, Integer => JInt}
import java.net.InetSocketAddress
import java.util.concurrent.{ConcurrentHashMap, TimeUnit}

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel._
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.codec.protobuf.{ProtobufDecoder, ProtobufEncoder, ProtobufVarint32FrameDecoder, ProtobufVarint32LengthFieldPrepender}
import io.netty.util.concurrent.DefaultThreadFactory
import moonbox.common.{MbConf, MbLogging}
import moonbox.grid.config._
import moonbox.grid.deploy.MoonboxService
import moonbox.message.protobuf.ProtoMessage

private[deploy] class TransportServer(host: String, port: Int, conf: MbConf, mbService: MoonboxService) extends MbLogging {

	private val maxRetries: Int = conf.get(PORT_MAX_RETRIES)
	private val channelToToken = new ConcurrentHashMap[Channel, String]()
	private val channelToSessionId = new ConcurrentHashMap[Channel, String]()
	private var channelFuture: ChannelFuture = _
	private var bootstrap: ServerBootstrap = _

	def start(): Int = {

		val bossGroup = new NioEventLoopGroup(0, new DefaultThreadFactory(this.getClass, true))
		val workerGroup = bossGroup
		bootstrap = new ServerBootstrap()
			.group(bossGroup, workerGroup)
			.channel(classOf[NioServerSocketChannel])
			.childOption[JBoolean](ChannelOption.SO_KEEPALIVE, true)
			.option[JInt](ChannelOption.SO_BACKLOG, 1024)
			.childOption[JInt](ChannelOption.SO_RCVBUF, 10240)
			.childOption[JInt](ChannelOption.SO_SNDBUF, 10240)
			.childHandler(new ChannelInitializer[SocketChannel] {
				override def initChannel(channel: SocketChannel): Unit = {
					channel.pipeline
						.addLast(new ProtobufVarint32FrameDecoder())
						.addLast("decoder", new ProtobufDecoder(ProtoMessage.getDefaultInstance))
						.addLast(new ProtobufVarint32LengthFieldPrepender())
						.addLast("encoder", new ProtobufEncoder())
						.addLast("handler", new TransportServerProtoHandler(channelToToken, channelToSessionId, mbService))
				}
			})

		// Bind and start to accept incoming connections.
		logInfo("Starting Master Transport server ...")
		for (offset <- 0 to maxRetries) {
			val tryPort = if (port == 0) port else port + offset
			try {
				channelFuture = bootstrap.bind(host, tryPort)
				channelFuture.syncUninterruptibly()
				logInfo(s"Master Transport server is listening on $host:$tryPort.")
				return tryPort
			} catch {
				case e: Exception =>
					if (offset >= maxRetries) {
						throw e
					}
					if (port == 0) {
						logWarning(s"Master Transport server could not bind on a random free port. Attempting again.")
					} else {
						logWarning(s"Master Transport server could not bind on port $tryPort. Attempting port ${tryPort + 1}")
					}
			}
		}

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
