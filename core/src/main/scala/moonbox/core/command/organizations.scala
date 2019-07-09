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

import moonbox.common.util.Utils
import moonbox.core.{MoonboxSession, SessionEnv}
import moonbox.catalog.CatalogOrganization
import org.apache.spark.sql.Row


trait Organization

case class CreateOrganization(
	name: String,
	comment: Option[String],
	ignoreIfExists: Boolean) extends MbRunnableCommand with Organization {

	override def run(mbSession: MoonboxSession)(implicit ctx: SessionEnv): Seq[Row] = {
		val organization = CatalogOrganization(
			name = name,
			description = comment,
			createBy = ctx.userId,
			updateBy = ctx.userId
		)
		mbSession.catalog.createOrganization(organization, ignoreIfExists)
		Seq.empty[Row]
	}
}

case class AlterOrganizationSetName(
	name: String,
	newName: String) extends MbRunnableCommand with Organization {

	override def run(mbSession: MoonboxSession)(implicit ctx: SessionEnv): Seq[Row] = {
		mbSession.catalog.renameOrganization(name, newName, ctx.userId)
		ctx.organizationName = newName
		Seq.empty[Row]
	}
}

case class AlterOrganizationSetComment(
	name: String,
	comment: String) extends MbRunnableCommand with Organization {

	override def run(mbSession: MoonboxSession)(implicit ctx: SessionEnv): Seq[Row] = {
		val existOrganization: CatalogOrganization = mbSession.catalog.getOrganization(name)
		mbSession.catalog.alterOrganization(
			existOrganization.copy(description = Some(comment), updateBy = ctx.userId, updateTime = Utils.now)
		)
		Seq.empty[Row]
	}
}

case class DropOrganization(
	name: String,
	ignoreIfNotExists: Boolean,
	cascade: Boolean) extends MbRunnableCommand with Organization {

	override def run(mbSession: MoonboxSession)(implicit ctx: SessionEnv): Seq[Row] = {
		mbSession.catalog.dropOrganization(name, ignoreIfNotExists, cascade)
		Seq.empty[Row]
	}
}
