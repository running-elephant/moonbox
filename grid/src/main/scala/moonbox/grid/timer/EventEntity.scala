package moonbox.grid.timer

import java.util.Date

object EventEntity {
	val FUNC = "function"
	val DEFINER = "definer"
	val SQLS = "sqls"
}

case class EventEntity(
	group: String,
	name: String,
	sqls: Seq[String],
	cronExpr: String,
	definer: String,
	start: Option[Date],
	end: Option[Date],
	desc: Option[String],
	function: Function0[Unit]
)
