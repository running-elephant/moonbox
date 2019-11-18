package moonbox.application.interactive.spark

import moonbox.protocol.protobuf.SchemaPB
import org.apache.spark.sql.types.StructType

object SchemaUtils {
	def convertSchema(schema: StructType): SchemaPB = {
		null
	}

	def emptySchema: StructType = StructType.apply(Seq.empty)
}
