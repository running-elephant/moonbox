package moonbox.grid.timer

import moonbox.common.MbConf
import org.scalatest.{BeforeAndAfterAll, FunSuite}

class TimedScheduleServiceSuite extends FunSuite with BeforeAndAfterAll {
	private var timedEventService: TimedEventService = _
	override protected def beforeAll(): Unit = {
		val conf = new MbConf()
		conf.set("moonbox.timer.org.quartz.scheduler.instanceName", "EventScheduler")
		conf.set("moonbox.timer.org.quartz.threadPool.threadCount", "3")
		conf.set("moonbox.timer.org.quartz.scheduler.skipUpdateCheck", "true")
		conf.set("moonbox.timer.org.quartz.jobStore.misfireThreshold", "3000")
		conf.set("moonbox.timer.org.quartz.jobStore.class", "org.quartz.impl.jdbcjobstore.JobStoreTX")
		conf.set("moonbox.timer.org.quartz.jobStore.driverDelegateClass", "org.quartz.impl.jdbcjobstore.StdJDBCDelegate")
		conf.set("moonbox.timer.org.quartz.jobStore.useProperties", "false")
		conf.set("moonbox.timer.org.quartz.jobStore.tablePrefix", "QRTZ_")
		conf.set("moonbox.timer.org.quartz.jobStore.dataSource", "quartzDataSource")
		conf.set("moonbox.timer.org.quartz.dataSource.quartzDataSource.driver", classOf[com.mysql.jdbc.Driver].getName)
		conf.set("moonbox.timer.org.quartz.dataSource.quartzDataSource.URL", "jdbc:mysql://master:3306/quartz-test")
		conf.set("moonbox.timer.org.quartz.dataSource.quartzDataSource.user", "root")
		conf.set("moonbox.timer.org.quartz.dataSource.quartzDataSource.password", "123456")
		conf.set("moonbox.timer.org.quartz.dataSource.quartzDataSource.maxConnections", "10")
		timedEventService = new TimedEventServiceImpl(conf)
		timedEventService.start()
	}

	test("add event") {
		timedEventService.addTimedEvent(EventEntity(
			group = "group_test",
			name = "event_test",
			sqls = Seq(),
			cronExpr = "0/2 * * * * ?",
			definer = "sally",
			start = None,
			end = None,
			desc = None,
			function = () => {
				println()
			}
		)
		)
		timedEventService.addTimedEvent(EventEntity(
			group = "group_test",
			name = "event_test2",
			sqls = Seq(),
			cronExpr = "0/4 * * * * ?",
			definer = "lee",
			start = None,
			end = None,
			desc = None,
			function = () => {})
		)
		Thread.sleep(10000)
		timedEventService.getTimedEvents("group_test").foreach(println)
	}

	override protected def afterAll(): Unit = {
		timedEventService.clear()
		timedEventService.stop()
	}
}
