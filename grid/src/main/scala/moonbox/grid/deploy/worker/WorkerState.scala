package moonbox.grid.deploy.worker

object WorkerState extends Enumeration {
	type WorkerState = Value
	val ALIVE, DEAD, UNKNOWN = Value
}
