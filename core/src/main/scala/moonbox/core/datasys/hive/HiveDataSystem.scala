package moonbox.core.datasys.hive

import moonbox.core.datasys.DataSystem
import org.apache.spark.sql.hive.HiveClientUtils

class HiveDataSystem(props: Map[String, String])
	extends DataSystem(props) {
	require(contains("metastore.url", "metastore.driver", "metastore.user",
		"metastore.password", "hivedb"))

	override def tableNames(): Seq[String] = {
		val client = HiveClientUtils.getHiveClient(props)
		client.listTables(props("hivedb"))
	}

	override def tableName(): String = {
		props("hivetable")
	}

	override def tableProperties(tableName: String): Map[String, String] = {
		props.+("hivetable" -> tableName)
	}
}
