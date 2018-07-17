package moonbox.core.datasys.hbase
import moonbox.core.datasys.DataSystem
import org.apache.hadoop.hbase.client.{Admin, ConnectionFactory, HBaseAdmin}
import org.apache.hadoop.hbase.{HBaseConfiguration, HConstants}

class HbaseDataSystem(props: Map[String, String]) extends DataSystem(props) {
	require(contains("hbase.zookeeper.quorum"))

	private def getClient: Admin = {
		val conf = HBaseConfiguration.create
		conf.set(HConstants.ZOOKEEPER_QUORUM, props("hbase.zookeeper.quorum"))

		val connection = ConnectionFactory.createConnection(conf)
		connection.getAdmin
	}

	override def tableNames(): Seq[String] = {
		val admin = getClient
		val tables: Seq[String] = admin.listTableNames().map(_.getNameAsString)
		admin.close()
		tables
	}

	override def tableName(): String = {
		throw new Exception("Function tableName no implementation, for HBASE dose not support physical mount")
	}

	override def tableProperties(tableName: String): Map[String, String] = {
		throw new Exception("Function tableProperties no implementation, for HBASE dose not support physical mount")
	}

	override def test(): Boolean = {
		try {
			val conf = HBaseConfiguration.create
			conf.set(HConstants.ZOOKEEPER_QUORUM, props("hbase.zookeeper.quorum"))

			HBaseAdmin.checkHBaseAvailable(conf)
			true
		}catch {
			case _: Exception => false
		}
	}
}
