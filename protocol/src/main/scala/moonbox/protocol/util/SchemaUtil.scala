package moonbox.protocol.util

import java.util.Locale

import moonbox.protocol.DataType
import org.json.JSONObject

import collection.JavaConverters._

object SchemaUtil {

	// name, type, nullable
	def parse(json: String): Array[(String, String, Boolean)] = {
		val schemaObject = new JSONObject(json)
		schemaObject.getJSONArray("fields").asScala.map {
			case elem: JSONObject =>
				val columnName = elem.getString("name")
				val nullable = elem.getBoolean("nullable")
				val columnType = elem.get("type") match {
					case v: JSONObject => handleArrayAndMap(v)
					case s => s.toString
				}
				(columnName, columnType, nullable)
			case _ => null
		}.filter(_ != null).toArray
	}

	/**
	  * map(string,string,false)  // map(keyType,valueType,valueContainsNull)
	  * array(string,false)  // array(elementType,containsNull)
	  */
	def handleArrayAndMap(jsonObject: JSONObject): String = {
		jsonObject.get("type") match {
			case "map" =>
				val kType = jsonObject.get("keyType")
				val vType = jsonObject.get("valueType")
				val vContainsNull = jsonObject.get("valueContainsNull")
				s"map($kType,$vType,$vContainsNull)"
			case "array" =>
				val elementType = jsonObject.get("elementType")
				val containsNull = jsonObject.get("containsNull")
				s"array($elementType,$containsNull)"
			case "struct" => "struct[...]"
			case other => throw new Exception(s"Cannot capture type: $other")
		}
	}

	def emptyJsonSchema: String = {
		"""
		  |{
		  |  "type": "struct",
		  |  "fields": []
		  |}
		""".stripMargin
	}

	def getKeyTypeInMap(typeName: String): String = {
		typeName.stripPrefix("map(").stripSuffix(")").split(",").head
	}

	def getValueTypeInMap(typeName: String): String = {
		Option(typeName.stripPrefix("map(").stripSuffix(")").split(",")(1)).getOrElse("")
	}

	def getElementTypeInArray(typeName: String): String = {
		typeName.stripPrefix("array(").stripSuffix(")").split(",").head
	}

	def typeNameToDataType(typeName: String): DataType = DataType.getDataType(typeName)

	def typeNameToSqlType(typeName: String): Int = {
		import java.sql.Types._

		val lowerCase = typeName.toLowerCase(Locale.ROOT)
		if (lowerCase.startsWith("decimal")) DECIMAL // decimal(10,2)
		else if (lowerCase.startsWith("map")) JAVA_OBJECT // map(string,string,false)
		else if (lowerCase.startsWith("array")) ARRAY // array(string,true)
		else if (lowerCase.startsWith("struct")) STRUCT
		else lowerCase match {
			case "binary" => BINARY
			case "boolean" => BIT
			case "date" => DATE
			case "char" => VARCHAR
			case "varchar" => VARCHAR
			case "double" => DOUBLE
			case "float" => FLOAT
			case "byte" => TINYINT
			case "integer" => INTEGER
			case "long" => BIGINT
			case "short" => SMALLINT
			case "string" => VARCHAR
			case "timestamp" => TIMESTAMP
			case "null" => NULL
			case "object" => JAVA_OBJECT
			case "calendarinterval" => JAVA_OBJECT // CalendarIntervalType in spark
			case _ => JAVA_OBJECT // user defined type in spark
		}
	}

	def typeNameToJavaClassName(typeName: String): String = {
		val lowerCase = typeName.toLowerCase(Locale.ROOT)
		if (lowerCase.startsWith("decimal")) {
			"java.math.BigDecimal"
		} else if (lowerCase.startsWith("map")) {
			"java.lang.Object"
		} else if (lowerCase.startsWith("array")) {
			"java.lang.Object"
		} else if (lowerCase.startsWith("struct")) {
			"java.lang.Object"
		} else lowerCase match {
			case "binary" => "[B"
			case "boolean" => "java.lang.Boolean"
			case "date" => "java.sql.Date"
			case "char" => "java.lang.Character"
			case "varchar" => "java.lang.String"
			case "double" => "java.lang.Double"
			case "float" => "java.lang.Float"
			case "byte" => "java.lang.Byte"
			case "integer" => "java.lang.Integer"
			case "long" => "java.lang.Long"
			case "short" => "java.lang.Short"
			case "string" => "java.lang.String"
			case "timestamp" => "java.sql.Timestamp"
			case "null" => "java.lang.Object"
			case "object" => "java.lang.Object"
			case "calendarinterval" => "java.lang.Object" // CalendarIntervalType in spark
			case _ => "java.lang.Object" // user defined type in spark
		}
	}

	def schemaWithSqlType(schema: Array[(String, String, Boolean)]): Array[(String, Int, Boolean)] = {
		schema.map {
			case (name, typeName, nullable) => (name, typeNameToSqlType(typeName), nullable)
		}
	}

	/** Array[(name dataType nullable)] */
	def schemaToDataType(schema: Array[(String, String, Boolean)]): Array[(String, DataType, Boolean)] = {
		schema.map {
			case (name, ty, nullable) => (name, typeNameToDataType(ty), nullable)
		}
	}
}
