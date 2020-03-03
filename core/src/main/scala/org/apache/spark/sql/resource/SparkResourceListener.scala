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

import moonbox.common.MbLogging
import org.apache.spark.SparkConf
import org.apache.spark.scheduler._


class SparkResourceListener(conf: SparkConf) extends SparkListener with MbLogging{

    val executorInfoMap = new scala.collection.mutable.HashMap[String, Int] //exector-id, (total, free)
    val cpus_per_task = conf.get("spark.task.cpus", "1").toInt

    /**in local mode, no this message, in yarn mode, receive when sparkcontext init**/
    override def onExecutorAdded(executorAdded: SparkListenerExecutorAdded): Unit = {
        val executorInfo = executorAdded.executorInfo
        logInfo(s"onExecutorAdded id: ${executorAdded.executorId}, host: ${executorInfo.executorHost}, cores: ${executorInfo.totalCores}, url: ${executorInfo.logUrlMap}")
        executorInfoMap.update(executorAdded.executorId, 0)
    }

    /**in local mode, no this message**/
    override def onExecutorRemoved(executorRemoved: SparkListenerExecutorRemoved): Unit = {
        logInfo("onExecutorRemoved: " + executorRemoved.executorId)
        executorInfoMap.remove(executorRemoved.executorId)
    }

    override def onTaskStart(taskStart: SparkListenerTaskStart): Unit = synchronized {
        val executorId = taskStart.taskInfo.executorId
        logInfo(s"onTaskStart begin task ${taskStart.taskInfo.index}, executorId $executorId, host ${taskStart.taskInfo.host}, contain ${executorInfoMap.contains(executorId)}")

        if(executorInfoMap.contains(executorId)) {
            val changedCores = executorInfoMap(executorId)
            executorInfoMap.update(executorId, changedCores - cpus_per_task)
        }else{
            executorInfoMap.put(executorId, 0 - cpus_per_task)
        }
        logInfo(s"onTaskStart update task ${taskStart.taskInfo.index}, executorId $executorId, changeCore ${{executorInfoMap(executorId)}}")

    }

    override def onTaskEnd(taskEnd: SparkListenerTaskEnd): Unit = synchronized {
        val executorId = taskEnd.taskInfo.executorId
        logInfo(s"onTaskEnd task ${taskEnd.taskInfo.index}, executorId $executorId, host ${taskEnd.taskInfo.host}, contain ${executorInfoMap.contains(executorId)}")

        if(executorInfoMap.contains(executorId)) {
            val changedCores = executorInfoMap(executorId)
            executorInfoMap.update(executorId, changedCores + cpus_per_task)
        }else{
            executorInfoMap.put(executorId, cpus_per_task)
        }
        logInfo(s"onTaskEnd update task ${taskEnd.taskInfo.index}, executorId $executorId, changeCore ${{executorInfoMap(executorId)}}")

    }

    def getExecutorInfo: Map[String, Int] = {
        Map.empty ++ executorInfoMap
    }

}
