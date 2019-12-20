package moonbox.grid.deploy.rest.entities


case class ApplicationIn(
	appName: String,
	appType: String,
	config: Map[String, String],
	cluster: Option[String],
	org: String,
	worker: Option[String],
	startOnBoot: Boolean
)

case class ApplicationOut(
	org: String,
	appName: String,
	appType: String,
	config: Map[String, String],
	createTime: Option[String],
	updateTime: Option[String],
	startOnBoot: Boolean
)

case class ApplicationInfo(
	name : String,
	appType: String,
	startTime: Option[String],
	state: Option[String],
	worker: Option[String],
	exception: Option[String]
)
