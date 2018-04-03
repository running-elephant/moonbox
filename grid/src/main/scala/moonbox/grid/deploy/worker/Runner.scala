package moonbox.grid.deploy.worker

import akka.actor.{Actor, ActorRef, PoisonPill}
import moonbox.common.{MbConf, MbLogging}
import moonbox.core.cache.RedisCache
import moonbox.core.command.{InsertInto, MQLQuery, MbCommand}
import moonbox.core.config.CACHE_SERVERS
import moonbox.core.{MbSession, MbTableIdentifier}
import moonbox.grid._
import moonbox.grid.deploy.DeployMessages.{AssignJobToWorker, JobStateChanged, RemoveJobFromWorker}
import org.apache.spark.sql.{DataFrame, SparkSession}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

class Runner(conf: MbConf, session: MbSession) extends Actor with MbLogging {
	override def receive: Receive = {
		case AssignJobToWorker(jobInfo) =>
			val target = sender()
			run(jobInfo).onComplete {
				case Success(size) =>
					successCallback(jobInfo, size, target)
				case Failure(e) =>
					failureCallback(jobInfo, e, target)
			}
		case RemoveJobFromWorker(jobId) =>
			Future {
				//TODO: cancel JOB in mbsession
				self ! PoisonPill
			}
	}


	private def run(jobInfo: JobInfo): Future[Long] = {
		Future {
			try {
				jobInfo.cmds.map {
					case InsertInto(MbTableIdentifier(table, database), query, overwrite) =>
						logDebug("Runner receive InsertInto")
						0
					case query: MQLQuery =>
						logDebug("Runner receive MQLQuery")
						//TODO: mbsession execute query
						//----here, debug only
						val spark: SparkSession = SparkSession.builder().master("local[*]").appName("test").getOrCreate()
						val values = List(1,2,3,4,5)
						import spark.implicits._
						val df = values.toDF()
						println(df.schema)
						df.show()
						redis(jobInfo.jobId, "127.0.0.1", 1100, df)
						//-----debug only
					case cmd: MbCommand =>
						logDebug("Runner recv1 MbCommand")
						0
				}.last
			} catch {
				case e: Exception =>
					try {
						jobInfo.cmds.map {
							case InsertInto(MbTableIdentifier(table, database), query, overwrite) =>
								logDebug("Runner receive2 InsertInto in exception")
								0
							case query: MQLQuery =>
								logDebug("Runner receive2 MQLQuery in exception")
								0
							case cmd: MbCommand =>
								logDebug("Runner receive2 MbCommand in exception")
								-1L
						}.last
					} catch {
						case e: Exception =>
							throw e
					}
			}
		}
	}

	//debug only
	def redis(key: String, host: String, port: Int, df: DataFrame): Long = {
		val server = conf.get(CACHE_SERVERS)
		val jedis = new RedisCache(server)
		println(s"write redis for key=${key}")
		val schema = df.schema.json
		jedis.put("SCHEMA", key, schema)  //save schema
		df.foreachPartition { part =>
			val j = new RedisCache(server)
			part.foreach { row =>
				j.put(key, row.toSeq)
			}
			j.close
		}
		val count = jedis.size(key)
		jedis.close
		count
	}

	private def successCallback(jobInfo: JobInfo, size: Long, sender: ActorRef): Unit = {
		logInfo(s"send job complete report to sender $sender")
		sender ! JobStateChanged(
			jobInfo.jobId,
			JobState.SUCCESS,
			CachedData(jobInfo.jobId)  //TODO: fill different return data type
		)

		if(jobInfo.sessionId.isEmpty) {  //BATCH JOB
			self ! PoisonPill
		}
	}

	private def failureCallback(jobInfo: JobInfo, e: Throwable, sender: ActorRef): Unit = {
		logInfo(s"send job failed report to sender ${jobInfo}")
		e.printStackTrace()
		sender ! JobStateChanged(
			jobInfo.jobId,
			JobState.FAILED,
			ExternalData(jobInfo.jobId)  //TODO: fill different return data type
		)

		if (jobInfo.sessionId.isEmpty) {   //is BATCH

			self ! PoisonPill
		}
	}


}
