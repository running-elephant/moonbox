package moonbox.grid.scheduler

import moonbox.common.util.ListenerEvent

trait MbListenerInterface {

	def onJobSubmitted(jobSubmitted: JobSubmitted): Unit

	def onJobAccepted(jobAccepted: JobAccepted): Unit

	def onJobReject(jobReject: JobReject): Unit

	def onJobStart(jobStart: JobStart): Unit

	def onJobEnd(jobEnd: JobEnd): Unit

	def onJobGettingResult(jobGettingResult: JobGettingResult): Unit

	def onOtherEvent(event: ListenerEvent): Unit
}
