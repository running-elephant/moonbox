package moonbox.grid.deploy.rest.routes

import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.server.Directives
import akka.stream.ActorMaterializer
import moonbox.catalog.JdbcCatalog
import moonbox.common.MbConf
import moonbox.grid.deploy.rest.service._
import moonbox.grid.deploy.rest.service.workbench._
import moonbox.grid.deploy.security.LoginManager

class AssembleRoutes(
                      conf: MbConf,
                      jdbcCatalog: JdbcCatalog,
                      actor: ActorRef
                    )(
                      implicit val actorSystem: ActorSystem,
                      implicit val materializer: ActorMaterializer) extends Directives {

  private val loginService = new LoginService(new LoginManager(conf, jdbcCatalog))

  lazy val routes = {
    new SwaggerRoute().route ~
      pathPrefix("api" / "v1") {
        new LoginRoute(loginService).route ~
          new LogoutRoute().route ~
          pathPrefix("grids") {
            new GridRoute(loginService, new GridService(actor)).route
          } ~
          pathPrefix("clusters") {
            new ClusterRoute(loginService, new ClusterService(jdbcCatalog)).route
          } ~
          pathPrefix("applications") {
            new ApplicationRoute(loginService, new ApplicationService(jdbcCatalog, actor)).route
          } ~
          pathPrefix("workbench") {
            new WorkbenchRoute(loginService, new WorkbenchService(actor, jdbcCatalog)).route
          } ~
          pathPrefix("organizations") {
            new OrganizationRoute(loginService, new OrganizationService(jdbcCatalog)).route
          } ~
          pathPrefix("sas") {
            new OrganizationSaRoute(loginService, new OrganizationSaService(jdbcCatalog)).route
          } ~
          pathPrefix("privileges") {
            new PrivilegeRoute(loginService, new PrivilegeService(jdbcCatalog)).route
          } ~
          pathPrefix("logs") {
            new LogRoute(loginService, new LogService(conf)).route
          } ~
          pathPrefix("stats") {
            new StatsRoute(loginService, new StatsService(jdbcCatalog, actor)).route
          }
      }
  }

}
