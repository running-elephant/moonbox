package moonbox.grid.runtime.cluster

import org.apache.spark.launcher.SparkAppHandle.State

object ClusterMessage {

    case class YarnAppInfo(coresTotal: Int, memoryTotal: Long, coresFree: Int, memoryFree: Long, lastHeartbeat: Long, submit: Long)

    case class ReportYarnAppResource(adhocInfo: YarnAppInfo, runningJobs: Int)

    case class YarnStatusChange(id: String, appid: String, state: State)

}
