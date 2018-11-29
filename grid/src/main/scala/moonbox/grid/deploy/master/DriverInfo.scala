package moonbox.grid.deploy.master

import java.util.Date

import moonbox.grid.deploy.ClusterDriverDescription


private[deploy] class DriverInfo(
	val startTime: Long,
	val id: String,
	val desc: ClusterDriverDescription,
	val submitDate: Date) extends Serializable {

	@transient var state: DriverState.Value = DriverState.WAITING

	@transient var exception: Option[Exception] = None

	@transient var worker: Option[WorkerInfo] = None

	@transient var yarnAppId: Option[String] = None

	init()

	private def init(): Unit = {
		state = DriverState.WAITING
		worker = None
		exception = None
		yarnAppId = None
	}
}
