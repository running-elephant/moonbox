package moonbox.grid.deploy.audit


import java.util.concurrent.{ConcurrentLinkedQueue, ScheduledThreadPoolExecutor, TimeUnit}

import moonbox.common.MbConf
import moonbox.grid.deploy.ConnectionInfo
import scala.collection.mutable.ArrayBuffer


trait AuditLogger {

	def log(user: String, action: String, param: Map[String, String])(implicit connectionInfo: ConnectionInfo): Unit

	def log(user: Option[String], action: String, param: Map[String, String])(implicit connectionInfo: ConnectionInfo): Unit

	def log(user: Option[String], action: String)(implicit connectionInfo: ConnectionInfo): Unit

	def log(user: String, action: String)(implicit connectionInfo: ConnectionInfo): Unit

}
abstract class AbstractAuditLogger(conf: MbConf) extends AuditLogger {
	private val eventQueue = new ConcurrentLinkedQueue[AuditEvent]()
	private val flushScheduler = {
		val executor = new ScheduledThreadPoolExecutor(1)
		executor.setRemoveOnCancelPolicy(true)
		executor
	}

	init()

	flushScheduler.scheduleAtFixedRate(new Runnable {
		override def run(): Unit = {
			val buffer = new ArrayBuffer[AuditEvent]()
			while (!eventQueue.isEmpty) {
				buffer.append(eventQueue.poll())
			}
			eventQueue.clear()
			persist(buffer)
		}
	}, 0, 30, TimeUnit.SECONDS)

	override def log(user: String, action: String, param: Map[String, String])(implicit connectionInfo: ConnectionInfo): Unit = {
		log(AuditEvent(user, action, param, connectionInfo))
	}

	override def log(user: Option[String], action: String, param: Map[String, String])(implicit connectionInfo: ConnectionInfo): Unit = {
		log(user.getOrElse("unknown"), action, param)
	}

	override def log(user: Option[String], action: String)(implicit connectionInfo: ConnectionInfo): Unit = {
		log(user.getOrElse("unknown"), action)
	}

	override def log(user: String, action: String)(implicit connectionInfo: ConnectionInfo): Unit = {
		log(user, action, Map.empty[String, String])
	}

	private def log(event: AuditEvent)(implicit connectionInfo: ConnectionInfo): Unit = {
		eventQueue.add(event)
	}

	protected def init(): Unit

	protected def persist(events: Seq[AuditEvent]): Unit

	def close(): Unit
}
