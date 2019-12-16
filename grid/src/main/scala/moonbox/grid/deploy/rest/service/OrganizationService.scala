package moonbox.grid.deploy.rest.service

import moonbox.catalog.AbstractCatalog.User
import moonbox.catalog.{CatalogOrganization, JdbcCatalog}
import moonbox.common.MbLogging
import moonbox.grid.deploy.rest.entities.Organization
import moonbox.grid.deploy.rest.routes.SessionConverter

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class OrganizationService(catalog: JdbcCatalog) extends SessionConverter with MbLogging {

  def createOrg(org: Organization)(implicit user: User): Future[Unit] = {
    Future {
      catalog.createOrganization(
        CatalogOrganization(
          name = org.name,
          config = org.config,
          description = org.comment
        ), false
      )
    }
  }

  def updateOrg(org: Organization)(implicit user: User): Future[Unit] = {
    Future {
      catalog.alterOrganization(
        CatalogOrganization(
          name = org.name,
          config = org.config,
          description = org.comment
        )
      )
    }
  }

  def deleteOrgCascade(org: String)(implicit user: User): Future[Unit] = {
    Future {
      catalog.dropOrganization(org, ignoreIfNotExists = false, cascade = true)
    }
  }

  def getOrg(org: String)(implicit user: User): Future[CatalogOrganization] = {
    Future {
      catalog.getOrganization(org)
    }
  }

  def listOrgs()(implicit user: User): Future[Seq[CatalogOrganization]] = {
    Future {
      catalog.listOrganizations()
    }
  }
}
