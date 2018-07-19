package moonbox.core.resource

import java.io.{BufferedReader, InputStreamReader}
import java.util.StringTokenizer

import org.apache.spark.SparkContext
import org.apache.spark.sql.MixcalContext
import org.apache.spark.sql.resource.{SparkResourceListener, SparkResourceMonitor}


class ResourceMonitor {
	private val mixCalResourceMonitor = MixcalContext.getMixcalResourceMonitor

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
    def clusterTotalCores: Int = {
		mixCalResourceMonitor.clusterTotalCores
	}

	def clusterFreeCores: Int = {
		mixCalResourceMonitor.clusterFreeCores
    }

	def clusterTotalMemory: Long = {
		mixCalResourceMonitor.clusterTotalMemory
	}

    def clusterFreeMemory: Long = {
		mixCalResourceMonitor.clusterFreeMemory
    }

}
