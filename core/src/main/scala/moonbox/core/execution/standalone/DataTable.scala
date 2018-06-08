package moonbox.core.execution.standalone

import org.apache.spark.sql.Row
import org.apache.spark.sql.types.StructType

class DataTable(iter: Iterator[Row],
	closeIfNeed: () => Unit,
	schema: StructType) {

	def foreach(f: Row => Unit): Unit = {
		iter.foreach { r =>
			f(r)
		}
		closeIfNeed()
	}
}
