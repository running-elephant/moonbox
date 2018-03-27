package moonbox.common.util

import java.util.concurrent.{ScheduledExecutorService, ScheduledThreadPoolExecutor}

object ThreadUtils {

	def newDaemonSingleThreadScheduledExecutor(threadName: String): ScheduledExecutorService = {
		val executor = new ScheduledThreadPoolExecutor(1)
		// By default, a cancelled task is not automatically removed from the work queue until its delay
		// elapses. We have to enable it manually.
		executor.setRemoveOnCancelPolicy(true)
		executor
	}
}
