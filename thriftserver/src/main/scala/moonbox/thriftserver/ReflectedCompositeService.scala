package moonbox.thriftserver

import org.apache.commons.logging.Log
import org.apache.hadoop.hive.conf.HiveConf
import org.apache.hive.service.{AbstractService, Service}
import java.util.{List => JList}

import org.apache.hive.service.Service.STATE
import ReflectionUtils._
import scala.collection.JavaConverters._

trait ReflectedCompositeService {
  this: AbstractService =>
  def initCompositeService(hiveConf: HiveConf) {
    // Emulating `CompositeService.init(hiveConf)`
    val serviceList = getAncestorField[JList[Service]](this, 2, "serviceList")
    serviceList.asScala.foreach(_.init(hiveConf))

    // Emulating `AbstractService.init(hiveConf)`
    invoke(classOf[AbstractService], this, "ensureCurrentState", classOf[STATE] -> STATE.NOTINITED)
    setAncestorField(this, 3, "hiveConf", hiveConf)
    invoke(classOf[AbstractService], this, "changeState", classOf[STATE] -> STATE.INITED)
    getAncestorField[Log](this, 3, "LOG").info(s"Service: $getName is inited.")
  }
}
