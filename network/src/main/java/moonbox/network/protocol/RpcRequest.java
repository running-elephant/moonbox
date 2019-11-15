package moonbox.network.protocol;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import io.netty.buffer.ByteBuf;

public class RpcRequest extends AbstractMessage implements RequestMessage {
  public final long requestId;

  public RpcRequest(long requestId, ByteBuf message) {
    super(message, true);
    this.requestId = requestId;
  }

  @Override
  public Type type() {
    return Type.RpcRequest;
  }

  @Override
  public int encodedLength() {
    return 8;
  }

  @Override
  public void encode(ByteBuf buf) {
    buf.writeLong(requestId);
  }

  public static RpcRequest decode(ByteBuf buf) {
    long requestId = buf.readLong();
    return new RpcRequest(requestId, buf.retain());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(requestId, body());
  }

  @Override
  public boolean equals(Object other) {
    if (other instanceof RpcRequest) {
      RpcRequest o = (RpcRequest) other;
      return requestId == o.requestId && super.equals(o);
    }
    return false;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("requestId", requestId)
        .add("body", body())
        .toString();
  }
}
