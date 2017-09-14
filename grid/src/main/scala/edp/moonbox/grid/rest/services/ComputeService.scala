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

import akka.actor.ActorRef
import akka.pattern._
import akka.util.Timeout
import scala.concurrent.{ExecutionContext, Future}
import scala.reflect.ClassTag


class ComputeService(val computer: ActorRef,
                     implicit val executionContext: ExecutionContext,
                     implicit val timeout: Timeout) {
	def service[T: ClassTag](message: T): Future[T] = {
	    (computer ask message).mapTo[T]
    }
}
