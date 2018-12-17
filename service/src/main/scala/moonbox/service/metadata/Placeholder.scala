package moonbox.service.metadata

import org.apache.spark.sql.types.StructType

trait Placeholder {
	def tables: Seq[String]
	def tableSchema(table: String): StructType
}
