package moonbox.grid.deploy

trait DriverDescription {
	def master: String
	def deployMode: Option[String]
	def mainClass: String
}

class ClientDriverDescription extends DriverDescription {
	override def master = "yarn"
	override def deployMode = Some("client")
	override def mainClass = "moonbox.application.cluster.Main"
}

private[deploy] case class ClusterDriverDescription(
	username: String,
	sqls: Seq[String],
	config: String) extends DriverDescription {

	override def master = "yarn"
	override def deployMode = Some("cluster")
	override def mainClass = "moonbox.application.cluster.Main"

	override def toString: String = {
		s"DriverDescription ($username ${sqls.mkString(";")})"
	}
}

class LocalDriverDescription extends DriverDescription {
	override def master = {
		val cores = Runtime.getRuntime.availableProcessors()
		s"local[${cores * 50}]"
	}
	override def deployMode = None
	override def mainClass = "moonbox.application.cluster.Main"
}

