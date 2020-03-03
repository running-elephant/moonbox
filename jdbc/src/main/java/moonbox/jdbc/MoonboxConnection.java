package moonbox.jdbc;

import moonbox.jdbc.util.Utils;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

public class MoonboxConnection implements Connection {
  private static final String DEFAULT_MASTER_HOST = "localhost";
  private static final int DEFAULT_MASTER_PORT = 10010;
  private static final int DEFAULT_CONNECT_TIMEOUT = 20 * 1000;
  private static final String DEFAULT_DATABASE = "default";
  private Properties info;
  private MoonboxClient client;
  private String masterHost;
  private int masterPort;
  private int connectTimeout;
  private String database;

  private DatabaseMetaData dbmd;

  public MoonboxConnection(String url, Properties info) throws SQLException {
    this.info = Utils.parseURL(url, info);
    this.database = this.info.getProperty("database", DEFAULT_DATABASE);
    this.masterHost = this.info.getProperty("host", DEFAULT_MASTER_HOST);
    this.masterPort = Integer.valueOf(this.info.getProperty("port", String.valueOf(DEFAULT_MASTER_PORT)));
    this.connectTimeout = Integer.valueOf(this.info.getProperty("connectTimeout", String.valueOf(DEFAULT_CONNECT_TIMEOUT)));
    this.client = createClient();
    this.dbmd = getMetaData(false);
  }

  private MoonboxClient createClient() throws SQLException {
    String user = this.info.getProperty("user");
    String password = this.info.getProperty("password");
    String appType = this.info.getProperty("apptype");
    String appName = this.info.getProperty("appname", null);
    Map<String, String> config = new HashMap<>();
    config.put("database", database);
    try {
      return new MoonboxClient(masterHost,
          masterPort, connectTimeout, user, password, appType, appName, config);
    } catch (Exception e) {
      throw new SQLException(e);
    }
  }

  MoonboxClient getClient() {
    return this.client;
  }

  int getConnectTimeout() {
    return this.connectTimeout;
  }

  @Override
  public Statement createStatement() throws SQLException {
    checkClosed();
    return new MoonboxStatement(this, this.database);
  }

  @Override
  public PreparedStatement prepareStatement(String sql) throws SQLException {
    checkClosed();
    return new MoonboxPrepareStatement();
  }

  @Override
  public CallableStatement prepareCall(String sql) throws SQLException {
    return null;
  }

  @Override
  public String nativeSQL(String sql) throws SQLException {
    return null;
  }

  @Override
  public void setAutoCommit(boolean autoCommit) throws SQLException {

  }

  @Override
  public boolean getAutoCommit() throws SQLException {
    return false;
  }

  @Override
  public void commit() throws SQLException {

  }

  @Override
  public void rollback() throws SQLException {

  }

  @Override
  public void close() throws SQLException {
    try {
      client.close();
    } catch (Exception e) {
      throw new SQLException(e);
    }
  }

  @Override
  public boolean isClosed() throws SQLException {
    return client.isClose();
  }

  @Override
  public DatabaseMetaData getMetaData() throws SQLException {
    return getMetaData(true);
  }

  private DatabaseMetaData getMetaData(boolean checkClosed) throws SQLException {
    if (checkClosed) {
      checkClosed();
    }
    return new MoonboxDatabaseMetaData(this, this.database);
  }

  public void checkClosed() throws SQLException {
    if (this.client.isClose()) {
      throw new SQLException("No operations allowed after connection closed.");
    }
  }

  @Override
  public void setReadOnly(boolean readOnly) throws SQLException {

  }

  @Override
  public boolean isReadOnly() throws SQLException {
    return true;
  }

  @Override
  public void setCatalog(String catalog) throws SQLException {
    createStatement().executeQuery("USE " + catalog);
    this.database = catalog;
  }

  @Override
  public String getCatalog() throws SQLException {
    return this.database;
  }

  @Override
  public void setTransactionIsolation(int level) throws SQLException {

  }

  @Override
  public int getTransactionIsolation() throws SQLException {
    return 0;
  }

  @Override
  public SQLWarning getWarnings() throws SQLException {
    return null;
  }

  @Override
  public void clearWarnings() throws SQLException {

  }

  @Override
  public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
    return createStatement();
  }

  @Override
  public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
    return prepareStatement(sql);
  }

  @Override
  public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
    return null;
  }

  @Override
  public Map<String, Class<?>> getTypeMap() throws SQLException {
    return null;
  }

  @Override
  public void setTypeMap(Map<String, Class<?>> map) throws SQLException {

  }

  @Override
  public void setHoldability(int holdability) throws SQLException {

  }

  @Override
  public int getHoldability() throws SQLException {
    return 0;
  }

  @Override
  public Savepoint setSavepoint() throws SQLException {
    return null;
  }

  @Override
  public Savepoint setSavepoint(String name) throws SQLException {
    return null;
  }

  @Override
  public void rollback(Savepoint savepoint) throws SQLException {

  }

  @Override
  public void releaseSavepoint(Savepoint savepoint) throws SQLException {

  }

  @Override
  public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
    return createStatement();
  }

  @Override
  public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
    return prepareStatement(sql);
  }

  @Override
  public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
    return null;
  }

  @Override
  public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
    return prepareStatement(sql);
  }

  @Override
  public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
    return prepareStatement(sql);
  }

  @Override
  public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
    return prepareStatement(sql);
  }

  @Override
  public Clob createClob() throws SQLException {
    return null;
  }

  @Override
  public Blob createBlob() throws SQLException {
    return null;
  }

  @Override
  public NClob createNClob() throws SQLException {
    return null;
  }

  @Override
  public SQLXML createSQLXML() throws SQLException {
    return null;
  }

  @Override
  public boolean isValid(int timeout) throws SQLException {
    return false;
  }

  @Override
  public void setClientInfo(String name, String value) throws SQLClientInfoException {

  }

  @Override
  public void setClientInfo(Properties properties) throws SQLClientInfoException {

  }

  @Override
  public String getClientInfo(String name) throws SQLException {
    return this.info.getProperty(name);
  }

  @Override
  public Properties getClientInfo() throws SQLException {
    return this.info;
  }

  @Override
  public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
    return null;
  }

  @Override
  public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
    return null;
  }

  @Override
  public void setSchema(String schema) throws SQLException {
    setCatalog(schema);
  }

  @Override
  public String getSchema() throws SQLException {
    return getCatalog();
  }

  @Override
  public void abort(Executor executor) throws SQLException {

  }

  @Override
  public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {

  }

  @Override
  public int getNetworkTimeout() throws SQLException {
    return 0;
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
