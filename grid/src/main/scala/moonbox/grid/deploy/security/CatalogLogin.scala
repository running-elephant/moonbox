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

import moonbox.catalog.{CatalogUser, JdbcCatalog, PasswordEncryptor}
import moonbox.common.{MbConf, MbLogging}
import moonbox.grid.deploy.security.RoleType.RoleType

class CatalogLogin(conf: MbConf, catalog: JdbcCatalog) extends Login with MbLogging {

  override def doLogin(username: String, password: String): Session = {
    val (org, user) = parseUsername(username)
    catalog.getUserOption(org, user) match {
      case Some(catalogUser) =>
        if (catalogUser.password == PasswordEncryptor.encryptSHA(password)) {
          val orgId = catalog.organizationId(org)
          val userId = catalog.userId(orgId, user)
          val roleType = getRoleType(catalogUser)
          Session.builder
            .put("org", org)
            .put("orgId", s"$orgId")
            .put("user", user)
            .put("userId", s"$userId")
            .put("roleType", s"${roleType.toString}")
            .put("password", s"$password")
            .build()
        }
        else {
          throw new PasswordNotMatchException
        }
      case _ => throw new UserNotFoundException(username)
    }
  }

  private def parseUsername(username: String): (String, String) = {
    if (username.equalsIgnoreCase("root")) {
      ("SYSTEM", username)
    } else {
      val orgUser = username.split("@")
      if (orgUser.length != 2) {
        throw new UsernameFormatException(username)
      } else {
        val org = orgUser(0)
        val user = orgUser(1)
        (org, user)
      }
    }
  }

  private def getRoleType(user: CatalogUser): RoleType = {
    if (user.org == "SYSTEM" && user.name == "ROOT") RoleType.ROOT
    else if (user.isSA) RoleType.SA
    else RoleType.NORMAL
  }
}
