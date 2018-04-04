package moonbox.catalyst.adapter.mongo.schema

import java.io.Serializable

import org.apache.spark.sql.types.DataType

class ConflictType private () extends DataType with Serializable {
  def defaultSize: Int = 0

  def asNullable: DataType = this
  override def toString: String = "ConflictType"
}

case object ConflictType extends ConflictType
