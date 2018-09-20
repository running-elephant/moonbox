package moonbox.grid.runtime.cluster

import org.apache.spark.launcher.SparkAppHandle

class MbAppListener extends SparkAppHandle.Listener {

	var appId: String = null

	var state: SparkAppHandle.State = null

	override def infoChanged(handle: SparkAppHandle): Unit = {
		appId = handle.getAppId
	}

	override def stateChanged(handle: SparkAppHandle): Unit = {
		state = handle.getState
	}

}
