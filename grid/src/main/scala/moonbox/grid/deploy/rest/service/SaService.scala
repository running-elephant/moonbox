package moonbox.grid.deploy.rest.service

import moonbox.catalog.AbstractCatalog.User
import moonbox.catalog.{CatalogUser, JdbcCatalog, PasswordEncryptor}
import moonbox.common.MbLogging
import moonbox.grid.deploy.rest.entities._
import moonbox.grid.deploy.rest.routes.SessionConverter

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import DateFormatUtils.formatDate

class SaService(catalog: JdbcCatalog) extends SessionConverter with MbLogging {

  def createSa(sa: OrgSa)(implicit user: User): Future[Unit] = {
    Future {
      val catalogUser = CatalogUser(org = sa.org, name = sa.name, password = PasswordEncryptor.encryptSHA(sa.password.get))
      catalog.createUser(catalogUser, ignoreIfExists = false)
    }
  }

  def updateSa(sa: OrgSa)(implicit user: User): Future[Unit] = {
    Future {
      val catalogUser = CatalogUser(org = sa.org, name = sa.name, password = PasswordEncryptor.encryptSHA(sa.password.get))
      catalog.alterUser(catalogUser)
    }
  }

  def deleteSas(batchOp: BatchOpSaSeq)(implicit user: User): Future[Unit] = {
    Future {
      batchOp.sas.foreach(sa => catalog.dropUser(sa.org, sa.name, ignoreIfNotExists = false))
    }
  }

  def listSas()(implicit user: User): Future[Seq[OrgSaDetail]] = {
    Future {
      catalog.listSas()
        .map(user => OrgSaDetail(user.org, user.name, user.createTime.map(formatDate).get, user.updateTime.map(formatDate).get))
        .sortBy(_.updateTime.toString).reverse
    }
  }

  def getSa(org: String, sa: String)(implicit user: User): Future[OrgSaDetail] = {
    Future {
      val catalogUser = catalog.getUser(org, sa)
      OrgSaDetail(catalogUser.org, catalogUser.name, catalogUser.createTime.map(formatDate).get, catalogUser.updateTime.map(formatDate).get)
    }
  }
}
