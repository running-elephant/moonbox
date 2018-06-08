package moonbox.jdbc

import java.util.{Properties, UUID}

import moonbox.client.JdbcClient

/**
  *
  * @param jdbcClient
  * @param user
  * @param pwd md5 value of the password
  */
case class JdbcSession(jdbcClient: JdbcClient,
                       database: String,
                       table: String,
                       user: String,
                       pwd: String, // md5 String of the original password
                       connectionProperties: Properties,
                       id: String = UUID.randomUUID().toString,
                       sessionStart: Long = System.currentTimeMillis
                      ) {
  var closed: Boolean = false

  def close(): Unit = {
    if (jdbcClient != null) {
      jdbcClient.close()
    }
    closed = true
  }

  def isClosed(): Boolean = {
    if (jdbcClient.isActive()) {
      closed = false
    } else {
      if (jdbcClient != null) {
        jdbcClient.close()
      }
      closed = true
    }
    closed
  }
}
