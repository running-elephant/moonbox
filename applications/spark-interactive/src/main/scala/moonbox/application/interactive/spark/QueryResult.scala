package moonbox.application.interactive.spark

trait QueryResult
case class DirectResult(schema: String, data: Seq[Seq[Any]]) extends QueryResult
case class IndirectResult(schema: String) extends QueryResult
