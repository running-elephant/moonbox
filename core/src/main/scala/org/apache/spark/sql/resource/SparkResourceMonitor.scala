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
