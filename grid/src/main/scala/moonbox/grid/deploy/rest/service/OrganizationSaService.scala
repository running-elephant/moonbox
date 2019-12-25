package moonbox.grid.deploy.rest.service

import moonbox.catalog.AbstractCatalog.User
import moonbox.catalog.{CatalogUser, JdbcCatalog, PasswordEncryptor}
import moonbox.common.MbLogging
import moonbox.grid.deploy.rest.entities._
import moonbox.grid.deploy.rest.routes.SessionConverter
import moonbox.grid.deploy.rest.service.DateFormatUtils.formatDate

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class OrganizationSaService(catalog: JdbcCatalog) extends SessionConverter with MbLogging {

  def createSa(sa: OrgSa)(implicit user: User): Future[Either[Unit, Throwable]] = {
    try {
      val catalogUser = CatalogUser(org = sa.org, name = sa.name, password = PasswordEncryptor.encryptSHA(sa.password.get))
      catalog.createUser(catalogUser, ignoreIfExists = false)
      Future(Left(Unit))
    } catch {
      case e: Throwable => Future(Right(e))
    }
  }

  def updateName(orgSa: OrgSaRename)(implicit user: User): Future[Either[Unit, Throwable]] = {
    try {
      catalog.renameUser(orgSa.org, orgSa.user, orgSa.newUser)
      Future(Left(Unit))
    } catch {
      case e: Throwable => Future(Right(e))
    }
  }

  def updatePassword(sa: OrgSa)(implicit user: User): Future[Either[Unit, Throwable]] = {
    try {
      val catalogUser = CatalogUser(org = sa.org, name = sa.name, password = PasswordEncryptor.encryptSHA(sa.password.get))
      catalog.alterUser(catalogUser)
      Future(Left(Unit))
    } catch {
      case e: Throwable => Future(Right(e))
    }
  }

  def deleteSas(batchOp: BatchOpSaSeq)(implicit user: User): Future[Either[Unit, Throwable]] = {
    try {
      batchOp.sas.foreach(sa => catalog.dropUser(sa.org, sa.name, ignoreIfNotExists = false))
      Future(Left(Unit))
    } catch {
      case e: Throwable => Future(Right(e))
    }
  }

  def listSas()(implicit user: User): Future[Either[Seq[OrgSaDetail], Throwable]] = {
    try {
      Future(Left(
        catalog.listSas()
          .filter(_.name != "ROOT")
          .map(user => OrgSaDetail(user.org, user.name, user.createTime.map(formatDate).get, user.updateTime.map(formatDate).get))
          .sortBy(_.updateTime.toString).reverse
      ))
    } catch {
      case e: Throwable => Future(Right(e))
    }
  }

  def getSa(org: String, sa: String)(implicit user: User): Future[Either[OrgSaDetail, Throwable]] = {
    try {
      val catalogUser = catalog.getUser(org, sa)
      Future(Left(
        OrgSaDetail(catalogUser.org, catalogUser.name, catalogUser.createTime.map(formatDate).get, catalogUser.updateTime.map(formatDate).get)
      ))
    } catch {
      case e: Throwable => Future(Right(e))
    }
  }
}
