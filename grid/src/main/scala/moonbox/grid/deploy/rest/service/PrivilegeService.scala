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

  def listColumnPrivileges()(implicit user: User): Future[Either[Seq[ColumnPrivilege], Throwable]] = {
    try {
      val catalogColumnPrivileges = catalog.listColumnPrivileges()

      val columnPrivilegeSeq = new ListBuffer[ColumnPrivilege]

      if (catalogColumnPrivileges.nonEmpty) {
        var currentUser: String = catalogColumnPrivileges.head.user
        var currentDb: String = catalogColumnPrivileges.head.database
        var currentTable: String = catalogColumnPrivileges.head.table
        var currentPrivilegeType: String = catalogColumnPrivileges.head.privilegeType
        val columnSeq = new ListBuffer[String]
        for (privilege <- catalogColumnPrivileges) {
          if (privilege.user == currentUser && privilege.database == currentDb && privilege.table == currentTable && privilege.privilegeType == currentPrivilegeType) {
            columnSeq.append(privilege.column)
          } else {
            columnPrivilegeSeq.append(
              ColumnPrivilege(
                user = currentUser,
                database = currentDb,
                table = currentTable,
                columns = columnSeq.mkString(","),
                privilege = currentPrivilegeType)
            )

            currentUser = privilege.user
            currentDb = privilege.database
            currentTable = privilege.table
            currentPrivilegeType = privilege.privilegeType
            columnSeq.clear()
            columnSeq.append(privilege.column)
          }
        }

        columnPrivilegeSeq.append(
          ColumnPrivilege(
            user = currentUser,
            database = currentDb,
            table = currentTable,
            columns = columnSeq.mkString(","),
            privilege = currentPrivilegeType
          )
        )
      }

      Future(Left(columnPrivilegeSeq))
    } catch {
      case e: Throwable => Future(Right(e))
    }
  }

  private def getRolePrivileges(user: CatalogUser) = {
    val privilegeSeq = new ListBuffer[String]

    if (user.grantAccount) privilegeSeq.append("GrantAccount")

    if (user.grantDcl) privilegeSeq.append("GrantDCL")

    if (user.grantDdl) privilegeSeq.append("GrantDDL")

    if (user.account) privilegeSeq.append("Account")

    if (user.dcl) privilegeSeq.append("DCL")

    if (user.ddl) privilegeSeq.append("DDL")

    privilegeSeq.mkString(",")
  }
}
