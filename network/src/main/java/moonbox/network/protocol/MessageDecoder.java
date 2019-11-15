package moonbox.network.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;
@ChannelHandler.Sharable
public class MessageDecoder extends MessageToMessageDecoder<ByteBuf> {

  public static final MessageDecoder INSTANCE = new MessageDecoder();

  private MessageDecoder() {}

  @Override
  protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
    Message.Type msgType = Message.Type.decode(in);
    Message decoded = decode(msgType, in);
    assert decoded.type() == msgType;
    out.add(decoded);
  }

  private Message decode(Message.Type msgType, ByteBuf buf) {
    switch (msgType) {
      case RpcRequest:
        return RpcRequest.decode(buf);
      case RpcResponse:
        return RpcResponse.decode(buf);
      case RpcFailure:
        return RpcFailure.decode(buf);
      default:
        throw new IllegalArgumentException("Unexpected message type: " + msgType);
    }
  }
}
