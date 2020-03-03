package moonbox.network.client;

import io.netty.buffer.ByteBuf;

public interface ResponseCallback {

  void onSuccess(ByteBuf response);

  void onFailure(Throwable e);
}
