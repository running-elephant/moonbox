import com.alibaba.druid.pool.DruidDataSource

object Test {
	def main(args: Array[String]) {
		val datasoruce = new DruidDataSource
		datasoruce.setUrl("jdbc:moonbox://10.143.131.38:10010/default")
		datasoruce.setUsername("sally")
		datasoruce.setPassword("123456")
		datasoruce.setDriverClassName("moonbox.jdbc.MbDriver")
		val metaData = datasoruce.getConnection.getMetaData
		val tables = metaData.getTables(null, "default", "%", null)
		val rsMeta = tables.getMetaData
		while (tables.next()) {
			val a = tables.getString(1)
			val tableName = tables.getString("TABLE_NAME")
			val keys = metaData.getPrimaryKeys(null, null, tableName)
			while (keys.next()) {
				keys.getString(4)
			}
			println(tableName)
			if (!tableName.startsWith("test_es") && !tableName.startsWith("kudu")) {
				val columns = metaData.getColumns(null, null, tableName, "%")
				while (columns.next()) {
					print("   ")
					println(columns.getString(4) + " " + columns.getString(6))
				}
			}
		}
	}

}
