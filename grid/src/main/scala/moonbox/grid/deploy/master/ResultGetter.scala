package moonbox.grid.deploy.master

import akka.actor.Actor
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
        val dat = redis.get[String, Any, Seq[Any]](jobId, offset, offset + size - 1).toSeq
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
