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

package moonbox.network.server

import java.net.InetSocketAddress
import java.util.concurrent.TimeUnit

import com.google.protobuf.MessageLite
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel._
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.codec.protobuf.{ProtobufDecoder, ProtobufEncoder, ProtobufVarint32FrameDecoder, ProtobufVarint32LengthFieldPrepender}
import io.netty.util.concurrent.DefaultThreadFactory
import moonbox.common.{MbConf, MbLogging}
import moonbox.network.config.PORT_MAX_RETRY_TIMES

class TransportServer(host: String, port: Int, conf: MbConf, messageLite: MessageLite, handlers: List[ChannelHandler]) extends MbLogging {

	private var channelFuture: ChannelFuture = _
	private var bootstrap: ServerBootstrap = _

	def start(): Int = {

		val eventLoopGroup = new NioEventLoopGroup(0, new DefaultThreadFactory(this.getClass, true))
		bootstrap = new ServerBootstrap()
				.group(eventLoopGroup, eventLoopGroup)
				.channel(classOf[NioServerSocketChannel])
				.option(ChannelOption.SO_BACKLOG, Integer.valueOf(1024))
				.childOption(ChannelOption.SO_KEEPALIVE, java.lang.Boolean.valueOf(true))
				.childOption(ChannelOption.SO_RCVBUF, Integer.valueOf(10240))
				.childOption(ChannelOption.SO_SNDBUF, Integer.valueOf(10240))
				.childHandler(new ChannelInitializer[SocketChannel] {
					override def initChannel(channel: SocketChannel): Unit = {
						channel.pipeline
								.addLast(new ProtobufVarint32FrameDecoder())
								.addLast("decoder", new ProtobufDecoder(messageLite))
								.addLast(new ProtobufVarint32LengthFieldPrepender())
								.addLast("encoder", new ProtobufEncoder())
								.addLast(handlers: _*)
					}
				})

		// Bind and start to accept incoming connections.
		logInfo("Starting Master Transport server ...")
		val maxRetries = conf.get(PORT_MAX_RETRY_TIMES)
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
						logWarning(s"TransportServer could not bind on a random free port. Attempting again.")
					} else {
						logWarning(s"TransportServer could not bind on port $tryPort. Attempting port ${tryPort + 1}")
					}
			}
		}

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
