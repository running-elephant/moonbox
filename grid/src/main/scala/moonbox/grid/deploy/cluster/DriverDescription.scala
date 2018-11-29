package moonbox.grid.deploy.cluster

private[deploy] case class DriverDescription(
	username: String,
	sqls: Seq[String],
	config: String) {
	override def toString: String = {
		s"DriverDescription ($username ${sqls.mkString(";")})"
	}
}
