package moonbox.network.protocol;

import io.netty.buffer.ByteBuf;

public abstract class AbstractMessage implements Message {
  private final ByteBuf body;
  private final boolean hasBody;

  protected AbstractMessage() {
    this(null, false);
  }

  protected AbstractMessage(ByteBuf body, boolean hasBody) {
    this.body = body;
    this.hasBody = hasBody;
  }
  @Override
  public ByteBuf body() {
    return body;
  }

  @Override
  public boolean hasBody() {
    return this.hasBody;
  }
}
