package moonbox.grid.deploy.timer

import org.scalatest.{BeforeAndAfterAll, FunSuite}

class ScheduleUtilityTest extends FunSuite with BeforeAndAfterAll{
    test("explain a job1") {
        val cron = "0/20 * * * * ?"
        val explain = ScheduleUtility.describe(cron)
        println(explain)
    }

    test("explain a job2"){
        val cron = "0 * * 1-3 * ? *"  //0 0 10am 1,15 * ? (failed)
        val explain = ScheduleUtility.describe(cron)
        println(explain)
    }

    test("explain a job3"){
        val cron = "0,30 * * ? * MON-FRI"
        val explain = ScheduleUtility.describe(cron)
        println(explain)
    }

    test("explain a job4") {
        val cron = "0 0/2 8-17 * * ?"
        val explain = ScheduleUtility.describe(cron)
        println(explain)
    }

}
