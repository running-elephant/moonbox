package moonbox.catalyst.adapter.mongo.schema

import org.apache.spark.sql.types.DataType

/**
  * A type marking fields in the document structure to schema translation process that should be skipped.
  *
  * The main example use case is skipping a field with an empty array as the value.
  */
class SkipFieldType private () extends DataType {
  // The companion object and this class is separated so the companion object also subclasses
  // this type. Otherwise, the companion object would be of type "SkipFieldType$" in byte code.
  // Defined with a private constructor so the companion object is the only possible instantiation.
  override def defaultSize: Int = 1

  override def asNullable: SkipFieldType = this
}

case object SkipFieldType extends SkipFieldType
