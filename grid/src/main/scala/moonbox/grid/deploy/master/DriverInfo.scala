package moonbox.grid.deploy.master

import java.util.Date

import moonbox.grid.deploy.DriverDescription


private[deploy] class DriverInfo(
	val startTime: Long,
	val id: String,
	val desc: DriverDescription,
	val submitDate: Date) extends Serializable {

	@transient var state: DriverState.Value = DriverState.WAITING

	@transient var exception: Option[Exception] = None

	@transient var worker: Option[WorkerInfo] = None

	@transient var appId: Option[String] = None

	init()

	private def init(): Unit = {
		state = DriverState.WAITING
		worker = None
		exception = None
		appId = None
	}

	private def readObject(in: java.io.ObjectInputStream): Unit = {
		in.defaultReadObject()
		init()
	}

	override def toString: String = {
		s"""startTime: $startTime
		   |id: $id
		   |desc: $desc
		   |submitDate: $submitDate
		   |state: $state
		   |appId: $appId
		 """.stripMargin
	}
}
