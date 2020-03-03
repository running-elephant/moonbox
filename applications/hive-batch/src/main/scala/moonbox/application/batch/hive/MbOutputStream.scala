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

package moonbox.application.batch.hive

import java.io.OutputStream
import java.nio.charset.Charset

import org.apache.spark.launcher.{AbstractLauncherBackend, SparkAppHandle}

import scala.util.matching.Regex

class MbOutputStream extends OutputStream {

	private val launcherBackend = new AbstractLauncherBackend() {
		override def onStopRequest(): Unit = stop(SparkAppHandle.State.KILLED)
	}

	launcherBackend.connect()

	def stateChanged(s: String): Unit = {
		if (s != "\n") {
			s match {
				case SubmittedJob(appId) =>
					launcherBackend.setAppId(appId)
					launcherBackend.setState(SparkAppHandle.State.SUBMITTED)
				case StartingJob(_)=>
					launcherBackend.setState(SparkAppHandle.State.RUNNING)
				case EndedJob(_) =>
					launcherBackend.setState(SparkAppHandle.State.FINISHED)
				case FailedJob(message) =>
					launcherBackend.setState(SparkAppHandle.State.FAILED)
				case _ =>
			}
		}
	}

	case object SubmittedJob {
		def unapply(arg: String): Option[String] = {
			val r: Regex = "Starting Job = (job_\\d+_\\d+).*".r
			arg match {
				case r(jobId) =>
					Some(jobId.replace("job", "application"))
				case _ =>
					None
			}
		}
	}

	case object StartingJob {
		def unapply(arg: String): Option[Boolean] = {
			val r: Regex = "Hadoop job information for Stage.*".r
			r.findFirstIn(arg).map(_ => true)
		}
	}

	case object EndedJob {
		def unapply(arg: String): Option[Boolean] = {
			val r: Regex = "Total MapReduce CPU Time.*".r
			r.findFirstIn(arg).map(_ => true)
		}
	}

	case object FailedJob {
		def unapply(arg: String): Option[String] = {
			val r: Regex = "FAILED: (.*)".r
			arg match {
				case r(message) =>
					Some(message)
				case _ =>
					None
			}
		}
	}


	override def write(b: Int): Unit = {
		System.out.write(b)
	}

	override def write(b: Array[Byte], off: Int, len: Int): Unit = {
		stateChanged(new String(b, off, len, Charset.forName("UTF-8")))
		System.out.write(b, off, len)
	}

	private def stop(finalState: SparkAppHandle.State): Unit = {
		// TODO stop running hive
		try {
			launcherBackend.setState(finalState)
		} finally {
			launcherBackend.close()
		}
	}
}
