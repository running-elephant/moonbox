package moonbox.catalyst.core

trait Schema {
	def getTableMetaData(name: String): TableMetaData
	def getTableNames: Seq[String]
    def getTableNames2: Seq[(String, String)]
	def getFunctionNames: Seq[String]
	def getVersion: Seq[Int]
}
