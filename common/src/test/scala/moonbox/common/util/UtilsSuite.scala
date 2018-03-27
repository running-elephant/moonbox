package moonbox.common.util

import org.scalatest.FunSuite

class UtilsSuite extends FunSuite {
	import Utils._
	test("allEquals") {
		assert(allEquals(Seq(1, 1, 1)))
		assert(!allEquals(Seq(1, 1, 2)))
		assert(allEquals(Seq("abc", "abc", "abc")))
		assert(!allEquals(Seq("abc", "abd", "abc")))
	}
}
