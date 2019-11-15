package moonbox.network.client;

import io.netty.channel.Channel;
import io.netty.util.ReferenceCountUtil;
import moonbox.network.protocol.ResponseMessage;
import moonbox.network.protocol.RpcFailure;
import moonbox.network.util.NettyUtils;
import moonbox.network.protocol.RpcResponse;
import moonbox.network.server.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class TransportResponseHandler extends MessageHandler<ResponseMessage> {
  private static final Logger logger = LoggerFactory.getLogger(TransportResponseHandler.class);

  private final Channel channel;

  private final Map<Long, ResponseCallback> outstandingRpcs;

  private final AtomicLong timeOfLastRequestNs;

  public TransportResponseHandler(Channel channel) {
    this.channel = channel;
    this.outstandingRpcs = new ConcurrentHashMap<>();
    this.timeOfLastRequestNs = new AtomicLong(0);
  }

  public void addRpcRequest(long requestId, ResponseCallback callback) {
    updateTimeOfLastRequest();
    outstandingRpcs.put(requestId, callback);
  }

  public void removeRpcRequest(long requestId) {
    outstandingRpcs.remove(requestId);
  }

  @Override
  public void channelInactive() {
    if (outstandingRpcs.size() > 0) {
      String remoteAddress = NettyUtils.getRemoteAddress(channel);
      logger.error("Still have {} requests outstanding when connection from {} is closed",
          outstandingRpcs.size(), remoteAddress);
      failOutstandingRequests(new IOException("Connection from " + remoteAddress + " closed"));
    }
  }

  @Override
  public void handle(ResponseMessage message) throws Exception {
    if (message instanceof RpcResponse) {
      RpcResponse resp = (RpcResponse) message;
      ResponseCallback listener = outstandingRpcs.get(resp.requestId);
      if (listener == null) {
        logger.warn("Ignoring response for RPC {} from {} since it is not outstanding",
            resp.requestId, NettyUtils.getRemoteAddress(channel));
      } else {
        outstandingRpcs.remove(resp.requestId);
        try {
          listener.onSuccess(resp.body());
        } finally {
          ReferenceCountUtil.release(message);
        }
      }
    } else if (message instanceof RpcFailure) {
      RpcFailure resp = (RpcFailure) message;
      ResponseCallback listener = outstandingRpcs.get(resp.requestId);
      if (listener == null) {
        logger.warn("Ignoring response for RPC {} from {} since it is not outstanding",
            resp.requestId, NettyUtils.getRemoteAddress(channel));
      } else {
        outstandingRpcs.remove(resp.requestId);
        try {
          listener.onFailure(new RuntimeException(resp.errorString));
        } finally {
          ReferenceCountUtil.release(message);
        }
      }
    } else {
      ReferenceCountUtil.release(message);
      throw new IllegalStateException("Unknown response type: " + message.type());
    }

  }

  @Override
  public void channelActive() {
  }

  @Override
  public void exceptionCaught(Throwable cause) {
    if (outstandingRpcs.size() > 0) {
      String remoteAddress = NettyUtils.getRemoteAddress(channel);
      logger.error("Still have {} requests outstanding when connection from {} is closed",
          outstandingRpcs.size(), remoteAddress);
      failOutstandingRequests(cause);
    }
  }

  private void failOutstandingRequests(Throwable cause) {
    for (Map.Entry<Long, ResponseCallback> entry : outstandingRpcs.entrySet()) {
      try {
        entry.getValue().onFailure(cause);
      } catch (Exception e) {
        logger.warn("ResponseCallback.onFailure throws exception", e);
      }
    }
    outstandingRpcs.clear();
  }

  public long getTimeOfLastRequestNs() {
    return timeOfLastRequestNs.get();
  }

  public void updateTimeOfLastRequest() {
    timeOfLastRequestNs.set(System.nanoTime());
  }

  public int numOutstandingRequests() {
    return outstandingRpcs.size();
  }

}
