package org.apache.spark.sql.resource

import java.io.{BufferedReader, InputStreamReader}
import java.util.StringTokenizer

import org.apache.spark.SparkContext


class ResourceMonitor(sparkContext: SparkContext, resourceListener: SparkResourceListener){

    def workerTotalMemory: Long = {
        Runtime.getRuntime.totalMemory  // Worker JVM 可使用内存
    }

    def workerFreeMemory: Long = {
        Runtime.getRuntime.freeMemory   // Worker JVM 剩余内存
    }

    def workerMaxMemory: Long = {
        Runtime.getRuntime.maxMemory    // Worker JVM 最大可使用内存
    }

    def workerCpuFreeRate: Float = {    // Worker CPU Free Rate
        try {
            val process = Runtime.getRuntime.exec("top -b -n 1")
            val is = process.getInputStream
            val isr = new InputStreamReader(is)
            val brStat = new BufferedReader(isr)
            brStat.readLine
            brStat.readLine
            val tokenStat = new StringTokenizer(brStat.readLine)
            tokenStat.nextToken
            tokenStat.nextToken
            tokenStat.nextToken
            tokenStat.nextToken
            tokenStat.nextToken
            tokenStat.nextToken
            tokenStat.nextToken
            val cpuUsage = tokenStat.nextToken
            //System.out.println("CPU idle : " + cpuUsage)
            val usage: Float = cpuUsage.substring(0, cpuUsage.indexOf("%")).toFloat
            1 - usage / 100
        } catch {
            case e: Exception => e.printStackTrace(); throw e
        }
    }

    /** executor.cores: default 1 in YARN mode, all the available cores on the worker in standalone */
    def clusterFreeCore: (Int, Int) = {
        if(sparkContext.isLocal){
            val phyCpuCores = Runtime.getRuntime.availableProcessors //获取当前机器cpu核数
            (phyCpuCores, phyCpuCores)
        }else {
            val coreNumPerExecutor: Int = sparkContext.conf.get("spark.executor.cores", "1").toInt
            val activeExecutorIds = sparkContext.getExecutorIds  //for CoarseGrainedSchedulerBackend, NOT LocalSchedulerBackend
            val totalCores = activeExecutorIds.size * coreNumPerExecutor
            val changedCores = resourceListener.getExecutorInfo.filter(executor => activeExecutorIds.contains(executor._1)).values.sum
            (totalCores, totalCores - changedCores)
        }
    }

    def clusterFreeMemory: (Long, Long) = {
        val totalMemory = sparkContext.getExecutorMemoryStatus.map{block => block._2._1}.sum
        val remainingMemory = sparkContext.getExecutorMemoryStatus.map{block => block._2._2}.sum
        (totalMemory, remainingMemory)
    }

}
