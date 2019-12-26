package moonbox.grid.deploy.rest.service

import moonbox.catalog.AbstractCatalog.User
import moonbox.catalog.{CatalogOrganization, JdbcCatalog}
import moonbox.common.MbLogging
import moonbox.grid.deploy.rest.entities.{BatchOpSeq, Organization}
import moonbox.grid.deploy.rest.routes.SessionConverter

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class OrganizationService(catalog: JdbcCatalog) extends SessionConverter with MbLogging {
  def createOrganization(org: Organization)(implicit user: User): Future[Either[Unit, Throwable]] = {
    try {
      catalog.createOrganization(
        CatalogOrganization(
          name = org.name,
          config = org.config,
          description = org.description
        ), ignoreIfExists = false
      )
      Future(Left(Unit))
    } catch {
      case e: Throwable => Future(Right(e))
    }
  }

  def updateOrganization(org: Organization)(implicit user: User): Future[Either[Unit, Throwable]] = {
    try {
      catalog.alterOrganization(
        CatalogOrganization(
          name = org.name,
          config = org.config,
          description = org.description
        )
      )
      Future(Left(Unit))
    } catch {
      case e: Throwable => Future(Right(e))
    }
  }

  def getOrganization(name: String)(implicit user: User): Future[Either[Organization, Throwable]] = {
    try {
      val org = catalog.getOrganization(name)
      Future(Left(
        Organization(
          name = org.name,
          config = org.config,
          description = org.description
        )
      ))
    } catch {
      case e: Exception => Future(Right(e))
    }
  }

  def deleteOrganization(org: String)(implicit user: User): Future[Either[Unit, Throwable]] = Future {
    try {
      catalog.dropOrganization(org, ignoreIfNotExists = false, cascade = true)
      Left(Unit)
    } catch {
      case e: Throwable => Right(e)
    }
  }

  def deleteOrganizations(deleteOps: BatchOpSeq)(implicit user: User): Future[Either[Unit, Throwable]] = Future {
    try {
      deleteOps.names.foreach(org => catalog.dropOrganization(org, ignoreIfNotExists = true, cascade = deleteOps.cascade.getOrElse(false)))
      Left(Unit)
    } catch {
      case e: Throwable => Right(e)
    }
  }

  def listOrganizations(): Future[Either[Seq[Organization], Throwable]] = {
    try {
      val orgs = catalog.listOrganizations().map { org =>
        Organization(
          name = org.name,
          config = org.config,
          description = org.description
        )
      }
      Future(Left(orgs))
    } catch {
      case e: Throwable => Future(Right(e))
    }
  }
}
