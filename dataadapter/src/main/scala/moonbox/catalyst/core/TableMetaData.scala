package moonbox.catalyst.core

import org.apache.spark.sql.types.StructType

trait TableMetaData {
	def getTableSchema: StructType

    def getTableStats: (Long, Long)
}
