package moonbox.network.client;

import com.google.common.base.Throwables;
import com.google.common.util.concurrent.SettableFuture;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import moonbox.network.protocol.RpcRequest;
import moonbox.network.util.NettyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.net.SocketAddress;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class TransportClient implements Closeable {
  private static final Logger logger = LoggerFactory.getLogger(TransportClient.class);

  private final Channel channel;
  private final TransportResponseHandler handler;
  private volatile boolean timedOut;


  public TransportClient(Channel channel, TransportResponseHandler handler) {
    this.channel = channel;
    this.handler = handler;
    this.timedOut = false;
  }

  public boolean isActive() {
    return !timedOut && (channel.isOpen() || channel.isActive());
  }

  public SocketAddress getSocketAddress() { return channel.remoteAddress(); }


  public long send(ByteBuf request, ResponseCallback callback) {
    long requestId = Math.abs(UUID.randomUUID().getLeastSignificantBits());
    handler.addRpcRequest(requestId, callback);
    channel.writeAndFlush(new RpcRequest(requestId, request)).addListener(future -> {
      if (!future.isSuccess()) {
        handler.removeRpcRequest(requestId);
        channel.close();
        try {
          String errorMsg = String.format("Failed to send RPC %s to %s: %s", requestId,
              NettyUtils.getRemoteAddress(channel), future.cause());
          callback.onFailure(new IOException(errorMsg, future.cause()));
        } catch (Exception e) {
          logger.error("Uncaught exception in RPC response callback handler!", e);
        }
      }
    });
    return requestId;
  }

  public ByteBuf sendSync(ByteBuf request, long timeoutMs) {
    final SettableFuture<ByteBuf> result = SettableFuture.create();

    send(request, new ResponseCallback() {
      @Override
      public void onSuccess(ByteBuf response) {
        result.set(response);
      }

      @Override
      public void onFailure(Throwable e) {
        result.setException(e);
      }
    });

    try {
      return result.get(timeoutMs, TimeUnit.MILLISECONDS);
    } catch (ExecutionException e) {
      throw Throwables.propagate(e.getCause());
    } catch (Exception e) {
      throw Throwables.propagate(e);
    }
  }


  public void timeOut() {
    this.timedOut = true;
  }

  @Override
  public void close() {
    channel.close().awaitUninterruptibly(10, TimeUnit.SECONDS);
  }
}
