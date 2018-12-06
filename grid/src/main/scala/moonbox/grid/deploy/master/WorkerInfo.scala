package moonbox.grid.deploy.master

import akka.actor.{ActorRef, Address}
import moonbox.grid.deploy.worker.WorkerState

import scala.collection.mutable


class WorkerInfo(
	val id: String,
	val host: String,
	val port: Int,
	val address: Address,
	val endpoint: ActorRef
) extends Serializable {

	@transient var state: WorkerState.Value = _
	@transient var drivers: mutable.HashMap[String, DriverInfo] = _
	@transient var lastHeartbeat: Long = _

	init()

	def setState(state: WorkerState.Value): Unit = {
		this.state = state
	}

	def isAlive: Boolean = {
		this.state == WorkerState.ALIVE
	}

	private def init(): Unit = {
		state = WorkerState.ALIVE
		drivers = new mutable.HashMap()
		lastHeartbeat = System.currentTimeMillis()
	}

	def addDriver(driver: DriverInfo): Unit = {
		drivers(driver.id) = driver
	}

	def removeDriver(driver: DriverInfo): Unit = {
		drivers -= driver.id
	}

	override def toString: String = {
		s"""id: $id
		   |host: $host
		   |port: $port
		   |address: $address
		   |endpoint: $endpoint
		   |state: $state
		 """.stripMargin
	}
}
