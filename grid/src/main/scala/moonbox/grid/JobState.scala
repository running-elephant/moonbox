package moonbox.grid

object JobState extends Enumeration {
	val WAITING, RUNNING, KILLED, FAILED, LOST, SUCCESS = Value
	type JobState = Value

	def isFinished(state: JobState): Boolean = Seq(KILLED, FAILED, LOST, SUCCESS).contains(state)
}
