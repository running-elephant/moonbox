package moonbox.jdbc.interpreter

import java.sql.Connection
import java.util.Properties

import com.zaxxer.hikari.{HikariConfig, HikariDataSource}

class ConnectionPoolImpl(override val property: Properties) extends ConnectionPool {
  import PoolPropsKey._

  val pool: HikariDataSource = initPool

  private def initPool: HikariDataSource = {
    val CPFileConfig = ConfigSingleton.getConfig.getConfig("pool")

    val CPConfig = new HikariConfig()
    CPConfig.setPoolName("HikariCP")
    CPConfig.setMaximumPoolSize(CPFileConfig.getInt("maximumPoolSize"))
    CPConfig.setMinimumIdle(CPFileConfig.getInt("minimumIdle"))
    CPConfig.setIdleTimeout(CPFileConfig.getInt("idleTimeout"))
    CPConfig.setMaxLifetime(CPFileConfig.getInt("maxLifetime"))
    CPConfig.setDriverClassName(property.getProperty(DRIVER_KEY))
    CPConfig.setJdbcUrl(property.getProperty(URL_KEY))
    CPConfig.setUsername(property.getProperty(USER_KEY))
    CPConfig.setPassword(property.getProperty(PASSWORD_KEY))

    new HikariDataSource(CPConfig)
  }

  def getConnection(): Connection = {
    pool.getConnection
  }

}

object PoolPropsKey {
  val URL_KEY = "url"
  val USER_KEY = "user"
  val PASSWORD_KEY = "password"
  val DRIVER_KEY = "driver"
}
