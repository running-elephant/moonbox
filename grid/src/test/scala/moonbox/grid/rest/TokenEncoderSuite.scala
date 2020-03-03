package moonbox.grid.rest

import moonbox.common.MbConf
import moonbox.grid.deploy.rest.TokenEncoder
import moonbox.grid.deploy.security.Session
import org.scalatest.FunSuite

class TokenEncoderSuite extends FunSuite {
	test("token encode") {
		val encoder = new TokenEncoder(new MbConf())
		val session = Session.builder.put("user", "sally")
		    .put("userId", "1")
		    .put("org", "moonbox")
		    .put("orgId", "1")
		    .build()

		val token = encoder.encode(session, 5)
		assert(encoder.isvalid(token))
		assert(encoder.decode(token).isDefined)
		val decodeSession = encoder.decode(token).get
		assert(decodeSession.contains("user"))
		assert(decodeSession.contains("userId"))
		assert(decodeSession.contains("org"))
		assert(decodeSession.contains("orgId"))
		Thread.sleep(5*1000)
		assert(!encoder.isvalid(token))
		assert(encoder.decode(token).isEmpty)
		assert(encoder.decode("abc").isEmpty)
	}
}
