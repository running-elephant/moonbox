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

package edp.moonbox.grid.rest.services


import edp.moonbox.grid.rest.entities._

import scala.concurrent.Future


trait ServiceInterface {

	def sqlQuery(query: SqlQuery): Future[Response]

	def sqlProgress(progress: SqlProgress): Future[Response]

	def sqlResult(result: SqlResult): Future[Response]

	def sqlOpenSession(open: SqlOpenSession.type): Future[Response]

	def sqlCancel(cancel: SqlCancel): Future[Response]

	def sqlCloseSession(close: SqlCloseSession): Future[Response]

}
