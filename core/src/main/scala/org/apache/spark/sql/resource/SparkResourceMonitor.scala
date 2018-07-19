package org.apache.spark.sql.resource

import org.apache.spark.SparkContext

class SparkResourceMonitor(sparkContext: SparkContext, resourceListener: SparkResourceListener) {

	def clusterTotalCores: Int = {
		if(sparkContext.isLocal){
			// TODO
			val phyCpuCores = Runtime.getRuntime.availableProcessors //获取当前机器cpu核数
			phyCpuCores
		}else {
			// TODO
			val coreNumPerExecutor: Int = sparkContext.conf.get("spark.executor.cores", "1").toInt
			val activeExecutorIds = sparkContext.getExecutorIds  //for CoarseGrainedSchedulerBackend, NOT LocalSchedulerBackend
			val totalCores = activeExecutorIds.size * coreNumPerExecutor
			totalCores
		}
	}

	def clusterFreeCores: Int = {
		if(sparkContext.isLocal){
			// TODO
			val phyCpuCores = Runtime.getRuntime.availableProcessors //获取当前机器cpu核数
			phyCpuCores
		} else {
			val coreNumPerExecutor: Int = sparkContext.conf.get("spark.executor.cores", "1").toInt
			val activeExecutorIds = sparkContext.getExecutorIds  //for CoarseGrainedSchedulerBackend, NOT LocalSchedulerBackend
			val totalCores = activeExecutorIds.size * coreNumPerExecutor
			val changedCores = resourceListener.getExecutorInfo.filter(executor => activeExecutorIds.contains(executor._1)).values.sum
			totalCores - changedCores
		}
	}

	def clusterTotalMemory: Long = {
		val totalMemory = sparkContext.getExecutorMemoryStatus.map{block => block._2._1}.sum
		totalMemory
	}

	def clusterFreeMemory: Long = {
		val remainingMemory = sparkContext.getExecutorMemoryStatus.map{block => block._2._2}.sum
		remainingMemory
	}
}
