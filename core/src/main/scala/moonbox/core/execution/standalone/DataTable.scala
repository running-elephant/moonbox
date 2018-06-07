package moonbox.core.execution.standalone

import org.apache.spark.sql.types.StructType

class DataTable[T](data: Iterator[T], schema: StructType)
