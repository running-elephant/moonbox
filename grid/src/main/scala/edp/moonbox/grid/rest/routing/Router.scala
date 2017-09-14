/*-
 * <<
 * Moonbox
 * ==
 * Copyright (C) 2016 - 2017 EDP
 * ==
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * >>
 */

package edp.moonbox.grid.rest.routing

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import de.heikoseeberger.akkahttpjson4s.Json4sSupport.ShouldWritePretty
import edp.moonbox.grid.rest.entities._
import edp.moonbox.grid.rest.serializers.JsonSupport
import edp.moonbox.grid.rest.services.ServiceInterface
import org.json4s.{DefaultFormats, Formats, jackson}


trait Router extends JsonSupport { self: ServiceInterface =>
    implicit val json4sJacksonFormats: Formats = DefaultFormats
    implicit val serialization = jackson.Serialization
    implicit val shouldWritePretty = ShouldWritePretty.True

    val routes: Route =
        pathPrefix("sql") {
            pathPrefix("batch") {
                  path("query")(post(entity(as[SqlQuery])(query => complete(sqlQuery(query))))) ~
                  path("progress")(post(entity(as[SqlProgress])(progress => complete(sqlProgress(progress))))) ~
                  path("result")(post(entity(as[SqlResult])(result => complete(sqlResult(result)))))
            } ~
            pathPrefix("adhoc") {
                path("open")(post(complete(sqlOpenSession(SqlOpenSession)))) ~
                path("query")(post(entity(as[SqlQuery])(query => complete(sqlQuery(query))))) ~
                path("progress")(post (entity(as[SqlProgress])(progress => complete(sqlProgress(progress))))) ~
                path("result")(post (entity(as[SqlResult])(result => complete(sqlResult(result))))) ~
                path("cancel")(post (entity(as[SqlCancel])(result => complete(sqlCancel(result))))) ~
                path("close")(post (entity(as[SqlCloseSession])(close => complete(sqlCloseSession(close)) )))
              }
        }
}
