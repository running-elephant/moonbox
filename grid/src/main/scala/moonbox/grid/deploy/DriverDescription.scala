package moonbox.grid.deploy

import moonbox.common.MbConf
import moonbox.common.util.Utils
import org.apache.spark.launcher.SparkLauncher

private[deploy] trait DriverDescription {
	def master: String
	def deployMode: Option[String]
	def mainClass: String
	def appResource: String
	def toAppArgs: Seq[String]
	def toConf: Map[String, String]
}

private[deploy] class LocalDriverDescription(conf: MbConf) extends DriverDescription {
	override def master = {
		val cores = Runtime.getRuntime.availableProcessors()
		s"local[${cores * 50}]"
	}
	override def deployMode = None
	override def mainClass = "moonbox.application.interactive.Main"

	override def toString: String = {
		s"DriverDescription ($master)"
	}

	override def toAppArgs: Seq[String] = {
		Map(
			"moonbox.deploy.catalog.implementation" -> "mysql",
			"moonbox.deploy.catalog.url" -> "jdbc:mysql://10.143.131.38:3306/moonbox7?createDatabaseIfNotExist=true",
			"moonbox.deploy.catalog.user" -> "root",
			"moonbox.deploy.catalog.password" -> "123456",
			"moonbox.deploy.catalog.driver" -> "com.mysql.jdbc.Driver"
		).toSeq.flatMap { case (k, v) => Seq(k, v)}
	}

	override def toConf: Map[String, String] = {
		Map(SparkLauncher.DRIVER_EXTRA_CLASSPATH ->
			"/Users/wanghao/IdeaProjects/moonbox-0.2.0/assembly/target/moonbox-assembly_2.11-0.3.0-SNAPSHOT-dist/moonbox/libs/*",
			SparkLauncher.DRIVER_EXTRA_JAVA_OPTIONS -> "-Dlog4j.debug=true -Dlog4j.configuration=\"\""
		)
	}

	override def appResource: String = {
		Utils.getAppResourceJar("interactive").getOrElse(
			throw new Exception("Interactive app jar does not found in env.")
		)
	}
}

private[deploy] class ClientDriverDescription(conf: MbConf) extends DriverDescription {
	override def master = "yarn"
	override def deployMode = Some("client")
	override def mainClass = "moonbox.application.interactive.Main"

	override def toString: String = {
		s"DriverDescription ($master ${deployMode.get})"
	}

	override def toAppArgs: Seq[String] = {
		Map(
			"moonbox.deploy.catalog.implementation" -> "mysql",
			"moonbox.deploy.catalog.url" -> "jdbc:mysql://10.143.131.38:3306/moonbox7?createDatabaseIfNotExist=true",
			"moonbox.deploy.catalog.user" -> "root",
			"moonbox.deploy.catalog.password" -> "123456",
			"moonbox.deploy.catalog.driver" -> "com.mysql.jdbc.Driver"
		).toSeq.flatMap { case (k, v) => Seq(k, v)}
	}

	override def toConf: Map[String, String] = {
		Map(SparkLauncher.DRIVER_EXTRA_CLASSPATH ->
			"/Users/wanghao/IdeaProjects/moonbox-0.2.0/assembly/target/moonbox-assembly_2.11-0.3.0-SNAPSHOT-dist/moonbox/libs/*",
			SparkLauncher.DRIVER_EXTRA_JAVA_OPTIONS -> "-Dlog4j.debug=true -Dlog4j.configuration=\"\""
		)
	}

	override def appResource: String = {
		Utils.getAppResourceJar("interactive").getOrElse(
			throw new Exception("Interactive app jar does not found in env.")
		)
	}
}

private[deploy] case class ClusterDriverDescription(
	username: String,
	sqls: Seq[String],
	config: String) extends DriverDescription {

	override def master = "yarn"
	override def deployMode = Some("cluster")
	override def mainClass = "moonbox.application.batch.Main"

	override def toString: String = {
		s"DriverDescription ($master ${deployMode.get} $username ${sqls.mkString(";")})"
	}

	override def toAppArgs: Seq[String] = {
		Map(
			"username" -> username,
			"sqls" -> sqls.mkString(";"),
			"moonbox.deploy.catalog.implementation" -> "mysql",
			"moonbox.deploy.catalog.url" -> "jdbc:mysql://10.143.131.38:3306/moonbox7?createDatabaseIfNotExist=true",
			"moonbox.deploy.catalog.user" -> "root",
			"moonbox.deploy.catalog.password" -> "123456",
			"moonbox.deploy.catalog.driver" -> "com.mysql.jdbc.Driver"
		).toSeq.flatMap { case (k, v) => Seq(k, v)}
	}

	override def toConf: Map[String, String] = {
		/*.setConf(SparkLauncher.DRIVER_EXTRA_JAVA_OPTIONS, "-XX:+UseConcMarkSweepGC -XX:+PrintGCDetails -XX:-UseGCOverheadLimit -Dlog4j.configuration=sparkx.log4j.properties -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/tmp/moonbox/gc/,spark.executor.extraJavaOptions=-XX:+UseConcMarkSweepGC -XX:+PrintGCDetails -XX:-UseGCOverheadLimit -Dlog4j.configuration=sparkx.log4j.properties -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/tmp/moonbox/gc")*/
		Map("spark.hadoop.yarn.resourcemanager.address" -> "172.16.231.133:8032",
			"spark.yarn.access.namenodes" -> "hdfs://172.16.231.133:8020",
			"spark.yarn.am.memory" -> "512m"
		)
	}

	override def appResource: String = {
		Utils.getAppResourceJar("batch").getOrElse(
			throw new Exception("batch app jar does not found in env.")
		)
	}
}

