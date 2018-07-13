package moonbox.core.datasys.parquet

import moonbox.core.datasys.DataSystem

class ParquetDataSystem(props: Map[String, String])
	extends DataSystem(props) {

	override def tableNames(): Seq[String] = Seq()

	override def tableName(): String = {
		props("path")
	}

	override def tableProperties(tableName: String): Map[String, String] = {
		props
	}
}
