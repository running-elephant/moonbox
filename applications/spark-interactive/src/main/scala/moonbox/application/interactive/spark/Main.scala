/*-
 * <<
 * Moonbox
 * ==
 * Copyright (C) 2016 - 2019 EDP
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

package moonbox.application.interactive.spark

import java.util.UUID
import java.util.concurrent.{ExecutionException, Executors}

import akka.actor.{ActorRef, ActorSystem, Address, Cancellable, Props}
import com.typesafe.config.ConfigFactory
import moonbox.common.util.Utils
import moonbox.common.{MbConf, MbLogging}
import moonbox.grid.config._
import moonbox.grid.deploy.DeployMessages._
import moonbox.grid.deploy.master.ApplicationType
import moonbox.grid.deploy.messages.Message._
import moonbox.grid.{LogMessage, MbActor}
import moonbox.protocol.util.SchemaUtil
import org.apache.spark.sql.SparkEngine

import scala.collection.JavaConverters._
import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

object Main extends MbLogging {

	def main(args: Array[String]) {
		val conf = new MbConf()
		val keyValues = for (i <- 0 until(args.length, 2)) yield (args(i), args(i+1))
		keyValues.foreach { case (k, v) => conf.set(k, v) }

		val driverId = conf.get("driverId").getOrElse(throw new NoSuchElementException("driverId"))
		val masters = conf.get("masters").map(_.split(";")).getOrElse(throw new NoSuchElementException("masters"))
		val appType = conf.get("applicationType").getOrElse(throw new NoSuchElementException("applicationType"))
		val appLabel = conf.get("appLabel").getOrElse("common")


		conf.set("moonbox.rpc.akka.remote.netty.tcp.hostname", Utils.localHostName())
		conf.set("moonbox.rpc.akka.remote.netty.tcp.port", "0")

		def akkaConfig: Map[String, String] = {
			for { (key, value) <- AKKA_DEFAULT_CONFIG ++ AKKA_HTTP_DEFAULT_CONFIG ++ conf.getAll
						if key.startsWith("moonbox.rpc.akka") || key.startsWith("moonbox.rest.akka")
			} yield {
				if (key.startsWith("moonbox.rpc.akka"))
					(key.stripPrefix("moonbox.rpc."), value)
				else
					(key.stripPrefix("moonbox.rest."), value)
			}
		}

		val akkaConf = ConfigFactory.parseMap(akkaConfig.asJava)
		val system = ActorSystem("Moonbox", akkaConf)

		try {
			system.actorOf(Props(
				classOf[Main], driverId, appLabel, masters, conf, ApplicationType.apply(appType)
			), name = "interactive")
		} catch {
			case e: Exception =>
				logError(e.getMessage)
				System.exit(1)
		}
		Thread.currentThread().join()
	}
}

class Main(
	driverId: String,
	appLabel: String,
	masterAddresses: Array[String],
	val conf: MbConf,
	appType: ApplicationType
) extends MbActor with LogMessage with MbLogging {

	private implicit val executionContext: ExecutionContext = {
		ExecutionContext.fromExecutorService(Executors.newCachedThreadPool())
	}
	private var master: Option[ActorRef] = None
	private var masterAddressToConnect: Option[Address] = None
	private var registered = false
	private var connected = false
	private var connectionAttemptCount = 0

	private val sessionIdToRunner = new scala.collection.mutable.HashMap[String, Runner]

	private var registerToMasterScheduler: Option[Cancellable] = None

	private var dataFetchServer: Option[DataFetchServer] = None
	private var dataFetchPort: Int = _

	@scala.throws[Exception](classOf[Exception])
	override def preStart(): Unit = {
		assert(!registered)
		logInfo("Running Moonbox Interactive Application ")
		logInfo(s"Starting Main at ${self.path.toSerializationFormatWithAddress(address)}")

		try {
			startComputeEnv()
		} catch {
			case e: Exception =>
				logError("Error caught ", e)
				gracefullyShutdown()
		}

		try {
			dataFetchServer = Some(new DataFetchServer(host, 0, conf, sessionIdToRunner))
			dataFetchPort = dataFetchServer.map(_.start()).get
		} catch {
			case e: Exception =>
				logError("Could not start data fetch server.", e)
				gracefullyShutdown()
		}

		registerWithMaster()
	}

	override def handleMessage: Receive = {
		case msg: RegisterApplicationResponse =>
			handleRegisterResponse(msg)

		case MasterChanged(masterRef) =>
			logInfo(s"Master has changed. new master is at ${masterRef.path.address}")
			changeMaster(masterRef, masterRef.path.address)
			masterRef ! ApplicationStateResponse(driverId)

		case open @ OpenSession(org, username, database, sessionConfig) =>
			val requester = sender()
			val sessionId = newSessionId
			val f = Future(new Runner(sessionId, org, username, database, conf, sessionConfig, self))
			f.onComplete {
				case Success(runner) =>
					sessionIdToRunner.put(sessionId, runner)
					logInfo(s"Open session successfully for $username, session id is $sessionId, current database set to ${database.getOrElse("default")} ")
					logInfo(s"Current ${sessionIdToRunner.size} users online.")
					requester ! OpenSessionResponse(Some(sessionId), Some(host), Some(dataFetchPort), "Open session successfully.")
				case Failure(e) =>
					logError("open session error", e)
					requester ! OpenSessionResponse(None, None, None, e.getMessage)
			}

		case close @ CloseSession(sessionId) =>
			sessionIdToRunner.get(sessionId) match {
				case Some(runner) =>
					sessionIdToRunner.remove(sessionId).foreach { r =>
						r.cancel()
					}
					val msg = s"Close session successfully $sessionId"
					logInfo(msg)
					logInfo(s"Current ${sessionIdToRunner.size} users online.")
					sender() ! CloseSessionResponse(sessionId, success = true, msg)
				case None =>
					val msg = s"Your session id $sessionId  is incorrect. Or it is lost in runner."
					logWarning(msg)
					sender() ! CloseSessionResponse(sessionId, success = false, msg)
			}

		case query @ JobQuery(sessionId, sqls, fetchSize, maxRows) =>
			val requester = sender()
			sessionIdToRunner.get(sessionId) match {
				case Some(runner) =>
					val f = Future(runner.query(sqls, fetchSize, maxRows))
					f.onComplete {
						case Success(result) =>
							result match  {
								case DirectResult(schema, data) =>
									requester ! JobQueryResponse(success = true, schema = schema, data = data, hasNext = false, message = "")
								case IndirectResult(schema) =>
									requester ! JobQueryResponse(success = true, schema = schema, data = Seq.empty, hasNext = true, message = "")
							}
						case Failure(throwable) =>
							val errorMessage = throwable match {
								case ee : ExecutionException =>
									ee.getCause.getMessage
								case error =>
									Option(error.getMessage).getOrElse(error.getStackTrace.mkString("\n"))
							}
							logError("Query execute error", throwable)
							logError(throwable.getStackTrace.mkString("\n"))
							requester ! JobQueryResponse(
								success = false,
								schema = SchemaUtil.emptyJsonSchema,
								data = Seq.empty,
								hasNext = false,
								message = errorMessage
							)
					}

				case None =>
					val msg = s"Your session id $sessionId  is incorrect. Or it is lost in runner."
					logWarning(msg)
					requester ! JobQueryResponse(
						success = false,
						schema = SchemaUtil.emptyJsonSchema,
						data = Seq.empty,
						hasNext = false,
						message = msg
					)
			}

		case cancel @ InteractiveJobCancel(sessionId) =>
			sessionIdToRunner.get(sessionId) match {
				case Some(runner) =>
					runner.cancel()
					sender() ! InteractiveJobCancelResponse(success = true, s"Job canceled.")
				case None =>
					val msg = s"Your session id $sessionId  is incorrect. Or it is lost in runner."
					logWarning(msg)
					sender() ! InteractiveJobCancelResponse(success = false, msg)
			}

		case event: RegisterTimedEvent =>
			if (master.isDefined) {
				master.foreach(_ forward event)
			} else {
				val message = s"Enable TimedEvent ${event.event.name} failed. Don't know master's address."
				logWarning(message)
				sender() ! RegisterTimedEventFailed(message)
			}

		case event: UnregisterTimedEvent =>
			if (master.isDefined) {
				master.foreach(_ forward event)
			} else {
				val message = s"Disable TimedEvent ${event.name} failed. Don't know master's address."
				logWarning(message)
				sender() ! UnregisterTimedEventFailed(message)
			}

		case sample @ SampleRequest(org, username, sql, database) =>
			val requester = sender()
			Future {
				val servicer = new Servicer(org, username, database, conf, self)
				servicer.sample(sql)
			}.onComplete {
				case Success(response) =>
					requester ! response
				case Failure(e) =>
					logError("sample error", e)
					requester ! SampleFailed(e.getMessage)
			}

		case translate @ TranslateRequest(org, username, sql, database) =>
			val requester = sender()
			Future {
				val servicer = new Servicer(org, username, database, conf, self)
				servicer.translate(sql)
			}.onComplete {
				case Success(response) =>
					requester ! response
				case Failure(e) =>
					logError("translate error", e)
					requester ! SampleFailed(e.getMessage)
			}

		case verify @ VerifyRequest(org, username, sqls, database) =>
			val requester = sender()
			Future {
				val servicer = new Servicer(org, username, database, conf, self)
				servicer.verify(sqls)
			}.onComplete {
				case Success(response) =>
					requester ! response
				case Failure(e) =>
					logError("verify error", e)
					requester ! VerifyResponse(success = false, message = Some(e.getMessage))
			}

		case resource @ TableResourcesRequest(org, username, sqls, database) =>
			val requester = sender()
			Future {
				val servicer = new Servicer(org, username, database, conf, self)
				servicer.resources(sqls)
			}.onComplete {
				case Success(response) =>
					requester ! response
				case Failure(e) =>
					logError("table resource error", e)
					requester ! TableResourcesResponses(success=false, message = Some(e.getMessage))
			}

		case schema @ SchemaRequest(org, username, sql, database) =>
			val requester = sender()
			Future {
				val servicer = new Servicer(org, username, database, conf, self)
				servicer.schema(sql)
			}.onComplete {
				case Success(response) =>
					requester ! response
				case Failure(e) =>
					logError("scheme error", e)
					requester ! SchemaFailed(e.getMessage)
			}

		case lineage @ LineageRequest(org, username, sqls, database) =>
			val requester = sender()
			Future {
				val servicer = new Servicer(org, username, database, conf, self)
				servicer.lineage(sqls)
			}.onComplete {
				case Success(response) =>
					requester ! response
				case Failure(e) =>
					logError("lineage error", e)
					requester ! LineageFailed(e.getMessage)
			}

		case e =>
			logWarning(s"Unknown message received: $e")
	}

	private def newSessionId: String = {
		UUID.randomUUID().toString
	}

	private def startComputeEnv(): Unit = {
		SparkEngine.start(conf)
	}

	private def handleRegisterResponse(msg: RegisterApplicationResponse): Unit = {
		msg match {
			case RegisteredApplication(masterRef) =>
				logInfo("Successfully registered with master " + masterRef.path.address)
				registered = true
				changeMaster(masterRef, masterRef.path.address)
			case RegisterApplicationFailed(message) =>
				if (!registered) {
					logError("Interactive Application registration failed: " + message)
					gracefullyShutdown()
				}
			case MasterInStandby =>
			// do nothing
		}
	}

	private def changeMaster(masterRef: ActorRef, masterAddress: Address): Unit = {
		masterAddressToConnect = Some(masterAddress)
		master = Some(masterRef)
		connected = true
		cancelRegistrationScheduler()
	}


	private def registerWithMaster(): Unit = {
		registerToMasterScheduler match {
			case None =>
				registered = false
				connectionAttemptCount = 0
				registerToMasterScheduler = Some {
					context.system.scheduler.schedule(
						new FiniteDuration(1, SECONDS),
						new FiniteDuration(5, SECONDS))(
						tryRegisterAllMasters()
					)
				}
			case Some(_) =>
				logInfo("Don't attempt to register with the master, since there is an attempt scheduled already.")
		}
	}

	private def tryRegisterAllMasters(): Unit = {
		connectionAttemptCount += 1
		if (registered) {
			cancelRegistrationScheduler()
		} else if (connectionAttemptCount <= 15) {
			logInfo(s"Connecting to master (attempt $connectionAttemptCount)")
			masterAddresses.foreach(sendRegisterMessageToMaster)
		} else {
			logError("All masters are unresponsive! Giving up.")
			gracefullyShutdown()
		}
	}

	private def sendRegisterMessageToMaster(masterRpcAddress: String): Unit = {
		logInfo(s"Try registering with master $masterRpcAddress.")
		context.system.actorSelection(masterRpcAddress).tell(RegisterApplication(
			driverId,
			appLabel,
			host,
			port,
			self,
			address,
			dataFetchPort,
			appType
		), self)
	}

	private def cancelRegistrationScheduler(): Unit = {
		registerToMasterScheduler.foreach(_.cancel())
		registerToMasterScheduler = None
	}


	override def onDisconnected(remoteAddress: Address): Unit = {
		if (master.exists(_.path.address == remoteAddress) ||
			masterAddressToConnect.contains(remoteAddress)) {
			logInfo(s"$remoteAddress Disassociated!")
			masterDisconnected()
		}
	}

	private def masterDisconnected(): Unit = {
		logError("Connection to master failed! Waiting for master to reconnect ...")
		connected = false
		registerWithMaster()
	}
}
