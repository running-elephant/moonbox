package moonbox.grid.deploy.rest.service

import java.util.concurrent.TimeUnit

import akka.actor.ActorRef
import akka.pattern.ask
import akka.util.Timeout
import moonbox.catalog.AbstractCatalog.User
import moonbox.catalog._
import moonbox.common.MbLogging
import moonbox.grid.deploy.DeployMessages.{AllDriverStatusResponse, RequestAllDriverStatus}
import moonbox.grid.deploy.app.DriverState
import moonbox.grid.deploy.rest.ApplicationResourceComputer
import moonbox.grid.deploy.rest.entities.{ApplicationResourceStat, OrgResourceStatInfo, OrgResourceStatSummary, ResourceStat}
import moonbox.grid.deploy.rest.routes.SessionConverter
import moonbox.network.util.JavaUtils

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class StatsService(catalog: JdbcCatalog, actorRef: ActorRef) extends SessionConverter with MbLogging {

  // org resource params
  private val TOTAL_MEMORY_KEY = "total.memory"
  private val TOTAL_CORES_KEY = "total.cores"
  private val MAX_TOTAL_MEMORY = Int.MaxValue.toString
  private val MAX_TOTAL_CORES = Int.MaxValue.toString

  private implicit val timeout = new Timeout(30, TimeUnit.SECONDS)

  def listResourceStats()(implicit user: User): Future[Either[Seq[ResourceStat], Throwable]] = {
    try {
      var orgs: Seq[CatalogOrganization] = Seq.empty
      var catalogApps: Seq[CatalogApplication] = Seq.empty
      var runningAppNames: Seq[String] = Seq.empty

      val patternOpt =
        if (user.org == "SYSTEM" && user.user == "ROOT") None
        else Some(user.org)

      patternOpt match {
        case Some(pattern) =>
          orgs = catalog.listOrganizations(pattern)
          catalogApps = catalog.listApplications(pattern)
        case None =>
          orgs = catalog.listOrganizations()
          catalogApps = catalog.listApplications()
      }

      actorRef.ask(RequestAllDriverStatus(patternOpt)).mapTo[AllDriverStatusResponse].map(response => {
        response.exception match {
          case Some(e) => return Future(Right(e))
          case None =>
            runningAppNames = response.driverStatus
              .filter(driver => driver.state.nonEmpty && Seq(DriverState.RUNNING, DriverState.UNKNOWN).contains(driver.state.get)).
              map(_.driverId)
        }
      })

      val appResourceStats = catalogApps.filter(app => runningAppNames.contains(app.org + "_" + app.name))
        .map(app => new ApplicationResourceComputer(app).getAppResourceStat)

      val resourceStats = patternOpt match {
        case Some(_) =>
          orgs.map(org => {
            val appStats = appResourceStats.filter(_.org == org.name)
            val summaryStats = getOrgResourceStatSummary(org, appStats)
            OrgResourceStatInfo(summaryStats, appStats)
          })
        case None =>
          orgs.map(org => getOrgResourceStatSummary(org, appResourceStats.filter(_.org == org.name)))
      }

      Future(Left(resourceStats))
    } catch {
      case e: Throwable => Future(Right(e))
    }
  }

  private def getOrgResourceStatSummary(org: CatalogOrganization, appsResource: Seq[ApplicationResourceStat]) = {
    val totalMemory = org.config.getOrElse(TOTAL_MEMORY_KEY, MAX_TOTAL_MEMORY).toLong + "G"
    val totalCores = org.config.getOrElse(TOTAL_CORES_KEY, MAX_TOTAL_CORES).toInt
    val usedMemory = appsResource.map(app => JavaUtils.byteStringAsGb(app.usedMemory)).sum + "G"
    val usedCores = appsResource.map(_.usedCores).sum
    OrgResourceStatSummary(
      org = org.name,
      usedMemory = usedMemory,
      usedCores = usedCores,
      totalMemory = totalMemory,
      totalCores = totalCores)
  }

}



