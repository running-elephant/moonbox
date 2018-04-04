package moonbox.repl

trait QueryMode
case object Adhoc extends QueryMode
case object Batch extends QueryMode
