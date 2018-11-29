package moonbox.grid.deploy.cluster.master

private[deploy] object DriverState extends Enumeration {
	type DriverState = Value
	val WAITING, SUBMITTING, SUBMITTED, RUNNING, FINISHED, UNKNOWN, KILLED, FAILED, ERROR = Value
}
