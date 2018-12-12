package moonbox.application.interactive

trait QueryResult
case class DirectResult(schema: String, data: Seq[Seq[Any]], hasMore: Boolean = false) extends QueryResult
case class IndirectResult() extends QueryResult
