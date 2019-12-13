package moonbox.grid.deploy.rest.service

import moonbox.catalog.AbstractCatalog.User
import moonbox.catalog.{CatalogCluster, JdbcCatalog}
import moonbox.common.MbLogging
import moonbox.grid.deploy.rest.entities.Cluster
import moonbox.grid.deploy.rest.routes.SessionConverter
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class ClusterService(catalog: JdbcCatalog) extends SessionConverter with MbLogging {

	def createCluster(cluster: Cluster)(implicit user: User): Future[Unit] = {
		Future {
			catalog.createCluster(
				CatalogCluster(
					name = cluster.name,
					`type` = cluster.`type`,
					environment = cluster.environment,
					config = cluster.config
				)
			)
		}
	}

	def updateCluster(cluster: Cluster)(implicit user: User): Future[Unit] = {
		Future {
			catalog.createCluster(
				CatalogCluster(
					name = cluster.name,
					`type` = cluster.`type`,
					environment = cluster.environment,
					config = cluster.config
				)
			)
		}
	}

	def deleteCluster(cluster: String)(implicit user: User): Future[Unit] = {
		Future {
			catalog.dropCluster(cluster, ignoreIfNotExists = false)
		}
	}

	def listClusters()(implicit user: User): Future[Seq[Cluster]] = {
		Future {
			Seq.empty
		}
	}
}
