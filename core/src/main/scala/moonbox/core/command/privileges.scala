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


object RolePrivilege extends Enumeration {
	type RolePrivilege = Value
	val DDL = Value
	val DCL = Value
	val ACCOUNT = Value

	def apply(privilege: String): RolePrivilege = privilege match {
		case "DDL" => DDL
		case "DCL" => DCL
		case "ACCOUNT" => ACCOUNT
		case _ => throw new Exception(s"Unknown privilege type $privilege.")
	}
}


trait ResourcePrivilege {
	val NAME: String

	def isColumnLevel: Boolean = false
}

object ResourcePrivilege {
	val ALL = Seq(
		SelectPrivilege,
		UpdatePrivilege,
		InsertPrivilege,
		DeletePrivilege,
		TruncatePrivilege
	)
}


case object SelectPrivilege extends ResourcePrivilege {
	val NAME = "SELECT"
}

case object UpdatePrivilege extends ResourcePrivilege {
	val NAME = "UPDATE"
}

case object InsertPrivilege extends ResourcePrivilege {
	val NAME = "INSERT"
}

case object DeletePrivilege extends ResourcePrivilege {
	val NAME = "DELETE"
}

case object TruncatePrivilege extends ResourcePrivilege {
	val NAME = "TRUNCATE"
}

case class ColumnSelectPrivilege(column: Seq[String]) extends ResourcePrivilege {
	override def isColumnLevel: Boolean = true

	val NAME = "SELECT"
}

case class ColumnUpdatePrivilege(column: Seq[String]) extends ResourcePrivilege {

	override def isColumnLevel: Boolean = true

	val NAME = "UPDATE"
}

