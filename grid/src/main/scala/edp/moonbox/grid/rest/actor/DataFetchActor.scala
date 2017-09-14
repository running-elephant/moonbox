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

package edp.moonbox.grid.rest.actor

import akka.actor.Actor
import edp.moonbox.common.{EdpLogging, RedisCacheClient}
import edp.moonbox.grid.message.{FetchData, FetchDataResponse}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}


class DataFetchActor(host: String, port: Int) extends Actor with EdpLogging{
	val redis = new RedisCacheClient(Seq(host -> port))

    override def receive: Receive = {
        case  FetchData(jobId, offset, size) =>
					val sender_ = sender()
					logInfo(s"receive FetchData($jobId, $offset, $size)")
	        val dataFuture = Future {
		        redis.mapValue("SCHEMA", jobId) :: redis.listValues(jobId, offset, offset + size)
	        }
	        dataFuture.onComplete {
		        case Success(data) =>
							sender_ ! FetchDataResponse(jobId, Some(data), "")
		        case Failure(e) =>
							sender_ ! FetchDataResponse(jobId, None, e.getMessage)
	        }
    }

}
