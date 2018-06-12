package moonbox.grid.timer

import org.quartz.{Job, JobExecutionContext, JobKey, TriggerKey}

class EventJob extends Job {
	override def execute(ctx: JobExecutionContext): Unit = {
		println(ctx.getMergedJobDataMap.get(EventEntity.EVENT_KEY))
	}
}
