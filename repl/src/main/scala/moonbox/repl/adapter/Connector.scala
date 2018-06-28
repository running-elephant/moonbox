package moonbox.repl.adapter

trait Connector {
  def prepare(host: String, port:Int, user: String, pwd: String, db: String): Boolean
  def process(sqls: Seq[String]): Unit
  def close(): Unit
  def shutdown(): Unit
  def cancel(): Unit
}
