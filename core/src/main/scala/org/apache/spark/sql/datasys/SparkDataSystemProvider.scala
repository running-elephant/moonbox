package org.apache.spark.sql.datasys

import moonbox.core.datasys.{DataSystem, DataSystemProvider}

class SparkDataSystemProvider extends DataSystemProvider {
	override def createDataSystem(parameters: Map[String, String]): DataSystem = {
		new SparkDataSystem
	}
}
