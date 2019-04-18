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

package moonbox.core.command

object PrivilegeType extends Enumeration {
	type PrivilegeType = Value
	val DDL = Value
	val DCL = Value
	val ACCOUNT = Value

	def apply(privilege: String): PrivilegeType = privilege match {
		case "DDL" => DDL
		case "DCL" => DCL
		case "ACCOUNT" => ACCOUNT
		case _ => throw new Exception(s"Unknown privilege type $privilege.")
	}
}

trait ResourcePrivilege


object SelectPrivilege {
	val NAME = "SELECT"
}
case class SelectPrivilege(columns: Seq[String]) extends ResourcePrivilege
object UpdatePrivilege {
	val NAME = "UPDATE"
}
case class UpdatePrivilege(columns: Seq[String]) extends ResourcePrivilege
case object InsertPrivilege extends ResourcePrivilege {
	val NAME = "INSERT"
}
case object DeletePrivilege extends ResourcePrivilege {
	val NAME = "DELETE"
}
case object TruncatePrivilege extends ResourcePrivilege {
	val NAME = "TRUNCATE"
}
case object AllPrivilege extends ResourcePrivilege {
	val NAMES = Seq(
		SelectPrivilege.NAME,
		UpdatePrivilege.NAME,
		InsertPrivilege.NAME,
		DeletePrivilege.NAME,
		TruncatePrivilege.NAME
	)
}
