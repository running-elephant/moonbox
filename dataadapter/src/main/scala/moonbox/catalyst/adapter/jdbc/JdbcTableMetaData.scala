package moonbox.catalyst.adapter.jdbc

import moonbox.catalyst.core.TableMetaData
import org.apache.spark.sql.types.{StringType, StructField, StructType}

class JdbcTableMetaData(schema: JdbcSchema, name: String) extends TableMetaData {
	override def getTableSchema: StructType = StructType(Array(
		StructField("name", StringType, nullable = true)))

	override def getTableStats = {
		(0L, 0L)
	}
}
