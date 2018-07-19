package moonbox.repl.adapter

trait Connector {

  var max_count: Int = 500 /* max rows to show in console */
  var truncate: Int = 50 /* the column length to truncate, 0 denotes unabridged */

  def prepare(host: String, port: Int, user: String, pwd: String, db: String): Boolean
  def process(sqls: Seq[String]): Unit
  def close(): Unit
  def shutdown(): Unit
  def cancel(): Unit
}
