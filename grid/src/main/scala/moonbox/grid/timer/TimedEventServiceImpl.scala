package moonbox.grid.timer

import java.util.{Locale, Properties}

import com.cronutils.descriptor.CronDescriptor
import com.cronutils.model.CronType
import com.cronutils.model.definition.CronDefinitionBuilder
import com.cronutils.parser.CronParser
import moonbox.common.MbConf
import moonbox.grid.timer.EventState.EventState
import org.quartz.Trigger.TriggerState
import org.quartz._
import org.quartz.impl.StdSchedulerFactory
import org.quartz.impl.matchers.GroupMatcher

import scala.collection.JavaConversions._

object TimedEventServiceImpl {

}

class TimedEventServiceImpl(conf: MbConf) extends TimedEventService {

	private val timedScheduler = {
		val props = new Properties()
		conf.getAll.filterKeys(key => key.startsWith("moonbox.timedEvent.")).foreach {
			case (key, value) => props.put(key.stripPrefix("moonbox.timedEvent."), value)
		}
		new StdSchedulerFactory(props).getScheduler
	}

	private val descriptor = CronDescriptor.instance(Locale.US); //Locale.CHINA
	private val parser = new CronParser(CronDefinitionBuilder.instanceDefinitionFor(CronType.QUARTZ))


	private def describe(cronExpr: String): String = {
		descriptor.describe(parser.parse(cronExpr))
	}

	override def start(): Unit = {
		if (!timedScheduler.isStarted) {
			timedScheduler.start()
		}
	}
	override def stop(): Unit = {
		if (!timedScheduler.isShutdown) {
			timedScheduler.shutdown()
		}
	}

	override def addTimedEvent(event: EventEntity): Unit = {
		val jobDataMap = new JobDataMap()
		jobDataMap.put(EventEntity.EVENT_KEY, event)
		val jobBuilder = JobBuilder.newJob(classOf[EventJob])
		if (event.desc.isDefined) {
			jobBuilder.withDescription(event.desc.get)
		}
		val jobDetail = jobBuilder.withIdentity(event.name, event.group).usingJobData(jobDataMap)
			.storeDurably().withDescription(describe(event.cronExpr))
			.build()
		val scheduler = CronScheduleBuilder.cronSchedule(event.cronExpr).withMisfireHandlingInstructionDoNothing()
		val triggerBuilder = TriggerBuilder.newTrigger().withIdentity(event.name, event.group)
		if (event.start.isDefined) { triggerBuilder.startAt(event.start.get) }
		if (event.end.isDefined) { triggerBuilder.endAt(event.end.get) }
		val trigger = triggerBuilder.withSchedule(scheduler).build()

		val jobKey = JobKey.jobKey(event.name, event.group)
		val jobExists = timedScheduler.checkExists(jobKey)
		val triggerKey = TriggerKey.triggerKey(event.name, event.group)
		val triggerExists = timedScheduler.checkExists(triggerKey)
		if (jobExists) {
			timedScheduler.addJob(jobDetail, true)
		}
		if (triggerExists) {
			timedScheduler.rescheduleJob(triggerKey, trigger)
		} else {
			timedScheduler.scheduleJob(jobDetail, trigger)
		}
	}

	override def deleteTimedEvent(group: String, name: String): Unit = {
		timedScheduler.deleteJob(JobKey.jobKey(name, group))
	}

	override def timedEventExists(group: String, name: String): Boolean = {
		val jobExists = timedScheduler.checkExists(JobKey.jobKey(name, group))
		val triggerExists = timedScheduler.checkExists(TriggerKey.triggerKey(name, group))
		jobExists && triggerExists
	}

	override def getTimedEvent(group: String, name: String): EventRuntime = {
		val triggerKey = TriggerKey.triggerKey(name, group)
		val eventState = triggerStateConvert(timedScheduler.getTriggerState(triggerKey))
		val cronDescription = timedScheduler.getJobDetail(JobKey.jobKey(name, group)).getDescription
		timedScheduler.getTrigger(triggerKey) match {
			case cron: CronTrigger =>
				EventRuntime(
					group = group,
					name = name,
					cronDescription = cronDescription,
					startTime = Option(cron.getStartTime),
					preFireTime = Option(cron.getPreviousFireTime),
					nextFireTime = Option(cron.getNextFireTime),
					endTime = Option(cron.getEndTime),
					status = eventState
				)
			case u => throw new Exception(s"Unsupported trigger type ${u.getClass.getSimpleName}")
		}
	}

	override def getTimedEvents(group: String): Seq[EventRuntime] = {
		timedScheduler.getTriggerKeys(GroupMatcher.groupEquals(group)).map { triggerKey =>
			getTimedEvent(group, triggerKey.getName)
		}.toSeq
	}

	private def triggerStateConvert(state: TriggerState): EventState = {
		state match {
			case TriggerState.BLOCKED => EventState.BLOCKED
			case TriggerState.COMPLETE => EventState.COMPLETE
			case TriggerState.ERROR => EventState.ERROR
			case TriggerState.NONE => EventState.NONE
			case TriggerState.NORMAL => EventState.NORMAL
			case TriggerState.PAUSED => EventState.PAUSED
		}
	}

	override def resumeTimedEvent(group: String, name: String): Unit = {
		timedScheduler.resumeTrigger(TriggerKey.triggerKey(name, group))
	}

	override def pauseTimedEvent(group: String, name: String): Unit = {
		timedScheduler.pauseTrigger(TriggerKey.triggerKey(name, group))
	}

	override def clear(): Unit = {
		timedScheduler.clear()
	}
}
