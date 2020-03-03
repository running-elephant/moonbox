package moonbox.network.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import java.util.List;
import static io.netty.buffer.Unpooled.wrappedBuffer;

@ChannelHandler.Sharable
public class MessageEncoder extends MessageToMessageEncoder<Message>{

  public static final MessageEncoder INSTANCE = new MessageEncoder();

  private MessageEncoder() {}

  @Override
  protected void encode(ChannelHandlerContext ctx, Message in, List<Object> out) throws Exception {

    Message.Type type = in.type();

    int headerLength = type.encodedLength() + in.encodedLength();
    ByteBuf header = ctx.alloc().heapBuffer(headerLength);
    type.encode(header);
    in.encode(header);

    if (in.hasBody()) {
      out.add(wrappedBuffer(header, in.body()));
    } else {
      out.add(header);
    }

  }


}
