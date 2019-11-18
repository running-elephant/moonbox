package moonbox.grid.deploy.rest.routes

import akka.http.scaladsl.model.HttpMethods._
import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.model.headers.{`Access-Control-Allow-Credentials`, `Access-Control-Allow-Headers`, `Access-Control-Allow-Methods`, `Access-Control-Allow-Origin`}
import akka.http.scaladsl.server._
import de.heikoseeberger.akkahttpjson4s.Json4sSupport
import moonbox.common.MbLogging
import moonbox.grid.deploy.rest.JsonSerializer

trait CrossDomainRoute  extends Directives with JsonSerializer with Json4sSupport with MbLogging {
	protected val requestHandler: Route = options(
		complete(HttpResponse(200).withHeaders(
			`Access-Control-Allow-Methods`(OPTIONS, POST, PUT, GET, DELETE))))

	protected val crossOrigin = mapResponseHeaders(responseHeaders => `Access-Control-Allow-Origin`.* +:
			`Access-Control-Allow-Credentials`(true) +:
			`Access-Control-Allow-Headers`("Authorization", "Content-Type", "X-Requested-With", "api_key") +:
			responseHeaders
	)

	def route = crossOrigin(requestHandler ~ createRoute)

	protected val createRoute: Route
}
