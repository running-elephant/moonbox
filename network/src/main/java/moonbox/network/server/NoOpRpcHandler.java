package moonbox.network.server;

import io.netty.buffer.ByteBuf;

public class NoOpRpcHandler extends RpcHandler {
  @Override
  public void receive(ByteBuf request, RpcCallContext context) {
    throw new UnsupportedOperationException("Cannot handle messages");
  }
}
