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
		conf.set("moonbox.timer.org.quartz.dataSource.quartzDataSource.driver", "com.mysql.jdbc.Driver")
		conf.set("moonbox.timer.org.quartz.dataSource.quartzDataSource.URL", "jdbc:mysql://master:3306/quartz-test")
		conf.set("moonbox.timer.org.quartz.dataSource.quartzDataSource.user", "root")
		conf.set("moonbox.timer.org.quartz.dataSource.quartzDataSource.password", "123456")
		conf.set("moonbox.timer.org.quartz.dataSource.quartzDataSource.maxConnections", "10")
		timedEventService = new TimedEventServiceImpl(conf, new EventHandler() {
			override def apply(v0: String, v1: String, v2: String, v3: Seq[String], v4: Map[String, String]): Unit = {
				println(v1 + v2 + v3 + v4)
			}
		})
		timedEventService.start()
	}

	test("add event") {
		timedEventService.addTimedEvent(EventEntity(
			group = "group_test",
			name = "event_test",
			lang = "mql",
			sqls = Seq(),
			config = Map(
				"spark.master" -> "local[*]",
				"spark.app.name" ->"test1"
			),
			cronExpr = "0/2 * * * * ?",
			org = "moonbox",
			definer = "sally",
			start = None,
			end = None,
			desc = None
		)
		)
		timedEventService.addTimedEvent(EventEntity(
			group = "group_test",
			name = "event_test2",
			lang = "hql",
			sqls = Seq(),
			config = Map(
				"spark.master" -> "local[*]",
				"spark.app.name" ->"test1"
			),
			cronExpr = "0/4 * * * * ?",
			org = "moonbox",
			definer = "lee",
			start = None,
			end = None,
			desc = None)
		)
		Thread.sleep(10000)
		timedEventService.getTimedEvents("group_test").foreach(println)
	}

	override protected def afterAll(): Unit = {
		timedEventService.clear()
		timedEventService.stop()
	}
}
