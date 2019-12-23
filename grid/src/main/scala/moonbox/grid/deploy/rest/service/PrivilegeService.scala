package moonbox.grid.deploy.rest.service

import moonbox.catalog.AbstractCatalog.User
import moonbox.catalog._
import moonbox.common.MbLogging
import moonbox.grid.deploy.rest.entities._
import moonbox.grid.deploy.rest.routes.SessionConverter
import moonbox.grid.deploy.rest.service.DateFormatUtils.formatDate

import scala.collection.mutable.ListBuffer
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class PrivilegeService(catalog: JdbcCatalog) extends SessionConverter with MbLogging {

  def listRolePrivileges()(implicit user: User): Future[Either[Seq[RolePrivilege], Throwable]] = {
    try {
      Future(Left(catalog.listUsers(user.org)
        .map(catalogUser =>
          RolePrivilege(user = catalogUser.name,
            isSa = catalogUser.isSA,
            privileges = getRolePrivileges(catalogUser),
            createBy = catalogUser.createBy.get,
            createTime = catalogUser.createTime.map(formatDate).get))
        .sortBy(_.createTime).reverse))
    } catch {
      case e: Throwable => Future(Right(e))
    }
  }

  def listDatabasePrivileges()(implicit user: User): Future[Either[Seq[CatalogDatabasePrivilege], Throwable]] = {
    try {
      Future(Left(catalog.listDatabasePrivileges()))
    } catch {
      case e: Throwable => Future(Right(e))
    }
  }

  def listTablePrivileges()(implicit user: User): Future[Either[Seq[CatalogTablePrivilege], Throwable]] = {
    try {
      Future(Left(catalog.listTablePrivileges()))
    } catch {
      case e: Throwable => Future(Right(e))
    }
  }

  def listColumnPrivileges()(implicit user: User): Future[Either[Seq[CatalogColumnPrivilege], Throwable]] = {
    try {
      Future(Left(catalog.listColumnPrivileges()))
    } catch {
      case e: Throwable => Future(Right(e))
    }
  }

  private def getRolePrivileges(user: CatalogUser) = {
    val privilegeSeq = new ListBuffer[String]

    if (user.grantAccount) privilegeSeq.append("grantAccount")

    if (user.grantDcl) privilegeSeq.append("grantDcl")

    if (user.grantDdl) privilegeSeq.append("grantDdl")

    if (user.account) privilegeSeq.append("account")

    if (user.dcl) privilegeSeq.append("dcl")

    if (user.ddl) privilegeSeq.append("ddl")

    privilegeSeq.mkString(",")
  }
}
