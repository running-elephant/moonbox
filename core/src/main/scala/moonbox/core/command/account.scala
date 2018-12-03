/*-
 * <<
 * Moonbox
 * ==
 * Copyright (C) 2016 - 2018 EDP
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
import moonbox.core.MbSession
import moonbox.core.catalog._
import org.apache.spark.sql.Row


trait Account

case class CreateSa(
	name: String,
	password: String,
	organization: String,
	ignoreIfExists: Boolean) extends MbRunnableCommand with Account {

	override def run(mbSession: MbSession)(implicit ctx: CatalogSession): Seq[Row] = {
		val catalogOrganization = mbSession.catalog.getOrganization(organization)
		val catalogUser = CatalogUser(
			name = name,
			password = PasswordEncryptor.encryptSHA(password),
			account = true,
			ddl = true,
			dcl = true,
			grantAccount = true,
			grantDdl = true,
			grantDcl = true,
			isSA = true,
			organizationId = catalogOrganization.id.get,
			createBy = ctx.userId,
			updateBy = ctx.userId
		)
		mbSession.catalog.createUser(catalogUser, catalogOrganization.name, ignoreIfExists)
		Seq.empty[Row]
	}
}

case class AlterSaSetName(
	name: String,
	newName: String,
	organization: String) extends MbRunnableCommand with Account {

	override def run(mbSession: MbSession)(implicit ctx: CatalogSession): Seq[Row] = {
		val catalogOrganization: CatalogOrganization = mbSession.catalog.getOrganization(organization)
		val existUser: CatalogUser = mbSession.catalog.getUser(catalogOrganization.id.get, name)
		require(existUser.isSA, s"ROOT can not alter non-sa.")
		mbSession.catalog.renameUser(catalogOrganization.id.get, organization, name, newName, ctx.userId)

		Seq.empty[Row]
	}
}

case class AlterSaSetPassword(
	name: String,
	newPassword: String,
	organization: String) extends MbRunnableCommand with Account {

	override def run(mbSession: MbSession)(implicit ctx: CatalogSession): Seq[Row] = {
		val catalogOrganization: CatalogOrganization = mbSession.catalog.getOrganization(organization)
		val existUser: CatalogUser = mbSession.catalog.getUser(catalogOrganization.id.get, name)
		require(existUser.isSA, s"ROOT can not alter non-sa.")
		mbSession.catalog.alterUser(
			existUser.copy(password = PasswordEncryptor.encryptSHA(newPassword), updateBy = ctx.userId, updateTime = Utils.now)
		)
		Seq.empty[Row]
	}
}

case class DropSa(
	name: String,
	organization: String,
	ignoreIfNotExists: Boolean
) extends MbRunnableCommand with Account {

	override def run(mbSession: MbSession)(implicit ctx: CatalogSession): Seq[Row] = {
		val catalogOrganization: CatalogOrganization = mbSession.catalog.getOrganization(organization)
		val existUser: CatalogUser = mbSession.catalog.getUser(catalogOrganization.id.get, name)
		require(existUser.isSA, s"ROOT can drop non-sa.")
		mbSession.catalog.dropUser(catalogOrganization.id.get, organization, name, ignoreIfNotExists)
		Seq.empty[Row]
	}
}

case class CreateUser(
	name: String,
	password: String,
	ignoreIfExists: Boolean) extends MbRunnableCommand with Account {

	override def run(mbSession: MbSession)(implicit ctx: CatalogSession): Seq[Row] = {
		val catalogUser = CatalogUser(
			name = name,
			password = PasswordEncryptor.encryptSHA(password),
			organizationId = ctx.organizationId,
			createBy = ctx.userId,
			updateBy = ctx.userId
		)
		mbSession.catalog.createUser(catalogUser, ctx.organizationName, ignoreIfExists)
		Seq.empty[Row]
	}
}

case class AlterUserSetName(
	name: String,
	newName: String) extends MbRunnableCommand with Account {

	override def run(mbSession: MbSession)(implicit ctx: CatalogSession): Seq[Row] = {
		mbSession.catalog.renameUser(ctx.organizationId, ctx.organizationName, name, newName, ctx.userId)
		ctx.userName = newName
		Seq.empty[Row]
	}
}

case class AlterUserSetPassword(
	name: String,
	newPassword: String) extends MbRunnableCommand with Account {

	override def run(mbSession: MbSession)(implicit ctx: CatalogSession): Seq[Row] = {
		if (ctx.userName.equalsIgnoreCase("ROOT")) {
			if (name.equalsIgnoreCase("ROOT")) {
				val root = mbSession.catalog.getUser(-1, "ROOT")
				mbSession.catalog.alterUser(root.copy(
					password = PasswordEncryptor.encryptSHA(newPassword),
					updateBy = ctx.userId,
					updateTime = Utils.now))
			} else {
				throw new Exception("ROOT can only alter Sa's password. Please use 'ALTER SA ... ' command alter sa's password.")
			}
		} else {
			val canAccount = mbSession.catalog.canAccount(ctx.userId)
			val existUser: CatalogUser = mbSession.catalog.getUser(ctx.organizationId, name)
			if ((canAccount && (!name.equalsIgnoreCase("ROOT") && !mbSession.catalog.isSa(existUser.id.get))) || (ctx.userName == name)) {
				mbSession.catalog.alterUser(
					existUser.copy(
						password = PasswordEncryptor.encryptSHA(newPassword),
						updateBy = ctx.userId,
						updateTime = Utils.now
					)
				)
			} else if (!canAccount) {
				throw new Exception("Access Denied.Please check your ACCOUNT privilege.")
			} else if (mbSession.catalog.isSa(existUser.id.get)) {
				throw new Exception("Access Denied.Sa's Password can only alter by ROOT or itself.")
			} else if (name.equalsIgnoreCase("ROOT")) {
				throw new Exception("Access Denied.ROOT's password can only alter by itself.")
			} else {
				throw new Exception("Access Denied.")
			}
		}
		Seq.empty[Row]
	}
}

case class DropUser(
	name: String,
	ignoreIfNotExists: Boolean) extends MbRunnableCommand with Account {

	override def run(mbSession: MbSession)(implicit ctx: CatalogSession): Seq[Row] = {
		mbSession.catalog.dropUser(ctx.organizationId, ctx.organizationName, name, ignoreIfNotExists)
		Seq.empty[Row]
	}
}

case class CreateGroup(
	name: String,
	comment: Option[String],
	ignoreIfExists: Boolean) extends MbRunnableCommand with Account {

	override def run(mbSession: MbSession)(implicit ctx: CatalogSession): Seq[Row] = {
		val catalogGroup = CatalogGroup(
			name = name,
			description = comment,
			organizationId = ctx.organizationId,
			createBy = ctx.userId,
			updateBy = ctx.userId
		)
		mbSession.catalog.createGroup(catalogGroup, ctx.organizationName, ignoreIfExists)
		Seq.empty[Row]
	}
}

case class AlterGroupSetName(
	name: String,
	newName: String) extends MbRunnableCommand with Account {

	override def run(mbSession: MbSession)(implicit ctx: CatalogSession): Seq[Row] = {
		mbSession.catalog.renameGroup(ctx.organizationId, ctx.organizationName, name, newName, ctx.userId)
		Seq.empty[Row]
	}
}

case class AlterGroupSetComment(
	name: String,
	comment: String) extends MbRunnableCommand with Account {

	override def run(mbSession: MbSession)(implicit ctx: CatalogSession): Seq[Row] = {
		val existGroup: CatalogGroup = mbSession.catalog.getGroup(ctx.organizationId, name)
		mbSession.catalog.alterGroup(
			existGroup.copy(
				description = Some(comment),
				updateBy = ctx.userId,
				updateTime = Utils.now
			)
		)
		Seq.empty[Row]
	}
}

case class AlterGroupSetUser(
	name: String,
	addUsers: Seq[String] = Seq(),
	removeUsers: Seq[String] = Seq(),
	addFirst: Boolean = true) extends MbRunnableCommand with Account {

	override def run(mbSession: MbSession)(implicit ctx: CatalogSession): Seq[Row] = {
		val groupId: Long = mbSession.catalog.getGroup(ctx.organizationId, name).id.get

		def addUsersToGroup() = {
			val users: Seq[CatalogUser] = mbSession.catalog.getUsers(ctx.organizationId, addUsers)
			require(addUsers.size == users.size, s"User does not exist: '${addUsers.diff(users.map(_.name)).mkString(", ")}' ")

			val catalogUserGroupRels = users.map { user =>
				CatalogUserGroupRel(
					groupId = groupId,
					userId = user.id.get,
					createBy = ctx.userId,
					updateBy = ctx.userId
				)
			}
			mbSession.catalog.createUserGroupRel(catalogUserGroupRels, ctx.organizationName, name, addUsers)
		}

		def removeUsersFromGroup() = {
			val users: Seq[CatalogUser] = mbSession.catalog.getUsers(ctx.organizationId, removeUsers)
			require(removeUsers.size == users.size, s"User does not exist: '${removeUsers.diff(users.map(_.name)).mkString(", ")}' ")
			mbSession.catalog.dropUserGroupRel(groupId, users.map(_.id.get), ctx.organizationName, name, removeUsers)
		}

		if (addFirst) {
			if (addUsers.nonEmpty) {
				addUsersToGroup()
			}
			if (removeUsers.nonEmpty) {
				removeUsersFromGroup()
			}
		} else {
			if (removeUsers.nonEmpty) {
				removeUsersFromGroup()
			}
			if (addUsers.nonEmpty) {
				addUsersToGroup()
			}
		}
		Seq.empty[Row]
	}
}

case class DropGroup(
	name: String,
	ignoreIfNotExists: Boolean,
	cascade: Boolean) extends MbRunnableCommand with Account {

	override def run(mbSession: MbSession)(implicit ctx: CatalogSession): Seq[Row] = {
		mbSession.catalog.dropGroup(ctx.organizationId, ctx.organizationName, name, ignoreIfNotExists, cascade)
		Seq.empty[Row]
	}
}

