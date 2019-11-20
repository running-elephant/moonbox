package moonbox.application.interactive.spark

import moonbox.protocol.protobuf.StructTypePB
import org.apache.spark.sql.types.StructType

object SchemaUtils {
	def convertSchema(schema: StructType): StructTypePB = {
		null
	}

	def emptySchema: StructType = StructType.apply(Seq.empty)
}
