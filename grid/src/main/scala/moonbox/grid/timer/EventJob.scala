package moonbox.grid.timer

import moonbox.common.MbLogging
import org.quartz.{Job, JobExecutionContext}

class EventJob extends Job with MbLogging {
	override def execute(ctx: JobExecutionContext): Unit = {
		val dataMap = ctx.getMergedJobDataMap
		val definer = dataMap.getString(EventEntity.DEFINER)
		val sqls = dataMap.get(EventEntity.SQLS).asInstanceOf[Seq[String]]
		val func = dataMap.get(EventEntity.FUNC).asInstanceOf[() => Unit]
		logInfo(s"""Timed event fire as user '$definer' run sqls (${sqls.mkString(", ")})""")
		//MbMaster.singleton ! JobSubmit(definer, sqls, async = true)
		func()
	}
}
