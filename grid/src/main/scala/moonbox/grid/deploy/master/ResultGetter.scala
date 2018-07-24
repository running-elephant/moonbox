/*-
 * <<
 * Moonbox
 * ==
 * Copyright (C) 2016 - 2018 EDP
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

package moonbox.grid.deploy.master

import akka.actor.Actor
import moonbox.common.util.Utils
import moonbox.common.{MbConf, MbLogging}
import moonbox.core.cache.RedisCache
import moonbox.grid.api._
import moonbox.core.config._

import scala.concurrent.Future
import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global

class ResultGetter(conf: MbConf) extends Actor with MbLogging {
  override def receive: Receive = {
    case FetchData(jobId, offset, size) =>
      val sender_ = sender()
      logInfo(s"receive FetchData($jobId, $offset, $size)")
      val dataFuture = Future {
        val redis = new RedisCache(conf.get(CACHE_SERVERS))
        val totalSize = redis.size(jobId)
        val schema = redis.get[String, String, String]("SCHEMA", jobId)
        val end = if(size < 0) { size }  // -1 no need to change
        else { offset + size - 1  }
        val dat = redis.getRange[String,Seq[Any]](jobId, offset, end )
        (totalSize, schema, dat)
      }
      dataFuture.onComplete {
        case Success(data) =>
          val size = data._1
          val schema = data._2
          val dat = data._3
          sender_ ! FetchDataSuccess(jobId, schema, dat, size)
        case Failure(e) =>
          sender_ ! FetchDataFailed(jobId, e.getMessage)
      }
    case a: Any =>
      println("recv UNKNOWN message")
  }
}
