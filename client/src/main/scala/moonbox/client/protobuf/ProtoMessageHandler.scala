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

package moonbox.client.protobuf

import java.util.concurrent.ConcurrentHashMap

import io.netty.channel.{ChannelHandlerContext, ChannelInboundHandlerAdapter, ChannelPromise}
import io.netty.util.ReferenceCountUtil
import moonbox.message.protobuf._

class ProtoMessageHandler(promises: ConcurrentHashMap[Long, ChannelPromise],
                          responses: ConcurrentHashMap[Long, ProtoMessage],
                          callbacks: ConcurrentHashMap[Long, ProtoMessage => Any])
  extends ChannelInboundHandlerAdapter {

  private var ctx: ChannelHandlerContext = _

  override def channelActive(ctx: ChannelHandlerContext): Unit = {
    this.ctx = ctx
    super.channelActive(ctx)
  }

  override def channelRead(ctx: ChannelHandlerContext, msg: Any): Unit = {
    try {
      if (!promises.isEmpty) {
        handleMessageWithPromise(msg)
      }
      if (!callbacks.isEmpty) {
        handleMessageWithCallback(msg)
      }
    } finally {
      ReferenceCountUtil.release(msg)
    }
  }

  override def exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable): Unit = {
    try {
      super.exceptionCaught(ctx, cause)
    } finally {
      ctx.close()
    }
  }

  private def handleMessageWithCallback(response: Any): Any = {
    response match {
      case resp: ProtoMessage =>
        val msgId = resp.getMessageId
        if (callbacks.contains(msgId)){
          val callback = callbacks.remove(msgId)
          callback(resp)
        }
      case _ => throw new Exception("Unsupported message")
    }
  }

  private def handleMessageWithPromise(message: Any) = {
    message match {
      case resp: ProtoMessage =>
        val id = resp.getMessageId
        if (promises.containsKey(id)) {
          responses.put(id, resp)
          promises.get(id).setSuccess()
        }
      case _ => throw new Exception("Unsupported message")
    }
  }
}

