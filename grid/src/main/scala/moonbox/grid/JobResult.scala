package moonbox.grid

trait JobResult

case object CachedData extends JobResult
case class DirectData( data: Seq[Seq[String]]) extends JobResult
case object UnitData extends JobResult
case class Failed(message: String) extends JobResult