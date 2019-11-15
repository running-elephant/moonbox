package moonbox.client


class MbRowSet(iterator: Iterator[MbRow], schema: Schema) extends Iterator[MbRow] {

	def columnIndex(name: String): Int = schema.columnIndex(name)

	def columnCount: Int = schema.length

	def columnName(index: Int): String = {
		checkIndex(index)
		schema.columnName(index)
	}

	def columnTypeName(index: Int): String = {
		checkIndex(index)
		schema.columnTypeName(index)
	}

	def columnDataType(index: Int): DataType = {
		checkIndex(index)
		schema.columnDataType(index)
	}

	def nullable(index: Int): Boolean = {
		checkIndex(index)
		schema.nullable(index)
	}

	private def checkIndex(index: Int): Unit = {
		if (index < 0 || index >= columnCount)
			throw new IllegalArgumentException(s"Index $index out of range 0 to $columnCount")
	}

	override def hasNext: Boolean = iterator.hasNext

	override def next(): MbRow = iterator.next()

}
