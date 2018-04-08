package moonbox.catalyst.adapter.elasticsearch5.jdbc

import java.sql
import java.sql.{Date, ResultSetMetaData, Time, Timestamp}

import moonbox.catalyst.adapter.jdbc.{JdbcResultSetMetaData, JdbcRow}
import moonbox.catalyst.jdbc.CatalystResultSet

class EsCatalystResultSet(iter: Iterator[JdbcRow], map: Map[String, Int]) extends CatalystResultSet {

    var currentRow: JdbcRow = _

    override def next(): Boolean = {
        if (iter.hasNext) {
            currentRow = iter.next()
            true
        } else false
    }

    override def getString(columnIndex: Int): String = {
        currentRow(columnIndex - 1).toString
    }

    override def getBoolean(columnIndex: Int): Boolean = {
        currentRow(columnIndex - 1).asInstanceOf[Boolean]
    }

    override def getShort(columnIndex: Int): Short = {
        currentRow(columnIndex - 1).asInstanceOf[Short]
    }

    override def getInt(columnIndex :Int) = {
        currentRow(columnIndex - 1).asInstanceOf[Int]
    }

    override def getLong(columnIndex: Int): Long = {
        currentRow(columnIndex - 1).asInstanceOf[Long]
    }

    override def getFloat(columnIndex: Int): Float = {
        currentRow(columnIndex - 1).asInstanceOf[Float]
    }

    override def getDouble(columnIndex: Int): Double = {
        currentRow(columnIndex - 1).asInstanceOf[Double]
    }

    override def getDate(columnIndex: Int): Date = {
        val ts = currentRow(columnIndex - 1).asInstanceOf[Long]
        new Date(ts)
    }

    override def getDate(columnLabel: String): Date = {
        getDate(map(columnLabel))
    }

    override def getTime(columnIndex: Int): Time = {
        val ts = currentRow(columnIndex - 1).asInstanceOf[Long]  //spark no time type, all date type in es are convert to timestamp
        new Time(ts)
    }

    override def getTime(columnLabel: String): Time = {
        getTime(map(columnLabel))
    }

    override def getTimestamp(columnLabel :String): Timestamp = {
        getTimestamp(map(columnLabel))
    }

    override def getTimestamp(columnIndex :Int): Timestamp = {
        new Timestamp(currentRow(columnIndex - 1).asInstanceOf[Long])
    }

    override def getString(columnLabel: String): String = {
        getString(map(columnLabel))
    }

    override def getBoolean(columnLabel: String): Boolean = {
        getBoolean(map(columnLabel))
    }

    override def getShort(columnLabel: String): Short = {
        getShort(map(columnLabel))
    }

    override def getInt(columnLabel :String): Int = {
        getInt(map(columnLabel))
    }

    override def getLong(columnLabel: String): Long = {
        getLong(map(columnLabel))
    }

    override def getFloat(columnLabel: String): Float = {
        getFloat(map(columnLabel))
    }

    override def getDouble(columnLabel: String): Double = {
        getDouble(map(columnLabel))
    }

    override def getMetaData: ResultSetMetaData = {
        new JdbcResultSetMetaData(map)
    }


    override def getObject(columnIndex: Int): AnyRef = {
        currentRow(columnIndex - 1).asInstanceOf[AnyRef]
    }

    override def getObject(columnLabel: String): AnyRef = {
        getObject(map(columnLabel))
    }

    override def getBigDecimal(columnIndex: Int): java.math.BigDecimal = {
        currentRow(columnIndex - 1).asInstanceOf[java.math.BigDecimal]
    }

    override def getBigDecimal(columnLabel: String): java.math.BigDecimal = {
        getBigDecimal(map(columnLabel))
    }

    override def getArray(columnIndex: Int): sql.Array = {
        val array: Array[Any] = currentRow(columnIndex - 1).asInstanceOf[scala.Array[Any]]
        new EsJdbcArray[Any](array)
    }

    override def getArray(columnLabel: String): sql.Array = {
        val array: Array[Any] = currentRow(map(columnLabel) - 1).asInstanceOf[scala.Array[Any]]
        new EsJdbcArray[Any](array)
    }


}
