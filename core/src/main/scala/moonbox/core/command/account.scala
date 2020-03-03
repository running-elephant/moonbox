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

import moonbox.catalog._
import moonbox.core.MoonboxSession
import org.apache.spark.sql.Row
import org.apache.spark.sql.catalyst.expressions.AttributeReference
import org.apache.spark.sql.types.StringType


trait Account



case class CreateUser(
	name: String,
	password: String,
	configuration: Map[String, String],
	ignoreIfExists: Boolean) extends MbRunnableCommand with Account {

	override def run(mbSession: MoonboxSession): Seq[Row] = {
		import mbSession.catalog._

		val catalogUser = CatalogUser(
			org = getCurrentOrg,
			name = name,
			password = PasswordEncryptor.encryptSHA(password),
			configuration = configuration
		)

		createUser(catalogUser, ignoreIfExists)

		Seq.empty[Row]
	}
}

case class AlterUserSetName(
	name: String,
	newName: String) extends MbRunnableCommand with Account {

	override def run(mbSession: MoonboxSession): Seq[Row] = {

		import mbSession.catalog._

		renameUser(getCurrentOrg, name, newName)

		Seq.empty[Row]
	}
}

case class AlterUserSetPassword(
	name: String,
	newPassword: String) extends MbRunnableCommand with Account {

	override def run(mbSession: MoonboxSession): Seq[Row] = {

		import mbSession.catalog._

		alterUser(
			getUser(getCurrentOrg, name).copy(
				password = PasswordEncryptor.encryptSHA(newPassword)
			)
		)

		Seq.empty[Row]
	}
}

case class AlterUserSetOptions(
	name: String,
	options: Map[String, String]) extends MbRunnableCommand with Account {

	override def run(mbSession: MoonboxSession): Seq[Row] = {

		import mbSession.catalog._

		val existUser = getUser(getCurrentOrg, name)
		alterUser(
			existUser.copy(
				configuration = existUser.configuration ++ options
			)
		)

		Seq.empty[Row]
	}
}

case class DropUser(
	name: String,
	ignoreIfNotExists: Boolean) extends MbRunnableCommand with Account {

	override def run(mbSession: MoonboxSession): Seq[Row] = {

		import mbSession.catalog._

		dropUser(getCurrentOrg, name, ignoreIfNotExists)

		Seq.empty[Row]
	}
}

case class ShowUsers(
	pattern: Option[String]) extends MbRunnableCommand with Account {

	override def output = {
		AttributeReference("ORGANIZATION", StringType, nullable = false)() ::
			AttributeReference("USER_NAME", StringType, nullable = false)() :: Nil
	}

	override def run(mbSession: MoonboxSession): Seq[Row] = {

		val currentOrg = mbSession.catalog.getCurrentOrg
		mbSession.catalog.listUsers(currentOrg, pattern).map(u => Row(currentOrg, u.name))

	}
}

case class DescUser(user: String) extends MbRunnableCommand with Account {

	override def output = {
		AttributeReference("PROPERTY_NAME", StringType, nullable = false)() ::
			AttributeReference("VALUE", StringType, nullable = false)() ::
			Nil
	}

	override def run(mbSession: MoonboxSession): Seq[Row] = {
		val catalogUser = mbSession.catalog.getUser(mbSession.catalog.getCurrentOrg,user)
		val result = Row("User Name", catalogUser.name) ::
			Row("Account", catalogUser.account.toString) ::
			Row("DDL", catalogUser.ddl.toString) ::
			Row("DCL", catalogUser.dcl.toString) ::
			Row("Grant Account", catalogUser.grantAccount.toString) ::
			Row("Grant DDL", catalogUser.grantDdl.toString) ::
			Row("Grant DCL", catalogUser.grantDcl.toString) ::
			Row("IsSA", catalogUser.isSA.toString) :: Nil
		result
	}
}
