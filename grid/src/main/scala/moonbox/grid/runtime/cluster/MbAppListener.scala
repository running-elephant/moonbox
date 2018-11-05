package moonbox.grid.runtime.cluster

import akka.actor.ActorRef
import org.apache.spark.launcher.SparkAppHandle

class MbAppListener(id: String, actor: ActorRef) extends SparkAppHandle.Listener {

	var appId: String = null

	var state: SparkAppHandle.State = null

	override def infoChanged(handle: SparkAppHandle): Unit = {
		appId = handle.getAppId
	}

	override def stateChanged(handle: SparkAppHandle): Unit = {
		state = handle.getState
	}

}
