/*-
 * <<
 * Moonbox
 * ==
 * Copyright (C) 2016 - 2019 EDP
 * ==
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * >>
 */

package moonbox.grid.timer

import java.util.{Locale, Properties}

import com.cronutils.descriptor.CronDescriptor
import com.cronutils.model.CronType
import com.cronutils.model.definition.CronDefinitionBuilder
import com.cronutils.parser.CronParser
import moonbox.common.{MbConf, MbLogging}
import moonbox.grid.timer.EventState.EventState
import org.quartz.Trigger.TriggerState
import org.quartz._
import org.quartz.impl.StdSchedulerFactory
import org.quartz.impl.matchers.GroupMatcher

import scala.collection.JavaConversions._
import moonbox.grid.config._

class TimedEventServiceImpl(conf: MbConf, handle: EventHandler) extends TimedEventService with MbLogging {

	private val timedScheduler = {
		val props = new Properties()
		(TIMER_SERVICE_QUARTZ_DEFAULT_CONFIG ++ conf.getAll.filterKeys(key => key.startsWith("moonbox.deploy.timer.")))
		.foreach {
			case (key, value) => props.put(key.stripPrefix("moonbox.deploy.timer."), value)
		}
		new StdSchedulerFactory(props).getScheduler
	}

	private val descriptor = CronDescriptor.instance(Locale.ROOT) //Locale.CHINA
	private val parser = new CronParser(CronDefinitionBuilder.instanceDefinitionFor(CronType.QUARTZ))


	private def describe(cronExpr: String): String = {
		descriptor.describe(parser.parse(cronExpr))
	}

	override def start(): Unit = {
		if (!timedScheduler.isStarted) {
			timedScheduler.start()
			logInfo("Timer is started.")
		}
	}
	override def stop(): Unit = {
		if (!timedScheduler.isShutdown) {
			timedScheduler.shutdown()
			logInfo("Timer is stopped.")
		}
	}

	override def addTimedEvent(event: EventEntity): Unit = {
		val jobDataMap = new JobDataMap()
		jobDataMap.put(EventEntity.DEFINER_ORG, event.org)
		jobDataMap.put(EventEntity.DEFINER_NAME, event.definer)
		jobDataMap.put(EventEntity.NAME, event.name)
		jobDataMap.put(EventEntity.LANG, event.lang)
		jobDataMap.put(EventEntity.SQLS, event.sqls)
		jobDataMap.put(EventEntity.HANDLER, handle)
		jobDataMap.put(EventEntity.CONFIG, event.config)
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

	override def getTimedEvents(): Seq[EventRuntime] = {
		timedScheduler.getTriggerKeys(GroupMatcher.anyGroup()).map { triggerKey =>
			getTimedEvent(triggerKey.getGroup, triggerKey.getName)
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
