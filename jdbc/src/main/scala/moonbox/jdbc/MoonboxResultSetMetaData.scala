package moonbox.jdbc

import java.sql.ResultSetMetaData

import moonbox.util.SchemaUtil

class MoonboxResultSetMetaData(resultSet: MoonboxResultSet,
                               originalSchemaJson: String
                              ) extends ResultSetMetaData {

  import SchemaUtil._

  private lazy val parsedSchema: Array[(String, Int, Boolean)] = schema2SqlType(parse(originalSchemaJson))
  private lazy val columnCount: Int = parsedSchema.length

  override def getSchemaName(column: Int) = ""

  override def getCatalogName(column: Int) = resultSet.getStatement.jdbcSession.database

  override def isSigned(column: Int) = true

  override def getColumnLabel(column: Int) = parsedSchema(column - 1)._1

  override def getColumnName(column: Int) = parsedSchema(column - 1)._1

  override def getColumnTypeName(column: Int) = null

  override def isWritable(column: Int) = false

  override def getColumnClassName(column: Int) = null

  override def isAutoIncrement(column: Int) = false

  override def isReadOnly(column: Int) = true

  override def isCurrency(column: Int) = true

  override def isSearchable(column: Int) = true

  override def isCaseSensitive(column: Int) = true

  override def getTableName(column: Int) = resultSet.getStatement.jdbcSession.table

  override def getColumnType(column: Int) = parsedSchema(column - 1)._2

  override def isDefinitelyWritable(column: Int) = false

  override def getColumnCount = columnCount

  override def getPrecision(column: Int) = 0

  override def getScale(column: Int) = {
    if (parsedSchema(column - 1)._2 != java.sql.Types.DECIMAL)
      0
    else {
      val presAndScale = parsedSchema(column - 1)._1.stripPrefix("decimal(").stripSuffix(")").split(",")
      if (presAndScale.length == 2)
        presAndScale(1).toInt
      else 0
    }
  }

  override def isNullable(column: Int) = {
    if (parsedSchema(column - 1)._3)
      ResultSetMetaData.columnNullable
    else
      ResultSetMetaData.columnNoNulls
  }

  override def getColumnDisplaySize(column: Int) = 0

  override def unwrap[T](iface: Class[T]) = null.asInstanceOf[T]

  override def isWrapperFor(iface: Class[_]) = false

}
