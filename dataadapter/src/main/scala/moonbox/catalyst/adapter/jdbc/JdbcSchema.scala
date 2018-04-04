package moonbox.catalyst.adapter.jdbc

import moonbox.catalyst.core.{Schema, TableMetaData}

class JdbcSchema(props: Map[String, String]) extends Schema {

  override def getFunctionNames: Seq[String] = Seq("max", "min", "avg", "count", "sum")

  override def getTableMetaData(name: String): TableMetaData = new JdbcTableMetaData(this, name)

  override def getTableNames: Seq[String] = Seq()

  override def getTableNames2: Seq[(String, String)] = Seq()

  override def getVersion: Seq[Int] = Seq()

}
