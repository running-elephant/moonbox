package moonbox.network.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.StandardCharsets;

public class JavaUtils {
  public static ByteBuf stringToBytes(String s) {
    return Unpooled.wrappedBuffer(s.getBytes(StandardCharsets.UTF_8));
  }

  public static String bytesToString(ByteBuf b) {
    return b.toString(StandardCharsets.UTF_8);
  }

  public static byte[] byteBufToByteArray(ByteBuf buf) {
    byte[] array;
    int length = buf.readableBytes();
    if (buf.hasArray()) {
      array = buf.array();
    } else {
      array = new byte[length];
          buf.getBytes(buf.readerIndex(), array, 0, length);
    }
    return array;
  }
}
