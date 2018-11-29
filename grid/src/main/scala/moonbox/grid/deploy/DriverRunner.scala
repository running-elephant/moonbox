package moonbox.grid.deploy

trait DriverRunner {
	def start(): Unit

	def kill(): Unit
}
