package moonbox.grid.deploy.authenticate

import moonbox.common.MbLogging

trait Login extends MbLogging {
	def doLogin(username: String, password: String): Boolean
}
