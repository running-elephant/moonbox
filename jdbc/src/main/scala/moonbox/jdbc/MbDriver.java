/*-
 * <<
 * Moonbox
 * ==
 * Copyright (C) 2016 - 2019 EDP
 * ==
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * >>
 *//*


package moonbox.jdbc;

import java.sql.*;
import java.util.Properties;
import java.util.logging.Logger;

public class MbDriver implements Driver {
    static {
        try {
            DriverManager.registerDriver(new MbDriver());
        } catch (SQLException e) {
            throw new RuntimeException("Can't register MbDriver!");
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
        if (!this.acceptsURL(url)) {
            return null;
        }

        MoonboxConnection conn = new MoonboxConnection(url, info);
        if (conn.init()) {
            return conn;
        } else {
            throw new SQLException("User check error");
        }
    }

    @Override
    public boolean acceptsURL(String url) throws SQLException {
        String urlPrefix = "jdbc:moonbox:";
        return url != null && url.toLowerCase().startsWith(urlPrefix);
    }

    @Override
    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) {
        return null;
    }
}
*/
