package moonbox.core.datasys.clickhouse

import moonbox.core.datasys.{DataSystem, DataSystemProvider, DataSystemRegister}


class ClickHouseDataSystemProvider extends DataSystemProvider with DataSystemRegister {
  override def createDataSystem(parameters: Map[String, String]): DataSystem = {
    new ClickHouseDataSystem(parameters)
  }

  override def shortName(): String = "clickhouse"

  override def dataSource(): String = "org.apache.spark.sql.execution.datasources.mbjdbc"
}
