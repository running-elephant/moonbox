package moonbox.grid.deploy.timer

import java.util.Date
import org.quartz._

class EventJob extends Job{
    import EventJob._
    override def execute(context: JobExecutionContext) = {
        val jobKey = context.getJobDetail.getKey
        val triggerKey = context.getTrigger.getKey
        //println("EventJob says: " + jobKey + " executing at " + new Date)

        val dataMap: JobDataMap = context.getMergedJobDataMap
        val sql: Seq[String] = dataMap.get(registeredSql).asInstanceOf[Seq[String]]
        val fun = dataMap.get(registeredFunction).asInstanceOf[(String, String, String, String, Seq[String]) => Unit]
        fun(triggerKey.getName, triggerKey.getGroup, jobKey.getName, jobKey.getGroup, sql)
        println()
    }
}

object EventJob {
    val registeredSql = "regSql"
    val registeredFunction = "regFun"
}
