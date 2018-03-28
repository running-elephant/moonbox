package moonbox.grid.deploy.rest

import moonbox.common.MbConf
import moonbox.grid.config._
import org.json4s.DefaultFormats
import org.json4s.native.JsonMethods._
import pdi.jwt.{Jwt, JwtAlgorithm, JwtClaim, JwtHeader}

class TokenManager(conf: MbConf) {

	private lazy val _JWT_ALGORITHM = conf.get(JWT_ALGORITHM.key, JWT_ALGORITHM.defaultValueString)
	private lazy val _JWT_TIMEOUT = conf.get(JWT_TIMEOUT.key, JWT_TIMEOUT.defaultValue.get)
	private lazy val _JWT_SECRET = conf.get(JWT_SECRET.key, JWT_SECRET.defaultValueString)

	private lazy val jwtHeader = JwtHeader(JwtAlgorithm.fromString(_JWT_ALGORITHM), "JWT")

	def encode(username: String): String = {
		val jwtClaim: JwtClaim = JwtClaim() + ("username", username)
		Jwt.encode(jwtHeader, jwtClaim.expiresIn(1000000000), _JWT_SECRET)
	}

	def decode(token: String): Option[String] = {
		implicit val formats = DefaultFormats
		Jwt.decodeRaw(token, _JWT_SECRET, JwtAlgorithm.allHmac()).map { decoded =>
			Some(parse(decoded).extract[Username])
		}.getOrElse(None).map(_.username)
	}

	case class Username(username: String)
}
