package moonbox.catalyst.jdbc;

import java.sql.*;
import java.util.Properties;
import java.util.logging.Logger;

public class CatalystDriver implements Driver  {

  public Connection connect(String url, Properties info) throws SQLException {
    return null;
  }

  public boolean acceptsURL(String url) throws SQLException {
    return false;
  }

  public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
    return new DriverPropertyInfo[0];
  }

  public int getMajorVersion() {
    return 0;
  }

  public int getMinorVersion() {
    return 0;
  }

  public boolean jdbcCompliant() {
    return false;
  }

  public Logger getParentLogger() throws SQLFeatureNotSupportedException {
    return null;
  }
}
