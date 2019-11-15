package moonbox.network.server;

import io.netty.buffer.ByteBuf;
import moonbox.network.client.ResponseCallback;

public class RpcCallContext {
  private final ResponseCallback callback;

  public RpcCallContext(ResponseCallback callback) {
    this.callback = callback;
  }

  public void reply(ByteBuf message) {
    callback.onSuccess(message);
  }

  public void sendFailure(Throwable e) {
    callback.onFailure(e);
  }
}
