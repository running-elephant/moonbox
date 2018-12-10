package moonbox.grid.deploy.master

private[deploy] object ApplicationState extends Enumeration {
	type ApplicationState = Value
	val RUNNING, FAILED, KILLED, DEAD, UNKNOWN = Value
}

