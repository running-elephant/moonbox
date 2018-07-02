package moonbox.core.datasys

trait DataSystemProvider {
	def createDataSystem(parameters: Map[String, String]): DataSystem
}
