package moonbox.core

import moonbox.common.MbConf
import org.scalatest.FunSuite

class MbSessionSuite extends FunSuite {
	val conf = new MbConf()
	test("mbSession") {
		val mbSession = MbSession.getMbSession(conf)
	}
}
