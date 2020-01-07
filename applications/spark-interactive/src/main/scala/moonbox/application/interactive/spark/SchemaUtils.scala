package moonbox.application.interactive.spark

import org.apache.spark.sql.types.StructType

object SchemaUtils {
	def emptySchema: StructType = StructType.apply(Seq.empty)
}
