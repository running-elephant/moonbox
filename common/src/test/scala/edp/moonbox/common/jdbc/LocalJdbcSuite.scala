package edp.moonbox.common.jdbc

import java.sql.DriverManager

import org.scalatest.FunSuite

class LocalJdbcSuite extends FunSuite {

	test("H2 Server") {
		val jdbc: LocalJdbc = new LocalJdbc()
		val urlWithUserAndPass = "jdbc:h2:mem:testdb0;user=testUser;password=testPass"
		jdbc.start()
		Class.forName("org.h2.Driver")
		val conn = DriverManager.getConnection(urlWithUserAndPass)
		val result = conn.createStatement().executeQuery("show databases")
		assert(result.next())
		jdbc.stop()
	}

}
