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

package moonbox.grid.deploy.security

import java.util.UUID

import moonbox.common.MbConf
import moonbox.grid.config._
import org.json4s.DefaultFormats
import org.json4s.native.JsonMethods._
import pdi.jwt.{Jwt, JwtAlgorithm, JwtClaim, JwtHeader}

private[deploy] class TokenEncoder(conf: MbConf) {

	private val _JWT_ALGORITHM = conf.get(JWT_ALGORITHM)

	private val _JWT_SECRET = conf.get(JWT_SECRET)

	private val jwtHeader = JwtHeader(JwtAlgorithm.fromString(_JWT_ALGORITHM), "JWT")

	def encode(org: String, username: String): String = {
		val jwtClaim: JwtClaim = JwtClaim() + ("org", org) + ("username", username) + ("seed", UUID.randomUUID().toString)
		Jwt.encode(jwtHeader, jwtClaim, _JWT_SECRET)
	}

	def decode(token: String): Option[(String, String)] = {
		implicit val formats = DefaultFormats
		Jwt.decodeRaw(token, _JWT_SECRET, JwtAlgorithm.allHmac()).map { decoded =>
			Some(parse(decoded).extract[Username])
		}.getOrElse(None).map(u => (u.org, u.username))
	}

	def isvalid(token: String): Boolean = {
		Jwt.isValid(token, _JWT_SECRET, JwtAlgorithm.allHmac())
	}

	private case class Username(org: String, username: String, seed: String)
}
