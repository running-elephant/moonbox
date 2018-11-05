package moonbox.grid.runtime.cluster


case class AppResourceInfo(coresTotal: Int, memoryTotal: Long, coresFree: Int, memoryFree: Long, lastHeartbeat: Long, submit: Long)