package moonbox.grid

trait JobResult

case class CachedData(jobId: String) extends JobResult
case class DirectData(data: Seq[Seq[String]]) extends JobResult
case class ExternalData(jobId: String) extends JobResult
case class Failed(message: String) extends JobResult