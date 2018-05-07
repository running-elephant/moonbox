package moonbox.grid.deploy.rest

import java.util.UUID

import moonbox.common.MbConf
import moonbox.grid.config._
import org.json4s.DefaultFormats
import org.json4s.native.JsonMethods._
import pdi.jwt.{Jwt, JwtAlgorithm, JwtClaim, JwtHeader}

class TokenManager(conf: MbConf) {

	private val _JWT_ALGORITHM = conf.get(JWT_ALGORITHM.key, JWT_ALGORITHM.defaultValueString)
	private val _JWT_TIMEOUT = conf.get(JWT_TIMEOUT.key, JWT_TIMEOUT.defaultValue.get)
	private val _JWT_SECRET = conf.get(JWT_SECRET.key, JWT_SECRET.defaultValueString)

	private val jwtHeader = JwtHeader(JwtAlgorithm.fromString(_JWT_ALGORITHM), "JWT")

	def encode(username: String): String = {
		val jwtClaim: JwtClaim = JwtClaim() + ("username", username) + ("seed", UUID.randomUUID().toString)
		Jwt.encode(jwtHeader, jwtClaim.expiresIn(_JWT_TIMEOUT), _JWT_SECRET)
	}

	def decode(token: String): Option[String] = {
		implicit val formats = DefaultFormats
		Jwt.decodeRaw(token, _JWT_SECRET, JwtAlgorithm.allHmac()).map { decoded =>
			Some(parse(decoded).extract[Username])
		}.getOrElse(None).map(_.username)
	}

	def isvalid(token: String): Boolean = {
		Jwt.isValid(token, _JWT_SECRET, JwtAlgorithm.allHmac())
	}

	private case class Username(username: String, seed: String)
}
