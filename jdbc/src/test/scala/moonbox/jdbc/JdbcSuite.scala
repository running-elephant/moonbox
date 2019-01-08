package moonbox.jdbc

import java.sql.{Connection, DatabaseMetaData, DriverManager, ResultSet}

import org.scalatest.FunSuite

class JdbcSuite extends FunSuite {
	test("jdbc") {
		Class.forName("moonbox.jdbc.MbDriver")
		val connection: Connection = DriverManager.getConnection("jdbc:moonbox://10.143.131.38:10010/my_sql_test_test", "sally", "123456")
		val metaData: DatabaseMetaData = connection.getMetaData
		val columns: ResultSet = metaData.getColumns(null, null, "write_test", null)
		while (columns.next()) {
			println(columns.getString(4) + " " + columns.getString(6))
		}
	}
}
