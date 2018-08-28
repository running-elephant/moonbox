package moonbox.jdbc.interpreter

import com.typesafe.config.ConfigFactory

object ConfigSingleton {
  lazy val config = ConfigFactory.load(this.getClass.getClassLoader, "pool-config.conf")
  def getConfig = config
}
