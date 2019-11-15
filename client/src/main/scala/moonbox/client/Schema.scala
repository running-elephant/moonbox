package moonbox.client

class Schema(columnSchema: Seq[ColumnSchema]) {

	def isEmpty: Boolean = columnSchema.isEmpty

	def length: Int = columnSchema.length

	def columnIndex(name: String): Int = {
		columnSchema.lastIndexWhere(_.name.equalsIgnoreCase(name))
	}

	def columnName(index: Int): String = {
		columnSchema(index).name
	}

	def columnTypeName(index: Int): String = {
		columnSchema(index).dt.typeName
	}

	def columnDataType(index: Int): DataType = {
		columnSchema(index).dt
	}

	def nullable(index: Int): Boolean = {
		columnSchema(index).nullable
	}

}
