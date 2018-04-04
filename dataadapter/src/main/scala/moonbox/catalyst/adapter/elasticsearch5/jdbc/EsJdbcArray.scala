package moonbox.catalyst.adapter.elasticsearch5.jdbc

import java.util

import scala.reflect.runtime.universe._

class EsJdbcArray[T: TypeTag](array: scala.Array[T]) extends java.sql.Array {
    override def getArray = {
        array
    }

    override def getArray(map :util.Map[String, Class[_]]) = {  //TODO:
        array
    }

    override def getArray(index :Long, count :Int) = {
        array
    }

    override def getArray(index :Long, count :Int, map :util.Map[String, Class[_]]) = {
        array
    }

    override def getBaseTypeName: String = {
        typeOf[T].toString
    }

    override def getBaseType: Int = {
        getBaseTypeName match {
            case "String" => java.sql.Types.VARCHAR
            case "Int" => java.sql.Types.INTEGER
            case "Double" =>  java.sql.Types.DOUBLE
            case "Float" =>  java.sql.Types.FLOAT
            case "TimeStamp" =>  java.sql.Types.TIMESTAMP
            case "Null" =>  java.sql.Types.NULL
            case "Boolean" =>  java.sql.Types.BOOLEAN
            case "Decimal" =>  java.sql.Types.DECIMAL
        }
    }

    override def getResultSet = {
        new EsCatalystResultSet(null, Map.empty[String, Int])
    }

    override def getResultSet(map :util.Map[String, Class[_]]) = {  //TODO:
        new EsCatalystResultSet(null, Map.empty[String, Int])
    }

    override def getResultSet(index :Long, count :Int) = {  //TODO:
        new EsCatalystResultSet(null, Map.empty[String, Int])
    }

    override def getResultSet(index :Long, count :Int, map :util.Map[String, Class[_]]) = {
        new EsCatalystResultSet(null, Map.empty[String, Int])
    }

    override def free() = {}


}
