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

package moonbox.grid.deploy.rest

import java.util.UUID

import moonbox.common.MbConf
import moonbox.grid.config._
import moonbox.grid.deploy.security.Session
import org.json4s.DefaultFormats
import org.json4s.JsonAST.JString
import org.json4s.native.JsonMethods._
import pdi.jwt.{Jwt, JwtAlgorithm, JwtClaim, JwtHeader}

class TokenEncoder(conf: MbConf) {

	private val _JWT_ALGORITHM = conf.get(JWT_ALGORITHM)

	private val _JWT_SECRET = conf.get(JWT_SECRET)

	private val jwtHeader = JwtHeader(JwtAlgorithm.fromString(_JWT_ALGORITHM), "JWT")

	private val SEED_KEY = "seed"


	def encode(session: Session, expires: Long): String = {
		val jwtClaim = JwtClaim().expiresIn(expires) ++ (session.toSeq:_*) + (SEED_KEY, UUID.randomUUID().toString)
		Jwt.encode(jwtHeader, jwtClaim, _JWT_SECRET)
	}

	def decode(token: String): Option[Session] = {
		implicit val formats = DefaultFormats
		Jwt.decodeRaw(token, _JWT_SECRET, JwtAlgorithm.allHmac()).map { decoded =>
			val paresed = parse(decoded)
			val JString(s) =  paresed \ Session.KEY

			val sessionBuilder = Session.builder
			Session.stringToKeys(s).foreach { key =>
				val JString(value) = paresed \ key
				sessionBuilder.put(key, value)
			}
			Some(sessionBuilder.build())
		}.getOrElse(None)
	}

	def isvalid(token: String): Boolean = {
		Jwt.isValid(token, _JWT_SECRET, JwtAlgorithm.allHmac())
	}

}
