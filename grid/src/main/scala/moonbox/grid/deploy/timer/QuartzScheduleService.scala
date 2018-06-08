package moonbox.grid.deploy.timer

import java.util
import java.util.Properties

import org.quartz._
import org.quartz.impl.StdSchedulerFactory
import org.quartz.impl.matchers.GroupMatcher

import scala.collection.JavaConverters._


/*******************************************************************
  * create event
  *     if enable,  startEvent()
  *     if disable, nothing
  * update event
  *     if enable -> disable, deleteEvent()
  *     if disable -> enable, addEvent()
  *
  * prop: Properties:
  *(basic config part)
  *  "org.quartz.scheduler.instanceName",               "EventScheduler"
  *  "org.quartz.threadPool.threadCount",               "3"
  *  "org.quartz.scheduler.skipUpdateCheck",            "true"
  *  "org.quartz.jobStore.misfireThreshold",            "3000"   //当任务错过触发时间时，最忍受的最长延迟时间 3s
  *
  *(if use ram store, add this part)
  *  "org.quartz.jobStore.class",                       "org.quartz.simpl.RAMJobStore"
  *
  *(if use mysql jdbc store, use this part)
  *  "org.quartz.jobStore.class",                       "org.quartz.impl.jdbcjobstore.JobStoreTX"
  *  "org.quartz.jobStore.driverDelegateClass",         "org.quartz.impl.jdbcjobstore.StdJDBCDelegate"
  *  "org.quartz.jobStore.useProperties",               "false" //该值表示是否datamap中所有数据都使用properties模式，即字符串，默认是false,使用blob
  *  "org.quartz.jobStore.tablePrefix",                 "QRTZ_"
  *  "org.quartz.jobStore.dataSource",                  "quartzDataSource"
  *  "org.quartz.dataSource.quartzDataSource.driver",    classOf[com.mysql.jdbc.Driver].getName
  *  "org.quartz.dataSource.quartzDataSource.URL",      "jdbc:mysql://host:port/database"
  *  "org.quartz.dataSource.quartzDataSource.user",     "xxxx"
  *  "org.quartz.dataSource.quartzDataSource.password", "yyyy"
  *  "org.quartz.dataSource.quartzDataSource.maxConnections", "10"
  *
  ******************************************************************/

class QuartzScheduleService(prop: Properties) extends ScheduleService{
    import QuartzScheduleService._

    private val scheduler: Scheduler = {
        val schedulerFactory = new StdSchedulerFactory(prop)
        val sched = schedulerFactory.getScheduler
        sched.start()  // auto start
        sched
    }

    /****
      * EVENT       ---- ( Quartz Trigger)
      * APPLICATION ---- ( Quartz Job )
      */
    override def getAllApplications(): Seq[(String, String)] = {
        val matcher: GroupMatcher[JobKey] = GroupMatcher.anyJobGroup
        val jobKeySet: java.util.Set[JobKey] = scheduler.getJobKeys(matcher)

        jobKeySet.asScala.map{ jobKey => (jobKey.getName, jobKey.getGroup) }.toSeq
    }

    override def getApplicationsByGroup(appGroup: String): Seq[(String, String)] = {
        val matcher: GroupMatcher[JobKey] = GroupMatcher.groupEquals(appGroup)
        val jobKeySet: java.util.Set[JobKey] = scheduler.getJobKeys(matcher)

        jobKeySet.asScala.map{ jobKey => (jobKey.getName, jobKey.getGroup) }.toSeq
    }

    /***/
    override def updateApplication(appName: String, appGroup: String, sql: Seq[String], function: (String, String, String, String, Seq[String]) => Unit ): Unit = {
        import EventJob._
        val hashMap = new java.util.HashMap[String, AnyRef]()
        hashMap.put(registeredSql, sql)
        hashMap.put(registeredFunction, function)
        val jobData = new JobDataMap(hashMap)

        val jobDetail = JobBuilder.newJob(classOf[EventJob]) //create a new job
                .withIdentity(appName, appGroup)
                .storeDurably(true)  //store
                .usingJobData(jobData)
                .build()
        scheduler.addJob(jobDetail, true)

    }

    override def deleteApplication(appName: String, appGroup: String): Boolean = {
        val jobKey = JobKey.jobKey(appName, appGroup)
        scheduler.deleteJob(jobKey)
    }

    override def pauseApplication(appName: String, appGroup: String): Unit = {
        val jobKey = JobKey.jobKey(appName, appGroup)
        scheduler.pauseJob(jobKey)
    }

    override def resumeApplication(appName: String, appGroup: String): Unit ={
        val jobKey = JobKey.jobKey(appName, appGroup)
        scheduler.resumeJob(jobKey)
    }

    override def checkApplicationExists(appName: String, appGroup: String): Boolean = {
        val jobKey = JobKey.jobKey(appName, appGroup)
        scheduler.checkExists(jobKey)    //if query a existed event, return true
    }

    override def getAllEvents(): Seq[(String, String)] = {
        val matcher: GroupMatcher[TriggerKey] = GroupMatcher.anyTriggerGroup()
        val triggerKeySet: java.util.Set[TriggerKey] = scheduler.getTriggerKeys(matcher)

        triggerKeySet.asScala.map{ triggerKey => (triggerKey.getName, triggerKey.getGroup) }.toSeq
    }

    override def getPendingEvents(): Seq[ScheduledSummary] = {
        val jobs: util.List[JobExecutionContext] = scheduler.getCurrentlyExecutingJobs

        jobs.asScala.map{ executingJob =>
            val jobDetail: JobDetail = executingJob.getJobDetail
            val jobDataMap: JobDataMap = jobDetail.getJobDataMap
            val sqls: Seq[String] = jobDataMap.get("regSql").asInstanceOf[Seq[String]]
            val trigger: Trigger = executingJob.getTrigger
            val triggerKey: TriggerKey = trigger.getKey
            val triggerStatus: Trigger.TriggerState = scheduler.getTriggerState(triggerKey)


            trigger match {
                case cronTrigger: CronTrigger =>
                    ScheduledSummary(
                        ScheduledEntity(
                            triggerKey.getName,
                            triggerKey.getGroup,
                            jobDetail.getKey.getName,
                            jobDetail.getKey.getGroup,
                            sqls,
                            cronTrigger.getCronExpression),
                        ScheduledStatistics(
                            packData(trigger.getStartTime),
                            packData(trigger.getPreviousFireTime),
                            packData(trigger.getNextFireTime),
                            packData(trigger.getEndTime),
                            packStatus(triggerStatus))
                    )
            }
        }
    }

    override def getEventsByApplication(appName: String, appGroup: String): Seq[ScheduledSummary] ={
        import EventJob._
        val jobKey = JobKey.jobKey(appName, appGroup)
        val triggers: util.List[_ <: Trigger] = scheduler.getTriggersOfJob(jobKey)

        triggers.asScala.map{case cronTrigger: CronTrigger =>
            val triggerKey = cronTrigger.getKey
            val triggerStatus: Trigger.TriggerState = scheduler.getTriggerState(triggerKey)
            val jobDataMap: JobDataMap = scheduler.getJobDetail(jobKey).getJobDataMap
            val sql: Seq[String] = jobDataMap.get(registeredSql).asInstanceOf[Seq[String]]
            ScheduledSummary(
                ScheduledEntity(
                    triggerKey.getName,
                    triggerKey.getGroup,
                    jobKey.getName,
                    jobKey.getGroup,
                    sql,
                    cronTrigger.getCronExpression),
                ScheduledStatistics(
                    packData(cronTrigger.getStartTime),
                    packData(cronTrigger.getPreviousFireTime),
                    packData(cronTrigger.getNextFireTime),
                    packData(cronTrigger.getEndTime),
                    packStatus(triggerStatus))
            )
        }
    }

    override def checkEventExists(eventGroup: String, eventName: String): Boolean = {
        val triggerKey: TriggerKey = TriggerKey.triggerKey(eventName, eventGroup)
        scheduler.checkExists(triggerKey)  //if query a existed event, return true
    }

    override def getEventsByGroup(eventGroup: String): Seq[(String, String)] = {
        val matcher: GroupMatcher[TriggerKey] = GroupMatcher.groupEquals(eventGroup)
        val triggerKeySet: java.util.Set[TriggerKey] = scheduler.getTriggerKeys(matcher)

        triggerKeySet.asScala.map{ triggerKey => (triggerKey.getName, triggerKey.getGroup) }.toSeq
    }

    override def getEventByName(eventGroup: String, eventName: String): Option[ScheduledSummary] = {  //TODO: job key may empty
        val triggerKey: TriggerKey = TriggerKey.triggerKey(eventName, eventGroup)
        val trigger: Trigger = scheduler.getTrigger(triggerKey)
        if(trigger == null) {
            None
        }else {
            import EventJob._
            val jobKey: JobKey = trigger.getJobKey
            val triggerStatus: Trigger.TriggerState = scheduler.getTriggerState(triggerKey)
            val jobDataMap: JobDataMap = scheduler.getJobDetail(jobKey).getJobDataMap
            val sqls: Seq[String] = jobDataMap.get(registeredSql).asInstanceOf[Seq[String]]

            trigger match {
                case cronTrigger: CronTrigger =>
                    Option(ScheduledSummary(
                        ScheduledEntity(
                            triggerKey.getName,
                            triggerKey.getGroup,
                            jobKey.getName,
                            jobKey.getGroup,
                            sqls,
                            cronTrigger.getCronExpression),
                        ScheduledStatistics(
                            packData(cronTrigger.getStartTime),
                            packData(cronTrigger.getPreviousFireTime),
                            packData(cronTrigger.getNextFireTime),
                            packData(cronTrigger.getEndTime),
                            packStatus(triggerStatus))
                    ))
            }
        }
    }


    private def buildTrigger(entity: ScheduledEntity): Trigger = {
        var triggerBuilder: TriggerBuilder[Trigger] = TriggerBuilder.newTrigger.withIdentity(entity.eventName, entity.eventGroup)
        if(entity.startAt.isDefined) {
            triggerBuilder = triggerBuilder.startAt(entity.startAt.get)
        }
        if(entity.endAt.isDefined){
            triggerBuilder = triggerBuilder.endAt(entity.endAt.get)
        }

        val scheduleBuilder = CronScheduleBuilder.cronSchedule(entity.cronExpression)
                                                 .withMisfireHandlingInstructionDoNothing()  //所有的misfire不管，执行下一个周期的任务

        val trigger: CronTrigger = triggerBuilder.forJob(entity.appName, entity.appGroup)
                                                 .withSchedule(scheduleBuilder)
                                                 .build()
        trigger
    }

    /** f(eventGroup, eventName, appGroup, appName) **/
    override def startEvent(entity: ScheduledEntity, function: (String, String, String, String, Seq[String]) => Unit ): Unit = {
        val trigger: Trigger = buildTrigger(entity)  //register fun to trigger

        if( !scheduler.checkExists(JobKey.jobKey(entity.appName, entity.appGroup))){ //if job not exist, create it
            import EventJob._
            val hashMap = new java.util.HashMap[String, AnyRef]()
            hashMap.put(registeredSql, entity.sql)
            hashMap.put(registeredFunction, function)
            val jobData = new JobDataMap(hashMap)

            val jobDetail = JobBuilder.newJob(classOf[EventJob]) //SimpleJob
                    .withIdentity(entity.appName, entity.appGroup)
                    .storeDurably(true)  //store
                    .usingJobData(jobData)
                    .build()
            scheduler.addJob(jobDetail, true)
        }

        if(scheduler.checkExists(trigger.getKey)){  //if trigger exist, reschedule it
            scheduler.rescheduleJob(trigger.getKey, trigger)
        }else {
            scheduler.scheduleJob(trigger)
        }
    }

    override def updateEvent(entity: ScheduledEntity): Unit = {
        val triggerKey = TriggerKey.triggerKey(entity.eventName, entity.eventGroup)
        val newTrigger: Trigger = buildTrigger(entity)

        scheduler.rescheduleJob(triggerKey, newTrigger)
    }


    override def deleteEvent(eventName: String, eventGroup: String): Boolean = {
        val triggerKey = TriggerKey.triggerKey(eventName, eventGroup)
        scheduler.unscheduleJob(triggerKey)
    }

    override def pauseEvent(eventName: String, eventGroup: String): Unit = {
        val triggerKey = TriggerKey.triggerKey(eventName, eventGroup)
        scheduler.pauseTrigger(triggerKey)
    }


    override def resumeEvent(eventName: String, eventGroup: String): Unit = {
        val triggerKey = TriggerKey.triggerKey(eventName, eventGroup)
        scheduler.resumeTrigger(triggerKey)
    }

    //if true the scheduler will not allow this method to return until all currently executing jobs have completed.
    override def shutdown(wait: Boolean = true): Unit = {
        scheduler.shutdown(wait)
    }

    override def clear(): Unit = {
        scheduler.clear()
    }

}


object QuartzScheduleService{
    def packStatus(ts: Trigger.TriggerState): EventStatus.Value = {
        ts match {
            case Trigger.TriggerState.NONE     =>  EventStatus.NONE
            case Trigger.TriggerState.NORMAL   =>  EventStatus.NORMAL
            case Trigger.TriggerState.PAUSED   =>  EventStatus.PAUSED
            case Trigger.TriggerState.COMPLETE =>  EventStatus.COMPLETE
            case Trigger.TriggerState.ERROR    =>  EventStatus.ERROR
            case Trigger.TriggerState.BLOCKED  =>  EventStatus.BLOCKED
            case _                             =>  println(s"UNKNOWN Trigger Type $ts")
                EventStatus.NONE
        }
    }

    def packData[T](d: T): Option[T] = {
        if(d == null){
            None
        }else{
            Option(d)
        }
    }
}