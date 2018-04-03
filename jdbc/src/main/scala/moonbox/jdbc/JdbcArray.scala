package moonbox.jdbc

import java.sql.Types
import java.util

class JdbcArray(array: Array[Any]) extends java.sql.Array {
  override def getArray = array

  override def getArray(map: util.Map[String, Class[_]]) = array

  override def getArray(index: Long, count: Int) = {
    if (index > Int.MaxValue)
      throw new IllegalArgumentException(s"The argument index cannot be greater than ${Int.MaxValue}")
    val newArray = Array[Any]()
    array.copyToArray(newArray, index.toInt, count)
    newArray
  }

  override def getArray(index: Long, count: Int, map: util.Map[String, Class[_]]) = getArray(index, count)

  /**
    * Returns the base type of the array. This database does support mixed type
    * arrays and therefore there is no base type.
    *
    * @return Types.NULL
    */
  override def getBaseType = Types.NULL

  /**
    * Returns the base type name of the array. This database does support mixed
    * type arrays and therefore there is no base type.
    *
    * @return "NULL"
    */
  override def getBaseTypeName = "NULL"

  override def getResultSet = null

  override def getResultSet(map: util.Map[String, Class[_]]) = null

  override def getResultSet(index: Long, count: Int) = null

  override def getResultSet(index: Long, count: Int, map: util.Map[String, Class[_]]) = null

  override def free() = {}
}
