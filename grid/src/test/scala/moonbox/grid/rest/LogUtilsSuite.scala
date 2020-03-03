package moonbox.grid.rest

import moonbox.grid.deploy.rest.service.LogUtils
import org.scalatest.FunSuite


class LogUtilsSuite extends FunSuite {

  test("execCmd") {
    val cmd = "tail -n1000 /Users/swallow/IdeaProjects/moonbox/assembly/target/moonbox-assembly_2.11-0.3.0-beta-dist/moonbox-0.3.0-beta/logs/adx-localtest.log"
//    val cmd = "ls /"
    val user = "swallow"
    val host = "localhost"
    val password = "wxhwzj"
    val port = 22
    println(LogUtils.execCmd(user, host, Some(port), Some(password), cmd))
  }
}
