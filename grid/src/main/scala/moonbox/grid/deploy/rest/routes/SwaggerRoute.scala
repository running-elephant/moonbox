package moonbox.grid.deploy.rest.routes

import akka.actor.ActorSystem
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import com.github.swagger.akka.model.{Info, License}
import com.github.swagger.akka.{HasActorSystem, SwaggerHttpService}
import io.swagger.models.ExternalDocs
import io.swagger.models.auth.{ApiKeyAuthDefinition, In, SecuritySchemeDefinition}

import scala.reflect.runtime.universe._

class SwaggerRoute(
	implicit val actorSystem: ActorSystem,
	implicit val materializer: ActorMaterializer) extends CrossDomainRoute with SwaggerHttpService with HasActorSystem {

	override val apiTypes = Seq(
		typeOf[LoginRoute],
		typeOf[LogoutRoute],
		typeOf[ApplicationRoute],
		typeOf[GridRoute],
		typeOf[ClusterRoute],
		typeOf[WorkbenchRoute],
		typeOf[OrganizationRoute],
		typeOf[OrganizationSaRoute],
		typeOf[PrivilegeRoute],
		typeOf[LogRoute],
		typeOf[StatsRoute]
	)

	override val basePath: String = "api/v1"
	override val apiDocsPath: String = "api-docs"
	override val info: Info = Info(
		title = "Moonbox REST API",
		description =
			"""This is moonbox rest server. How to use:
				|1. execute login api.
				|2. copy the token in response header.
				|3. click the Authorize button.
				|4. fill in value text input with the token.""".stripMargin,
		version = "0.3.0-beta",
		license = Some(new License("Apache 2.0", "http://www.apache.org/licenses/LICENSE-2.0.html"))
	)

	override val externalDocs: Option[ExternalDocs] = Some(
		new ExternalDocs("Moonbox Document", "https://edp963.github.io/moonbox")
	)

	override val securitySchemeDefinitions: Map[String, SecuritySchemeDefinition] = Map(
		"Bearer" -> new ApiKeyAuthDefinition("Authorization", In.HEADER)
	)

	override protected lazy val createRoute: Route = {
		pathPrefix("swagger") {
			getFromResourceDirectory("swagger-ui-dist") ~
					pathSingleSlash(get(redirect("index.html", StatusCodes.PermanentRedirect)))
		} ~ routes
	}
}
