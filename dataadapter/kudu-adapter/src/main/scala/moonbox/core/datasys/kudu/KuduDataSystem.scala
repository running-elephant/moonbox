package moonbox.core.datasys.kudu

import moonbox.core.datasys.{DataSystem, DataSystemRegister}
import org.apache.kudu.client.KuduClient.KuduClientBuilder

import scala.collection.JavaConversions._

class KuduDataSystem(props: Map[String, String]) extends DataSystem(props) {
	require(contains("kudu.master"))

	override def tableNames(): Seq[String] = {
		val kuduClient = new KuduClientBuilder(props("kudu.master")).build()
		val tables = kuduClient.getTablesList.getTablesList
		kuduClient.close()
		tables
	}

	override def tableName(): String = {
		props("kudu.table")
	}

	override def tableProperties(tableName: String): Map[String, String] = {
		props.+("kudu.table" -> tableName)
	}


}
