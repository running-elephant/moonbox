package moonbox.grid.deploy.rest

import moonbox.catalog.CatalogApplication
import moonbox.grid.deploy.rest.entities.ApplicationResourceStat
import moonbox.network.util.JavaUtils


class ApplicationResourceComputer(app: CatalogApplication) {

  import ApplicationResourceComputer._

  def getAppResourceStat = {
    val (usedMemory, usedCores) = app.appType match {
      case SPARK_LOCAL => getSparkLocalAppResource(app.config)
      case SPARK_CLUSTER => getSparkClusterAppResource(app.config)
      case PRESTO => getPrestoAppResource(app.config)
    }
    ApplicationResourceStat(
      org = app.org,
      appName = app.name,
      appType = app.appType,
      appCluster = app.cluster.getOrElse(""),
      usedMemory = usedMemory,
      usedCores = usedCores
    )
  }

}


object ApplicationResourceComputer {

  // spark resource params
  private val SPARK_DRIVER_MEMORY_KEY = "spark.driver.memory"
  private val SPARK_EXECUTOR_MEMORY_KEY = "spark.executor.memory"
  private val SPARK_EXECUTOR_INSTANCES_KEY = "spark.executor.instances"
  private val SPARK_DYNAMIC_MAX_EXECUTORS_KEY = "spark.dynamicAllocation.maxExecutors"
  private val SPARK_DRIVER_CORES_KEY = "spark.driver.cores"
  private val SPARK_EXECUTOR_CORES_KEY = "spark.executor.cores"

  private val DEFAULT_SPARK_MEMORY = "1g"
  private val DEFAULT_SPARK_CORES = "1"
  private val DEFAULT_SPARK_EXECUTOR_INSTANCES = "1"

  // app type
  private val SPARK_LOCAL = "sparklocal"
  private val SPARK_CLUSTER = "sparkcluster"
  private val PRESTO = "presto"

  def getSparkLocalAppResource(config: Map[String, String]): (String, Int) = {
    val usedMemory = JavaUtils.byteStringAsGb(config.getOrElse(SPARK_DRIVER_MEMORY_KEY, DEFAULT_SPARK_MEMORY))
    val usedCores = config.getOrElse(SPARK_DRIVER_CORES_KEY, DEFAULT_SPARK_CORES).toInt
    (usedMemory + "G", usedCores)
  }

  def getSparkClusterAppResource(config: Map[String, String]): (String, Int) = {
    var usedMemory: Long = 0
    var usedCores: Int = 0
    val executorInstances = config.getOrElse(SPARK_DYNAMIC_MAX_EXECUTORS_KEY,
      config.getOrElse(SPARK_EXECUTOR_INSTANCES_KEY, DEFAULT_SPARK_EXECUTOR_INSTANCES)).toInt
    usedMemory += JavaUtils.byteStringAsGb(config.getOrElse(SPARK_DRIVER_MEMORY_KEY, DEFAULT_SPARK_MEMORY))
    usedMemory += JavaUtils.byteStringAsGb(config.getOrElse(SPARK_EXECUTOR_MEMORY_KEY, DEFAULT_SPARK_MEMORY)) * executorInstances
    usedCores += config.getOrElse(SPARK_DRIVER_CORES_KEY, DEFAULT_SPARK_CORES).toInt
    usedCores += config.getOrElse(SPARK_EXECUTOR_CORES_KEY, DEFAULT_SPARK_CORES).toInt * executorInstances
    (usedMemory + "G", usedCores)
  }

  def getPrestoAppResource(config: Map[String, String]): (String, Int) = ("0G", 0)

}
