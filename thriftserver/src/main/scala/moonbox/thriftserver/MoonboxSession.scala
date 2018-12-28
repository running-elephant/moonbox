package moonbox.thriftserver

import java.util

import moonbox.thriftserver.ReflectionUtils._
import org.apache.hadoop.hive.conf.HiveConf
import org.apache.hadoop.hive.ql.session.SessionState
import org.apache.hive.service.cli.session.HiveSessionImpl
import org.apache.hive.service.cli.thrift.TProtocolVersion
class MoonboxSession(protocol: TProtocolVersion,
                     username: String,
                     password: String,
                     hiveConf: HiveConf,
                     ipAddress: String
                     ) extends HiveSessionImpl(protocol, username, password, hiveConf,ipAddress) {

  override def open(sessionConfMap: util.Map[String, String]) = {
    val sessionState = new SessionState(hiveConf, username)
    sessionState.setUserIpAddress(ipAddress)
    sessionState.setIsHiveServerQuery(true)
    val lastAccessTime: Long = System.currentTimeMillis
    setSuperField(this, "lastIdleTime", lastAccessTime.asInstanceOf[AnyRef])
    setSuperField(this, "sessionState", sessionState)
  }
}
