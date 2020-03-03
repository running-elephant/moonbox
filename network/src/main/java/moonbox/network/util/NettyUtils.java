package moonbox.network.util;

import io.netty.channel.Channel;

public class NettyUtils {

  public static String getRemoteAddress(Channel channel) {
    if (channel != null && channel.remoteAddress() != null) {
      return channel.remoteAddress().toString();
    }
    return "<unknown remote>";
  }
}
