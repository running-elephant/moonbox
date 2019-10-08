package moonbox.grid.deploy.app

object AppType {

	def apply(`type`: String): AppType = {
		if (`type`.equalsIgnoreCase("CENTRALIZED")) {
			CENTRALIZED
		} else {
			DISTRIBUTED
		}
	}

	case object CENTRALIZED extends AppType

	case object DISTRIBUTED extends AppType
}

trait AppType