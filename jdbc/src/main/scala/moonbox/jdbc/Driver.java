package moonbox.jdbc;

import moonbox.util.MoonboxJDBCUtils;

import java.sql.*;
import java.util.Properties;
import java.util.logging.Logger;

public class Driver implements java.sql.Driver {
    static {
        try {
            DriverManager.registerDriver(new Driver());
        } catch (SQLException e) {
            throw new RuntimeException("Can't register Driver!");
        }
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

    @Override
    public Connection connect(String url, Properties info) throws SQLException {
        return new MoonboxConnection(url, info);
    }

    @Override
    public boolean acceptsURL(String url) throws SQLException {
        if (url == null)
            return false;
        return MoonboxJDBCUtils.parseURL(url, null) != null;
    }

    @Override
    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) {
        return null;
    }
}
