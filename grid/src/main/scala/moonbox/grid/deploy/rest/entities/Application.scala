package moonbox.grid.deploy.rest.entities

case class Application(appName: String, appType: String, state: Option[String], config: Map[String, String])
