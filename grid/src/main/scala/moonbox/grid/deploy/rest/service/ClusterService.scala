package moonbox.grid.deploy.rest.service

import moonbox.catalog.AbstractCatalog.User
import moonbox.catalog.{CatalogCluster, JdbcCatalog}
import moonbox.common.MbLogging
import moonbox.grid.deploy.rest.entities.{Cluster, ClusterTemplate}
import moonbox.grid.deploy.rest.routes.SessionConverter

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class ClusterService(catalog: JdbcCatalog) extends SessionConverter with MbLogging {

	def createCluster(cluster: Cluster)(implicit user: User): Future[Either[Unit, Throwable]] = {
		try {
			catalog.createCluster(
				CatalogCluster(
					name = cluster.name,
					`type` = cluster.`type`,
					environment = cluster.environment,
					config = cluster.config
				)
			)
			Future(Left(Unit))
		} catch {
			case e: Throwable => Future(Right(e))
		}
	}

	def updateCluster(cluster: Cluster)(implicit user: User): Future[Either[Unit, Throwable]] = {
		try {
			catalog.alterCluster(
				CatalogCluster(
					name = cluster.name,
					`type` = cluster.`type`,
					environment = cluster.environment,
					config = cluster.config
				)
			)
			Future(Left(Unit))
		} catch {
			case e: Throwable => Future(Right(e))
		}
	}

	def getCluster(name: String)(implicit user: User): Future[Either[Cluster, Throwable]] = {
		try {
			val cluster = catalog.getCluster(name)
			Future(Left(Cluster(
				name = cluster.name,
				`type` = cluster.`type`,
				environment = cluster.environment,
				config = cluster.config
			)))
		} catch {
			case e: Exception => Future(Right(e))
		}
	}

	def deleteCluster(cluster: String)(implicit user: User): Future[Either[Unit, Throwable]] = {
		try {
			val clusterInUse = catalog.applicationUsingClusterExists(cluster)
			if (!clusterInUse) {
				catalog.dropCluster(cluster, ignoreIfNotExists = false)
				Future(Left(Unit))
			} else {
				Future(Right(new RuntimeException(s"Cluster $cluster is referenced by some apps.")))
			}
		} catch  {
			case e: Throwable => Future(Right(e))
		}
	}

	def listClusters()(implicit user: User): Future[Either[Seq[Cluster], Throwable]] = {
		try {
			val clusters = catalog.listClusters().map { cluster =>
				Cluster(
					name = cluster.name,
					`type` = cluster.`type`,
					environment = cluster.environment,
					config = cluster.config
				)
			}
			Future(Left(clusters))
		} catch {
			case e: Throwable => Future(Right(e))
		}
	}

	def getClusterTemplates()(implicit user: User): Future[Either[Seq[ClusterTemplate], Throwable]] = Future {
			Left(Seq())
	}

}
