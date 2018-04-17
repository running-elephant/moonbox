package moonbox.core

import moonbox.common.MbConf
import moonbox.core.parser.MbParser
import org.scalatest.FunSuite

class MbSessionSuite extends FunSuite {
	val conf = new MbConf()
	val mbParser = new MbParser
	test("mbSession") {
		val mbSession = MbSession.getMbSession(conf)
		mbSession.bindUser("jack")
		//mbSession.execute("test", mbParser.parsePlan("GRANT DML ON default.mysql_test_booklist.* TO USER jack"))
		mbSession.sql("select * from mysql_test_booklist").show()
	}
}
