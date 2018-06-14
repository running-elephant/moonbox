package moonbox.grid.timer

import java.util.Date

import moonbox.grid.timer.EventState.EventState

case class EventRuntime (
	group: String,
	name: String,
	cronDescription: String,
	startTime: Option[Date],
	preFireTime: Option[Date],
	nextFireTime: Option[Date],
	endTime: Option[Date],
	status: EventState)
