package moonbox.network.server;

import io.netty.buffer.ByteBuf;

public abstract class RpcHandler {

  public abstract void receive(ByteBuf request, RpcCallContext context);
}
