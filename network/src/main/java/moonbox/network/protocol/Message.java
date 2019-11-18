package moonbox.network.protocol;

import io.netty.buffer.ByteBuf;

public interface Message extends Encodable {

  Type type();

  ByteBuf body();

  boolean hasBody();


  enum Type implements Encodable {
    RpcRequest(1), RpcResponse(2), RpcFailure(3);

    private final byte id;

    Type(int id) {
      assert id < 128 : "Cannot have more than 128 message types";
      this.id = (byte) id;
    }

    public byte id() { return id; }

    @Override public int encodedLength() { return 1; }

    @Override public void encode(ByteBuf buf) { buf.writeByte(id); }

    public static Type decode(ByteBuf buf) {
      byte id = buf.readByte();
      switch (id) {
        case 1: return RpcRequest;
        case 2: return RpcResponse;
        case 3: return RpcFailure;
        default: throw new IllegalArgumentException("Unknown message type: " + id);
      }
    }
  }
}
