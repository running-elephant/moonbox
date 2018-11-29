package moonbox.grid.deploy

private[deploy] case class ClusterDriverDescription(
	username: String,
	sqls: Seq[String],
	config: String) extends DriverDescription {
	override def toString: String = {
		s"DriverDescription ($username ${sqls.mkString(";")})"
	}
}
