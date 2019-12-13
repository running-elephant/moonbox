package moonbox.grid.deploy.rest.routes

import moonbox.catalog.AbstractCatalog.User
import moonbox.grid.deploy.security.Session

import scala.language.implicitConversions

trait SessionConverter {

	implicit def sessionConverter(session: Session): User = {
		User(session("orgId").toLong, session("org"), session("userId").toLong, session("user"))
	}
}
