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

package moonbox.client

import java.io.IOException
import java.net.SocketAddress
import java.util.concurrent.{ConcurrentHashMap, TimeUnit}
import java.util.concurrent.atomic.AtomicLong

import io.netty.bootstrap.Bootstrap
import io.netty.channel._
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.handler.codec.serialization.{ClassResolvers, ObjectDecoder, ObjectEncoder}
import io.netty.util.concurrent.DefaultThreadFactory
import moonbox.protocol.client._

object JdbcClient {
	val DAEMON_NIOEVENTLOOPGROUP = new NioEventLoopGroup(0, new DefaultThreadFactory(this.getClass, true))
}

class JdbcClient(host: String, port: Int) {
	import JdbcClient._
	private var bootstrap: Bootstrap = _
	private var channel: Channel = _
	private val messageId = new AtomicLong(0L)
	private var connected: Boolean = false
	private var timeout = 1000 * 15: Long //time unit: ms
	private val promises = new ConcurrentHashMap[Long, ChannelPromise]
	private val responses = new ConcurrentHashMap[Long, Outbound]
	private val callbacks = new ConcurrentHashMap[Long, Outbound => Any]

	def setTimeout(timeout: Long): this.type = {
		this.timeout = timeout
		this
	}

	def getTimeout: Long = {
		this.timeout
	}

	@throws(classOf[Exception])
	def connect(): Unit = {
		bootstrap = new Bootstrap()
			.group(DAEMON_NIOEVENTLOOPGROUP)
			.channel(classOf[NioSocketChannel])
			.option[java.lang.Boolean](ChannelOption.SO_KEEPALIVE, true)
			.option[java.lang.Boolean](ChannelOption.TCP_NODELAY, true)
			.option[java.lang.Integer](ChannelOption.SO_RCVBUF, 10240)
			.handler(new ChannelInitializer[SocketChannel]() {
				override def initChannel(ch: SocketChannel) = {
					ch.pipeline.addLast(new ObjectEncoder,
						new ObjectDecoder(Int.MaxValue, ClassResolvers.cacheDisabled(null)),
						new ClientInboundHandler(promises, responses, callbacks))
				}
			})
		val channelFuture = bootstrap.connect(host, port)
		channelFuture.syncUninterruptibly()
		channel = channelFuture.channel()
		if (!channelFuture.await(timeout)) {
			throw new IOException(s"Connecting to $host timed out (${timeout} ms)")
		} else if (channelFuture.cause != null) {
			throw new IOException(s"Failed to connect to $host", channelFuture.cause)
		}
		this.connected = true
	}

	@throws(classOf[Exception])
	def getRemoteAddress: SocketAddress = {
		if (channel != null) channel.remoteAddress()
		else throw new ChannelException("channel unestablished")
	}

  def genMessageId: Long = messageId.getAndIncrement()

  def sendOneWayMessage(msg: Any) = send(msg)

  def sendWithCallback(msg: Any, callback: Outbound => Any) = send(msg, callback)

  def isConnected: Boolean = {
	  this.connected
  }

  def isActive: Boolean = {
	  channel.isActive
  }

  def close(): Unit = {
	  if (channel != null) {
		  channel.close()
	  }
	  if (bootstrap != null && bootstrap.group() != null) {
		  bootstrap.group().shutdownGracefully()
	  }
	  bootstrap = null
  }

  @throws(classOf[Exception])
  def sendAndReceive(message: Any): Outbound = {
	  message match {
		  case in: Inbound =>
			  send(in)
			  val messageId = in.getId
			  try {
				  if (promises.containsKey(messageId)) {
					  val promise = promises.get(messageId)
					  if (!promise.await(timeout))
						  throw new Exception(s"No response within $timeout ms")
					  else {
						  if (promise.isSuccess) {
							  responses.get(messageId)
						  } else {
							  throw promise.cause()
						  }
					  }
				  } else {
					  throw new Exception(s"Send message failed: $message")
				  }
			  } finally {
				  release(messageId)
			  }
		  case _ => throw new Exception("Unsupported message format")
	  }
  }

  private def release(key: Long): Unit = {
    promises.remove(key)
    responses.remove(key)
  }

	@throws(classOf[Exception])
	def send(message: Any): Unit = {
		message match {
		  case in: Inbound =>
			  val messageId = in.getId
			  promises.put(messageId, channel.newPromise())
			  channel.writeAndFlush(in).sync()
		  case _ => throw new Exception("Unsupported message")
		}
	}

  def getRemoteAddress(channel: Channel): String = {
    channel.remoteAddress().toString
  }

	@throws(classOf[Exception])
  def send(message: Any, callback: Outbound => Any): Unit = {
		message match {
			case in: Inbound =>
				callbacks.put(in.getId, callback)
				channel.writeAndFlush(message).sync()
			case _ => throw new Exception("Unsupported message")
		}
  }

}


