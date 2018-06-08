package moonbox.core.execution.standalone

import org.apache.spark.sql.SaveMode

import scala.collection.mutable

class DataTableWriter(dt: DataTable) {
	private var source: String = _
	private var mode: SaveMode = SaveMode.ErrorIfExists
	private val options = new mutable.HashMap[String, String]

	def mode(saveMode: SaveMode): DataTableWriter = {
		this.mode = saveMode
		this
	}

	def format(source: String): DataTableWriter = {
		this.source = source
		this
	}

	def option(key: String, value: String): DataTableWriter = {
		this.options += (key -> value)
		this
	}

	def options(options: scala.collection.Map[String, String]): DataTableWriter = {
		this.options ++= options
		this
	}

	def save(): Unit = {

	}

}
