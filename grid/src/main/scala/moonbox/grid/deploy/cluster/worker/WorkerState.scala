package moonbox.grid.deploy.cluster.worker

object WorkerState extends Enumeration {
	type WorkerState = Value
	val ALIVE, DEAD, UNKNOWN = Value
}
