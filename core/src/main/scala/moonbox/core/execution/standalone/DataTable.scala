package moonbox.core.execution.standalone

import org.apache.spark.sql.Row
import org.apache.spark.sql.types.StructType

class DataTable(val iter: Iterator[Row],
								val schema: StructType,
								closeIfNeed: () => Unit) {

	def foreach(f: Row => Unit): Unit = {
		iter.foreach { r =>
			f(r)
		}
		closeIfNeed()
	}

	def close() = {
		closeIfNeed()
	}

	def write(): DataTableWriter = {
		new DataTableWriter(this)
	}
}
