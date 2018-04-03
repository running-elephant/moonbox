package moonbox.grid

import org.apache.spark.sql.Row

trait JobResult

case object CachedData extends JobResult
case class DirectData( data: Seq[Row]) extends JobResult
case object UnitData extends JobResult
case class Failed(message: String) extends JobResult