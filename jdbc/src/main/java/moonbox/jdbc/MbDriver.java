package moonbox.jdbc;

import java.sql.*;
import java.util.Properties;
import java.util.logging.Logger;

public class MbDriver implements Driver {

  public static final String OS = getOSName();
  public static final String PLATFORM = getPlatform();
  public static final String LICENSE = "APACHE 2";
  public static final String VERSION = "0.4.0";
  public static final String NAME = "Moonbox Connector Java";
  public static final String URL_PREFIX = "jdbc:moonbox://";

  static {
    try {
      DriverManager.registerDriver(new MbDriver());
    } catch (SQLException e) {
      throw new RuntimeException("Can't register MbDriver!");
    }
  }

  @Override
  public Connection connect(String url, Properties info) throws SQLException {
    if (!acceptsURL(url)) {
      return null;
    }
    return new MoonboxConnection(url, info);
  }

  @Override
  public boolean acceptsURL(String url) throws SQLException {
    return url != null && url.toLowerCase().startsWith(URL_PREFIX);
  }

  @Override
  public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
    return new DriverPropertyInfo[0];
  }

  @Override
  public int getMajorVersion() {
    return 0;
  }

  @Override
  public int getMinorVersion() {
    return 0;
  }

  @Override
  public boolean jdbcCompliant() {
    return false;
  }

  @Override
  public Logger getParentLogger() throws SQLFeatureNotSupportedException {
    return null;
  }

  public static String getOSName() {
    return System.getProperty("os.name");
  }

  public static String getPlatform() {
    return System.getProperty("os.arch");
  }
}
