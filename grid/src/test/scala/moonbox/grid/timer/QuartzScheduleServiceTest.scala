/*
package moonbox.grid.timer

import java.util.{Calendar, Date, Properties}
import org.scalatest.{BeforeAndAfterAll, FunSuite}

class QuartzScheduleServiceTest extends FunSuite with BeforeAndAfterAll{
    var service: QuartzScheduleService = _
    val function = new Function5[String, String, String, String, Seq[String], Unit] with Serializable {
        override def apply(v1: String, v2: String, v3: String, v4: String, v5: Seq[String]): Unit = {
            println(s"[${new Date}]: $v1 - $v2 - $v3 - $v4 - ${v5.mkString(",")}")
        }
    }

    override def beforeAll{
        val prop = new Properties()
        prop.put("org.quartz.scheduler.instanceName", "EventScheduler")
        prop.put("org.quartz.threadPool.threadCount", "3")
        prop.put("org.quartz.scheduler.skipUpdateCheck", "true")
        prop.put("org.quartz.jobStore.misfireThreshold", "3000")  //当任务错过触发时间时，最忍受的最长延迟时间 3s ?

        //prop.put("org.quartz.jobStore.class", "org.quartz.simpl.RAMJobStore")  //only use ram store

        prop.put("org.quartz.jobStore.class", "org.quartz.impl.jdbcjobstore.JobStoreTX")
        prop.put("org.quartz.jobStore.driverDelegateClass", "org.quartz.impl.jdbcjobstore.StdJDBCDelegate")
        prop.put("org.quartz.jobStore.useProperties", "false")  //该值表示是否datamap中所有数据都使用properties模式，即字符串，默认是false,使用blob

        prop.put("org.quartz.jobStore.tablePrefix", "QRTZ_")
        prop.put("org.quartz.jobStore.dataSource", "quartzDataSource")
        prop.put("org.quartz.dataSource.quartzDataSource.driver", classOf[com.mysql.jdbc.Driver].getName)
        prop.put("org.quartz.dataSource.quartzDataSource.URL", "jdbc:mysql://master:3306/quartz-test")
        prop.put("org.quartz.dataSource.quartzDataSource.user", "root")
        prop.put("org.quartz.dataSource.quartzDataSource.password", "123456")
        prop.put("org.quartz.dataSource.quartzDataSource.maxConnections", "10")

        service = new QuartzScheduleService(prop)
    }

    override def afterAll(): Unit = {
        service.clear()
        service.shutdown(false)
    }

    test("recovery") {
        Thread.sleep(50000)
    }

    test("schedule a job every 2s"){
        try {
            val org = "yrd"
            val appId = "app1"
            val eventId = "event1"
            val cron = "0/2 * * * * ?"
            val entity = ScheduledEntity(eventId, org, appId, org, Seq("select * from table"), cron)
            service.startEvent(entity, function)

            Thread.sleep(10000)

            service.clear()
        }catch{
            case e: Exception =>
                e.printStackTrace()
        }
    }

    test("get job and app list") {
        val org = "yrd"
        val appId = "app1"
        val eventId = "event1"
        val cron = "0/3 * * * * ?"
        val entity = ScheduledEntity(eventId, org, appId, org, Seq("select * from table"), cron)
        service.startEvent(entity, function )

        val eventId2 = "event2"
        val cron2 = "0/2 * * * * ?"
        val entity2 = ScheduledEntity(eventId2, org, appId, org, Seq("select * from table"), cron2)
        service.startEvent(entity2, function )

        Thread.sleep(5000)
        println(s"event: ${service.getAllEvents().mkString(",")}")
        println(s"app: ${service.getAllApplications().mkString(",")}")
        println(s"running: ${service.getPendingEvents().mkString(",")}")

        Thread.sleep(5000)
        println(s"event: ${service.getAllEvents().mkString(",")}")
        println(s"app: ${service.getAllApplications().mkString(",")}")
        println(s"running: ${service.getPendingEvents().mkString(",")}")

        service.clear()
    }

    test("pause and resume event") {
        val org = "yrd"
        val appId = "app1"
        val eventId = "event1"
        val cron = "0/1 * * * * ?"
        val entity = ScheduledEntity(eventId, org, appId, org, Seq("select * from table"), cron)
        service.startEvent(entity, function)

        Thread.sleep(5000)

        println(s"-------pause ${new Date()}-------")
        service.pauseEvent(eventId, org)
        Thread.sleep(10000)

        println(s"-------resume ${new Date()}-------")
        service.resumeEvent(eventId, org)
        Thread.sleep(5000)
    }

    test("delete event") {
        val org = "yrd"
        val appId = "app1"
        val eventId = "event1"
        val cron = "0/1 * * * * ?"
        val entity = ScheduledEntity(eventId, org, appId, org, Seq("select * from table"), cron)
        service.startEvent(entity, function)

        Thread.sleep(5000)

        println(s"-------delete ${new Date()}-------")
        service.deleteEvent(eventId, org)

        Thread.sleep(5000)
    }

    test("pause and resume app") {
        val org = "yrd"
        val appId = "app1"
        val eventId = "event1"
        val cron = "0/1 * * * * ?"
        val entity = ScheduledEntity(eventId, org, appId, org, Seq("select * from table"), cron)
        service.startEvent(entity, function)

        val eventId2 = "event2"
        val cron2 = "0/1 * * * * ?"
        val entity2 = ScheduledEntity(eventId2, org, appId, org, Seq("select * from table"), cron2)
        service.startEvent(entity2, function)

        val appId3 = "app3"
        val eventId3 = "event3"
        val cron3 = "0/1 * * * * ?"
        val entity3 = ScheduledEntity(eventId3, org, appId3, org, Seq("select * from table"), cron3)
        service.startEvent(entity3, function)

        Thread.sleep(5000)

        println(s"-------pause ${new Date()}-------")
        service.pauseApplication(appId, org)
        Thread.sleep(10000)

        println(s"-------resume ${new Date()}-------")
        service.resumeApplication(appId, org)
        Thread.sleep(5000)
    }


    test("update event") {
        val org = "yrd"
        val appId = "app1"
        val eventId = "event1"
        val cron = "0/10 * * * * ?"
        val entity = ScheduledEntity(eventId, org, appId, org, Seq("select * from table"), cron)
        service.startEvent(entity, function)

        println(s"-------update ${new Date()}-------")
        Thread.sleep(21000)

        val cron2 = "0/1 * * * * ?"
        val entity2 = ScheduledEntity(eventId, org, appId, org, Seq("select * from table"), cron2)
        service.updateEvent(entity2)
        Thread.sleep(20000)
    }

    test("update application") {
        val org = "yrd"
        val appId = "app1"
        val eventId = "event1"
        val cron = "0/1 * * * * ?"
        val entity = ScheduledEntity(eventId, org, appId, org, Seq("select * from table"), cron)
        service.startEvent(entity, function)

        val eventId2 = "event2"
        val entity2 = ScheduledEntity(eventId2, org, appId, org, Seq("select * from table"), cron)
        service.startEvent(entity2, function)

        Thread.sleep(5000)
        println(s"-------update new job ${new Date()}-------")
        //service.pauseApplication(appId, org)
        ////service.deleteApplication(appId, org)  //no need to delete
        service.updateApplication(appId, org, Seq("select * from table2"), function)
        //service.resumeApplication(appId, org)

        Thread.sleep(5000)

    }

    test("run between interval") {
        val org = "yrd"
        val appId = "app1"
        val eventId = "event1"
        val cron = "0/1 * * * * ?"

        val tenMinBefore = getTimeByMinute(-30)  //30 sec before
        val tenMinAfter = getTimeByMinute(30)    //30 sec after

        val startDate = new Date(tenMinBefore._1, tenMinBefore._2, tenMinBefore._3, tenMinBefore._4, tenMinBefore._5, tenMinBefore._6)
        val endDate = new Date(tenMinAfter._1, tenMinAfter._2, tenMinAfter._3, tenMinAfter._4, tenMinAfter._5, tenMinAfter._6)
        val entity = ScheduledEntity(eventId, org, appId, org, Seq("select * from table"), cron, Option(startDate), Option(endDate))
        service.startEvent(entity, function)

        println(s"-------run between [${tenMinBefore._4}:${tenMinBefore._5}:${tenMinBefore._6}, ${tenMinAfter._4}:${tenMinAfter._5}:${tenMinAfter._6}], now ${new Date()}-------")
        Thread.sleep(120000)
    }

    test("run before interval") {
        val org = "yrd"
        val appId = "app1"
        val eventId = "event1"
        val cron = "0/1 * * * * ?"

        val tenMinBefore = getTimeByMinute(30)
        val tenMinAfter = getTimeByMinute(60)

        val startDate = new Date(tenMinBefore._1, tenMinBefore._2, tenMinBefore._3, tenMinBefore._4, tenMinBefore._5, tenMinBefore._6)
        val endDate = new Date(tenMinAfter._1, tenMinAfter._2, tenMinAfter._3, tenMinAfter._4, tenMinAfter._5, tenMinAfter._6)
        val entity = ScheduledEntity(eventId, org, appId, org, Seq("select * from table"), cron, Option(startDate), Option(endDate))
        service.startEvent(entity, function)

        println(s"-------run between [${tenMinBefore._4}:${tenMinBefore._5}:${tenMinBefore._6}, ${tenMinAfter._4}:${tenMinAfter._5}:${tenMinAfter._6}], now ${new Date()}-------")
        Thread.sleep(120000)
    }


    def getTimeByMinute(second: Int): (Int, Int, Int, Int, Int, Int) = {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.SECOND, second)

        val year = calendar.get(Calendar.YEAR) - 1900  //
        val month = calendar.get(Calendar.MONTH)      // 0~
        val day = calendar.get(Calendar.DAY_OF_MONTH)  //1~
        val hour = calendar.get(Calendar.HOUR_OF_DAY)  //0~
        val min = calendar.get(Calendar.MINUTE)  //0 ~
        val sec = calendar.get(Calendar.SECOND)  //0 ~
        (year, month, day, hour, min, sec)
    }

}
*/
