package moonbox.grid.deploy.rest.routes

import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.server.Directives
import akka.stream.ActorMaterializer
import moonbox.catalog.JdbcCatalog
import moonbox.common.MbConf
import moonbox.grid.deploy.rest.service.{ApplicationService, ClusterService, LoginService, WorkbenchService}
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
          pathPrefix("cluster") {
            new ClusterRoute(loginService, new ClusterService(jdbcCatalog)).route
          } ~
          pathPrefix("application") {
            new ApplicationRoute(loginService, new ApplicationService(jdbcCatalog)).route
          } ~
          pathPrefix("workbench") {
            new WorkbenchRoute(loginService, new WorkbenchService(actor, jdbcCatalog)).route
          }
      }
  }

}
