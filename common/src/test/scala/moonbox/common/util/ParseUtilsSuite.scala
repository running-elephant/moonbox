package moonbox.common.util

import fastparse.core.Parsed
import moonbox.common.util.ParseUtils._
import org.scalatest.FunSuite
class ParseUtilsSuite extends FunSuite {

	test("parse hostname") {
		val Parsed.Success(host1, _) = hostnameParser.parse("localhost")
		val Parsed.Success(host2, _) = hostnameParser.parse("LOCALHOST")
		val Parsed.Success(host3, _) = hostnameParser.parse("lOcAlHoSt")
		val Parsed.Success(host4, _) = hostnameParser.parse("lO3cAl5HoS9t")
		assert(host1 == "localhost")
		assert(host2 == "LOCALHOST")
		assert(host3 == "lOcAlHoSt")
		assert(host4 == "lO3cAl5HoS9t")
	}

	test("parse ip") {
		val Parsed.Success(ip1, _) = ipParser.parse("127.0.0.1")
		val Parsed.Success(ip2, _) = ipParser.parse("100.100.100.1")
		assert(ip1 == "127.0.0.1")
		assert(ip2 == "100.100.100.1")
	}

	test("parse port") {
		val Parsed.Success(port, _) = portParser.parse("8080")
		assert(port == 8080)
	}

	test("parse socket") {
		val Parsed.Success(socket1, _) = socketParser.parse("localhost:7070")
		val Parsed.Success(socket2, _) = socketParser.parse("localhost")
		val Parsed.Success(socket3, _) = socketParser.parse("127.0.0.1:8080")
		val Parsed.Success(socket4, _) = socketParser.parse("127.0.0.1")
		assert(socket1 == ("localhost", Some(7070)))
		assert(socket2 == ("localhost", None))
		assert(socket3 == ("127.0.0.1", Some(8080)))
		assert(socket4 == ("127.0.0.1", None))
	}

	test("parse sockets") {
		val sockets = parseAddresses("localhost,localhost:7077,127.0.0.1,127.0.0.1:8080")
		assert(sockets == Seq(
			("localhost", None),
			("localhost", Some(7077)),
			("127.0.0.1", None),
			("127.0.0.1", Some(8080))
		))
	}

	test("parse time") {
		assert(parseTime("1ms") == 1)
		assert(parseTime("1MS") == 1)
		assert(parseTime("1s") == 1000)
		assert(parseTime("1S") == 1000)
		assert(parseTime("1m") == 1 * 60 * 1000)
		assert(parseTime("1M") == 1 * 60 * 1000)
		assert(parseTime("1min") == 1 * 60 * 1000)
		assert(parseTime("1h") == 1 * 60 * 60 * 1000)
		assert(parseTime("1H") == 1 * 60 * 60 * 1000)
	}

	test("parse key") {
		val Parsed.Success(key1, _) = keyParser.parse("type")
		val Parsed.Success(key2, _) = keyParser.parse("input.location")
		assert(key1 == "type")
		assert(key2 == "input.location")
	}

	test("parse value") {
		val Parsed.Success(value1, _) = valueParser.parse("'mysql'")
		val Parsed.Success(value2, _) = valueParser.parse("\"mysql\"")
		val Parsed.Success(value3, _) = valueParser.parse("'a.b.c'")
		val Parsed.Success(value4, _) = valueParser.parse("\"a.b.c\"")
		assert(value1 == "mysql")
		assert(value2 == "mysql")
		assert(value3 == "a.b.c")
		assert(value4 == "a.b.c")
	}

	test("parse key value pair") {
		val Parsed.Success(kv1, _) = keyValueParser.parse("type 'mysql'")
		val Parsed.Success(kv2, _) = keyValueParser.parse("url 'jdbc:mysql://localhost:3306/test'")
		val Parsed.Success(kv3, _) = keyValueParser.parse("url='jdbc:mysql://localhost:3306/test'")
		val Parsed.Success(kv4, _) = keyValueParser.parse("url=='jdbc:mysql://localhost:3306/test'")
		val Parsed.Success(kv5, _) = keyValueParser.parse("url = 'jdbc:mysql://localhost:3306/test'")
		val Parsed.Success(kv6, _) = keyValueParser.parse("url ==  'jdbc:mysql://localhost:3306/test'")
		val Parsed.Success(kv7, _) = keyValueParser.parse("url    \"jdbc:mysql://localhost:3306/test\"")
		val Parsed.Success(kv8, _) = keyValueParser.parse("url=\"jdbc:mysql://localhost:3306/test\"")
		val Parsed.Success(kv9, _) = keyValueParser.parse("url==\"jdbc:mysql://localhost:3306/test\"")
		val Parsed.Success(kv10, _) = keyValueParser.parse("url = \"jdbc:mysql://localhost:3306/test\"")
		val Parsed.Success(kv11, _) = keyValueParser.parse("url ==  \"jdbc:mysql://localhost:3306/test\"")
		assert(kv1 == ("type", "mysql"))
		assert(kv2 == ("url", "jdbc:mysql://localhost:3306/test"))
		assert(kv3 == ("url", "jdbc:mysql://localhost:3306/test"))
		assert(kv4 == ("url", "jdbc:mysql://localhost:3306/test"))
		assert(kv5 == ("url", "jdbc:mysql://localhost:3306/test"))
		assert(kv6 == ("url", "jdbc:mysql://localhost:3306/test"))
		assert(kv7 == ("url", "jdbc:mysql://localhost:3306/test"))
		assert(kv8 == ("url", "jdbc:mysql://localhost:3306/test"))
		assert(kv9 == ("url", "jdbc:mysql://localhost:3306/test"))
		assert(kv10 == ("url", "jdbc:mysql://localhost:3306/test"))
		assert(kv11 == ("url", "jdbc:mysql://localhost:3306/test"))
	}

	test("parse properties") {
		val Parsed.Success(properties, _) = propertiesParser.parse("""type   'mysql',url==  "jdbc:mysql://localhost:3306/test",   password = '123456'""")
		assert(properties == Seq(
			("type", "mysql"),
			("url", "jdbc:mysql://localhost:3306/test"),
			("password", "123456"))
		)
	}
}
