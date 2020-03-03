package moonbox.network.server;

public abstract class MessageHandler<T> {

  public abstract void handle(T message) throws Exception;

  public abstract void channelActive();

  public abstract void exceptionCaught(Throwable cause);

  public abstract void channelInactive();

}
