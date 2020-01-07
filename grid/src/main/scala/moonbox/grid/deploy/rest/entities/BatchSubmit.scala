package moonbox.grid.deploy.rest.entities

case class BatchSubmit(name: String, typeName: String, script: Seq[String], config: Map[String, String])

