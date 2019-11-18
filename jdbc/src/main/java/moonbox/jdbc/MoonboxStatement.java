package moonbox.jdbc;


import com.google.common.util.concurrent.SettableFuture;
import io.netty.buffer.ByteBuf;
import moonbox.jdbc.util.Utils;
import moonbox.network.client.ResponseCallback;
import moonbox.protocol.protobuf.*;

import java.sql.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class MoonboxStatement implements Statement {
  private static final int DEFAULT_QUERY_TIMEOUT = 60 * 60;// 1h
  private static final int DEFAULT_FETCH_SIZE = 1000;
  private static final int DEFAULT_MAX_ROWS = 1000;

  private MoonboxConnection conn;
  private String database;
  private int maxRows;
  private int fetchSize;
  private int queryTimeout;
  private String sessionId;
  private SettableFuture<ByteBuf> result;
  private AtomicBoolean executing = new AtomicBoolean(false);

  MoonboxStatement(MoonboxConnection conn, String database) throws SQLException {
    this.conn = conn;
    this.database = database;
    this.maxRows = Integer.valueOf(this.conn.getClientInfo().getProperty("maxRows", String.valueOf(DEFAULT_MAX_ROWS)));
    this.fetchSize = Integer.valueOf(this.conn.getClientInfo().getProperty("fetchSize", String.valueOf(DEFAULT_FETCH_SIZE)));
    this.queryTimeout = Integer.valueOf(this.conn.getClientInfo().getProperty("queryTimeout", String.valueOf(DEFAULT_QUERY_TIMEOUT)));
    this.sessionId = openSession();
  }

  private String openSession() throws SQLException {
    OpenSessionRequestPB openSessionRequestPB = OpenSessionRequestPB.newBuilder().build();
    ByteBuf byteBuf = conn.getClient().sendSync(Utils.messageToBytebuf(openSessionRequestPB), conn.getConnectTimeout());
    String sessionId;
    try {
      OpenSessionResponsePB openSessionResponsePB =
          OpenSessionResponsePB
              .getDefaultInstance()
              .getParserForType()
              .parseFrom(Utils.byteBufToByteArray(byteBuf));
      sessionId = openSessionResponsePB.getSessionId();
    } catch (Exception e) {
      throw new SQLException(e);
    }
    return sessionId;
  }

  @Override
  public ResultSet executeQuery(String sql) throws SQLException {
    synchronized (this) {
      result = SettableFuture.create();
      ExecutionRequestPB requestPB =
          ExecutionRequestPB.newBuilder()
              .setFetchSize(fetchSize)
              .setMaxRows(maxRows)
              .addAllSqls(Utils.splitSQLs(sql))
              .build();

      conn.getClient().send(Utils.messageToBytebuf(requestPB), new ResponseCallback() {
        @Override
        public void onSuccess(ByteBuf response) {
          result.set(response);
        }

        @Override
        public void onFailure(Throwable e) {
          result.setException(e);
        }
      });

      executing.compareAndSet(false, true);

      ByteBuf byteBuf;
      try {
        byteBuf = result.get(queryTimeout, TimeUnit.SECONDS);
      } catch (Exception e) {
        cancel();
        throw new SQLException(e);
      } finally {
        executing.compareAndSet(true, false);
      }
      ExecutionResultPB executionResultPB;
      try {
        executionResultPB = ExecutionResultPB.getDefaultInstance().getParserForType().parseFrom(Utils.byteBufToByteArray(byteBuf));
      } catch (Exception e) {
        throw new SQLException(e);
      }
      return resultConvert(executionResultPB);
    }

  }

  private ResultSet resultConvert(ExecutionResultPB resultPB) {
    return new MoonboxResultSet();
  }

  @Override
  public int executeUpdate(String sql) throws SQLException {
    throw new SQLException("Unsupport operation");
  }

  @Override
  public void close() throws SQLException {
    CloseSessionRequestPB close = CloseSessionRequestPB.newBuilder().setSessionId(sessionId).build();
    conn.getClient().sendSync(Utils.messageToBytebuf(close), conn.getConnectTimeout());
  }

  @Override
  public int getMaxFieldSize() throws SQLException {
    return 0;
  }

  @Override
  public void setMaxFieldSize(int max) throws SQLException {

  }

  @Override
  public int getMaxRows() throws SQLException {
    return this.maxRows;
  }

  @Override
  public void setMaxRows(int max) throws SQLException {
    this.maxRows = max;
  }

  @Override
  public void setEscapeProcessing(boolean enable) throws SQLException {

  }

  @Override
  public int getQueryTimeout() throws SQLException {
    return this.queryTimeout;
  }

  @Override
  public void setQueryTimeout(int seconds) throws SQLException {
    this.queryTimeout = seconds;
  }

  @Override
  public void cancel() throws SQLException {
    ExecutionCancelRequestPB cancelRequestPB = ExecutionCancelRequestPB.newBuilder().setSessionId(sessionId).build();
    conn.getClient().sendSync(Utils.messageToBytebuf(cancelRequestPB), conn.getConnectTimeout());
  }

  @Override
  public SQLWarning getWarnings() throws SQLException {
    return null;
  }

  @Override
  public void clearWarnings() throws SQLException {

  }

  @Override
  public void setCursorName(String name) throws SQLException {

  }

  @Override
  public boolean execute(String sql) throws SQLException {
    return false;
  }

  @Override
  public ResultSet getResultSet() throws SQLException {
    return null;
  }

  @Override
  public int getUpdateCount() throws SQLException {
    return 0;
  }

  @Override
  public boolean getMoreResults() throws SQLException {
    return false;
  }

  @Override
  public void setFetchDirection(int direction) throws SQLException {

  }

  @Override
  public int getFetchDirection() throws SQLException {
    return 0;
  }

  @Override
  public void setFetchSize(int rows) throws SQLException {
    this.fetchSize = rows;
  }

  @Override
  public int getFetchSize() throws SQLException {
    return this.fetchSize;
  }

  @Override
  public int getResultSetConcurrency() throws SQLException {
    return 0;
  }

  @Override
  public int getResultSetType() throws SQLException {
    return 0;
  }

  @Override
  public void addBatch(String sql) throws SQLException {

  }

  @Override
  public void clearBatch() throws SQLException {

  }

  @Override
  public int[] executeBatch() throws SQLException {
    return new int[0];
  }

  @Override
  public Connection getConnection() throws SQLException {
    return this.conn;
  }

  @Override
  public boolean getMoreResults(int current) throws SQLException {
    return false;
  }

  @Override
  public ResultSet getGeneratedKeys() throws SQLException {
    return null;
  }

  @Override
  public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
    return 0;
  }

  @Override
  public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
    return 0;
  }

  @Override
  public int executeUpdate(String sql, String[] columnNames) throws SQLException {
    return 0;
  }

  @Override
  public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
    return false;
  }

  @Override
  public boolean execute(String sql, int[] columnIndexes) throws SQLException {
    return false;
  }

  @Override
  public boolean execute(String sql, String[] columnNames) throws SQLException {
    return false;
  }

  @Override
  public int getResultSetHoldability() throws SQLException {
    return 0;
  }

  @Override
  public boolean isClosed() throws SQLException {
    return false;
  }

  @Override
  public void setPoolable(boolean poolable) throws SQLException {

  }

  @Override
  public boolean isPoolable() throws SQLException {
    return false;
  }

  @Override
  public void closeOnCompletion() throws SQLException {

  }

  @Override
  public boolean isCloseOnCompletion() throws SQLException {
    return false;
  }

  @Override
  public <T> T unwrap(Class<T> iface) throws SQLException {
    return null;
  }

  @Override
  public boolean isWrapperFor(Class<?> iface) throws SQLException {
    return false;
  }
}
