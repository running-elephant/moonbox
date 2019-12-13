package moonbox.grid.deploy.rest.entities

case class Cluster(name: String, `type`: String, environment: Map[String, String], config: Map[String,String])