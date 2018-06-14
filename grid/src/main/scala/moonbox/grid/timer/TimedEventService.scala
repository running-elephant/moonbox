package moonbox.grid.timer

trait TimedEventService {
	def start(): Unit
	def stop(): Unit
	def clear(): Unit
	def addTimedEvent(event: EventEntity): Unit
	def deleteTimedEvent(group: String, name: String): Unit
	def pauseTimedEvent(group: String, name: String): Unit
	def resumeTimedEvent(group: String, name: String): Unit
	def getTimedEvent(group: String, name: String): EventRuntime
	def getTimedEvents(group: String): Seq[EventRuntime]
	def timedEventExists(group: String, name: String): Boolean
}
