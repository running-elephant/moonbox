package moonbox.core.command

import moonbox.common.util.Utils
import moonbox.core.MbSession
import moonbox.core.catalog.{CatalogOrganization, CatalogSession}
import org.apache.spark.sql.Row


trait Organization

case class CreateOrganization(
	name: String,
	comment: Option[String],
	ignoreIfExists: Boolean) extends MbRunnableCommand with Organization {

	override def run(mbSession: MbSession)(implicit ctx: CatalogSession): Seq[Row] = {
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

	override def run(mbSession: MbSession)(implicit ctx: CatalogSession): Seq[Row] = {
		mbSession.catalog.renameOrganization(name, newName, ctx.userId)
		ctx.organizationName = newName
		Seq.empty[Row]
	}
}

case class AlterOrganizationSetComment(
	name: String,
	comment: String) extends MbRunnableCommand with Organization {

	override def run(mbSession: MbSession)(implicit ctx: CatalogSession): Seq[Row] = {
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

	override def run(mbSession: MbSession)(implicit ctx: CatalogSession): Seq[Row] = {
		mbSession.catalog.dropOrganization(name, ignoreIfNotExists, cascade)
		Seq.empty[Row]
	}
}
