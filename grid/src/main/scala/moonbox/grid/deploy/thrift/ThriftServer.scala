package moonbox.grid.deploy.thrift

abstract class ThriftServer {
	def start(): Int
	def stop(): Unit
}
