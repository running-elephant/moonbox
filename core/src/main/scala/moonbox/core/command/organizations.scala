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

import moonbox.catalog.{CatalogOrganization, CatalogUser, PasswordEncryptor}
import moonbox.core.MoonboxSession
import org.apache.spark.sql.Row
import org.apache.spark.sql.catalyst.expressions.{Attribute, AttributeReference}
import org.apache.spark.sql.types.StringType


trait Organization

case class CreateOrganization(
	name: String,
	comment: Option[String],
	config: Map[String, String] = Map(),
	ignoreIfExists: Boolean) extends MbRunnableCommand with Organization {

	override def run(mbSession: MoonboxSession): Seq[Row] = {
		val organization = CatalogOrganization(
			name = name,
			config = config,
			description = comment
		)
		mbSession.catalog.createOrganization(organization, ignoreIfExists)
		Seq.empty[Row]
	}
}

case class AlterOrganizationSetName(
	name: String,
	newName: String) extends MbRunnableCommand with Organization {

	override def run(mbSession: MoonboxSession): Seq[Row] = {
		mbSession.catalog.renameOrganization(name, newName)
		Seq.empty[Row]
	}
}

case class AlterOrganizationSetComment(
	name: String,
	comment: String) extends MbRunnableCommand with Organization {

	override def run(mbSession: MoonboxSession): Seq[Row] = {
		val existOrganization: CatalogOrganization = mbSession.catalog.getOrganization(name)
		mbSession.catalog.alterOrganization(
			existOrganization.copy(description = Some(comment))
		)
		Seq.empty[Row]
	}
}

case class AlterOrganizationSetOptions(
	name: String,
	options: Map[String, String]) extends MbRunnableCommand with Organization {

	override def run(mbSession: MoonboxSession): Seq[Row] = {
		val existOrganization: CatalogOrganization = mbSession.catalog.getOrganization(name)
		mbSession.catalog.alterOrganization(
			existOrganization.copy(config = existOrganization.config ++ options)
		)
		Seq.empty[Row]
	}
}


case class DropOrganization(
	name: String,
	ignoreIfNotExists: Boolean,
	cascade: Boolean) extends MbRunnableCommand with Organization {

	override def run(mbSession: MoonboxSession): Seq[Row] = {
		mbSession.catalog.dropOrganization(name, ignoreIfNotExists, cascade)
		Seq.empty[Row]
	}
}

case class ShowOrganizations(
	pattern: Option[String]) extends MbRunnableCommand with Organization {

	override def output: Seq[Attribute] = {
		AttributeReference("ORGANIZATION", StringType, nullable = false)() :: Nil
	}

	override def run(mbSession: MoonboxSession): Seq[Row] = {
		pattern.map(mbSession.catalog.listOrganizations).getOrElse(
			mbSession.catalog.listOrganizations()
		).filterNot(_.name.equalsIgnoreCase("SYSTEM")).map(o => Row(o.name))
	}
}


case class CreateSa(
	name: String,
	password: String,
	organization: String,
	configuration: Map[String, String],
	ignoreIfExists: Boolean) extends MbRunnableCommand with Organization {

	override def run(mbSession: MoonboxSession): Seq[Row] = {

		val catalogSa = CatalogUser(
			org = organization,
			name = name,
			password = PasswordEncryptor.encryptSHA(password),
			account = true,
			ddl = true,
			dcl = true,
			grantAccount = true,
			grantDdl = true,
			grantDcl = true,
			isSA = true,
			configuration = configuration
		)
		mbSession.catalog.createUser(catalogSa, ignoreIfExists)

		Seq.empty[Row]
	}
}

case class AlterSaSetName(
	name: String,
	newName: String,
	organization: String) extends MbRunnableCommand with Organization {

	override def run(mbSession: MoonboxSession): Seq[Row] = {

		mbSession.catalog.renameUser(organization, name, newName)

		Seq.empty[Row]
	}
}

case class AlterSaSetPassword(
	name: String,
	newPassword: String,
	organization: String) extends MbRunnableCommand with Organization {

	override def run(mbSession: MoonboxSession): Seq[Row] = {

		val existSa = mbSession.catalog.getUser(organization, name)

		mbSession.catalog.alterUser(
			existSa.copy(password = PasswordEncryptor.encryptSHA(newPassword))
		)

		Seq.empty[Row]
	}
}

case class AlterSaSetOptions(
	name: String,
	options: Map[String, String],
	organization: String) extends MbRunnableCommand with Organization {

	override def run(mbSession: MoonboxSession): Seq[Row] = {

		val existSa = mbSession.catalog.getUser(organization, name)

		mbSession.catalog.alterUser(
			existSa.copy(configuration = existSa.configuration ++ options)
		)

		Seq.empty[Row]
	}
}

case class DropSa(
	name: String,
	organization: String,
	ignoreIfNotExists: Boolean
) extends MbRunnableCommand with Organization {

	override def run(mbSession: MoonboxSession): Seq[Row] = {

		mbSession.catalog.dropUser(organization, name, ignoreIfNotExists)

		Seq.empty[Row]
	}
}

case class DescOrganization(name: String) extends MbRunnableCommand with Organization {

	override def output: Seq[Attribute] = {
		AttributeReference("PROPERTY_NAME", StringType, nullable = false)() ::
			AttributeReference("VALUE", StringType, nullable = false)() ::
			Nil
	}

	override def run(mbSession: MoonboxSession): Seq[Row] = {
		val organization = mbSession.catalog.getOrganization(name)
		Row("organization", organization.name) ::
		Row("config", organization.config.map{ case (k, v) => s"$k '$v'"}.mkString(", ")) ::
		Row("description", organization.description.getOrElse("-")) :: Nil
	}
}

case class ShowSas(pattern: Option[String]) extends MbRunnableCommand with Organization {

	override def output: Seq[Attribute] = {
		AttributeReference("ORGANIZATION", StringType, nullable = false)() ::
			AttributeReference("USER_NAME", StringType, nullable = false)() :: Nil
	}

	override def run(mbSession: MoonboxSession): Seq[Row] = {
		val sas = pattern.map(mbSession.catalog.listSas).getOrElse(
			mbSession.catalog.listSas()
		)
		sas.map(u => Row(u.org, u.name))
	}
}

