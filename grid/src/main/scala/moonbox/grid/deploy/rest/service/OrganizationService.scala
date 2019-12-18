package moonbox.grid.deploy.rest.service

import moonbox.catalog.AbstractCatalog.User
import moonbox.catalog.{CatalogOrganization, JdbcCatalog}
import moonbox.common.MbLogging
import moonbox.grid.deploy.rest.entities._
import moonbox.grid.deploy.rest.routes.SessionConverter

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class OrganizationService(catalog: JdbcCatalog) extends SessionConverter with MbLogging {

  def createOrg(org: Organization)(implicit user: User): Future[Unit] = {
    Future {
      checkProps(org.config)
      catalog.createOrganization(
        CatalogOrganization(
          name = org.name,
          config = org.config,
          description = org.description
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
          description = org.description
        )
      )
    }
  }

  def deleteOrgsCascade(batchOp: BatchOpSeq)(implicit user: User): Future[Unit] = {
    Future {
      batchOp.names.foreach(org => catalog.dropOrganization(org, ignoreIfNotExists = false, cascade = true))
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
        .filter(_.name != "SYSTEM")
        .sortBy(_.updateTime.get.toString).reverse
    }
  }

  private def checkProps(props: Map[String, String]): Unit = {
    require(props.contains("total.memory"), "config must have total.memory key")
    require(props.contains("total.cores"), "config must have total.cores key")
    require(props.contains("spark.sql.permission"), "config must have spark.sql.permission key")
  }
}
