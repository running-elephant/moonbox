package moonbox.grid.deploy.master

private[deploy] object DriverState extends Enumeration {
	type DriverState = Value
	val WAITING, SUBMITTING, CONNECTED, SUBMITTED, RUNNING, FINISHED, UNKNOWN, KILLED, FAILED, ERROR = Value
}
