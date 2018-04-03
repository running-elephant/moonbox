package moonbox.jdbc

import scala.collection.mutable.ArrayBuffer

class MoonboxJdbcRow {

  var wasNull: Boolean = _

  def this(value: Any*) = {
    this()
    values ++= value
  }

  lazy val values: ArrayBuffer[Any] = new ArrayBuffer()

  def iterator = values.iterator

  def length = values.size

  def apply(i: Int) = values(i)

  def get(i: Int): Any = {
    wasNull = values(i) == null
    values(i)
  }

  def isNullAt(i: Int) = values(i) == null

  def getInt(i: Int): Int = get(i).asInstanceOf[Int]

  def getLong(i: Int): Long = get(i).asInstanceOf[Long]

  def getDouble(i: Int): Double = get(i).asInstanceOf[Double]

  def getFloat(i: Int): Float = get(i).asInstanceOf[Float]

  def getBoolean(i: Int): Boolean = get(i).asInstanceOf[Boolean]

  def getShort(i: Int): Short = get(i).asInstanceOf[Short]

  def getByte(i: Int): Byte = get(i).asInstanceOf[Byte]

  def getString(i: Int): String = get(i).toString

  def getAs[T](i: Int): T = get(i).asInstanceOf[T]

  def copy() = this

  def toSeq = values.toSeq
}
