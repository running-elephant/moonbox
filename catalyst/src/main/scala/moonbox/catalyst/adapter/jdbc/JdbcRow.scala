package moonbox.catalyst.adapter.jdbc

import org.apache.spark.sql.Row

import scala.collection.mutable.ArrayBuffer

class JdbcRow extends Row {
  def this(value: Any*) = {
    this()
    values ++= (value)
  }

  lazy val values: ArrayBuffer[Any] = new ArrayBuffer()

  def iterator = values.iterator

  override def length = values.size

  override def apply(i: Int) = values(i)

  override def get(i: Int): Any = values(i)

  override def isNullAt(i: Int) = values(i) == null

  override def getInt(i: Int): Int = getAs[Int](i)

  override def getLong(i: Int): Long = getAs[Long](i)

  override def getDouble(i: Int): Double = getAs[Double](i)

  override def getFloat(i: Int): Float = getAs[Float](i)

  override def getBoolean(i: Int): Boolean = getAs[Boolean](i)

  override def getShort(i: Int): Short = getAs[Short](i)

  override def getByte(i: Int): Byte = getAs[Byte](i)

  override def getString(i: Int): String = get(i).toString

  def copy() = this

  override def toSeq = values.toSeq
}
