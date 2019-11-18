package moonbox.network.protocol;

import io.netty.buffer.ByteBuf;

public class RpcResponse extends AbstractMessage implements ResponseMessage  {
  public long requestId;

  public RpcResponse(long requestId, ByteBuf message) {
    super(message, true);
    this.requestId = requestId;
  }

  @Override
  public Type type() {
    return Type.RpcResponse;
  }

  @Override
  public int encodedLength() {
    return 8;
  }

  @Override
  public void encode(ByteBuf buf) {
    buf.writeLong(requestId);
  }

  public static RpcResponse decode(ByteBuf buf) {
    long requestId = buf.readLong();
    return new RpcResponse(requestId, buf.retain());
  }
}
