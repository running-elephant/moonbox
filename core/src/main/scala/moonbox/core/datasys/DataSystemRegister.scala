package moonbox.core.datasys

trait DataSystemRegister {
	DataSystem.registerDataSource(shortName(), dataSource())

	def shortName(): String
	def dataSource(): String
}
