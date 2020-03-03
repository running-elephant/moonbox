package moonbox.application.interactive.spark

import org.apache.spark.sql.catalyst.catalog.{CatalogTable, CatalogTableType}
import org.apache.spark.sql.types.StructType

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

class LineageBuilder {

  private var id = 0

  private val tableNodeBuffer = ListBuffer.empty[DagNode]

  private val tableNodeMap = mutable.HashMap.empty[String, Int]

  private val tableNodeEdgeMap = mutable.HashMap.empty[Int, Int]

  def genTableNode(catalogTable: CatalogTable): Int = {
    val database = catalogTable.database
    val table = catalogTable.identifier.table
    val columns = schemaToNodeColumns(catalogTable.schema)
    val tableNodeKey = tableName(database, table)
    if (tableNodeMap.contains(tableNodeKey)) {
      tableNodeMap(tableNodeKey)
    } else {
      genCurrentId
      tableNodeMap.put(database + "." + table, id)
      tableNodeBuffer.append(DagNode(id,
        database = database,
        table = table,
        `type` = getTableType(catalogTable.tableType),
        desc = "",
        cols = Some(columns)
      ))
      id
    }
  }

  def genTableNodeEdges: Seq[DagEdge] = {
    tableNodeEdgeMap.keySet.map(sourceId => {
      DagEdge(source = SourceNode(sourceId),
        target = TargetNode(tableNodeEdgeMap(sourceId)))
    }).toSeq
  }

  def buildTableDag: DagEntity = {
    DagEntity(nodes = tableNodeBuffer, edges = genTableNodeEdges)
  }

  def putTableNodeEdge(sourceId: Int, targetId: Int): Unit = {
    if (!tableNodeEdgeMap.contains(sourceId)) {
      tableNodeEdgeMap.put(sourceId, targetId)
    }
  }

  private def schemaToNodeColumns(structType: StructType): Seq[TableColumn] = {
    structType.toList.map(structField =>
      TableColumn(structField.name, structField.dataType.simpleString, structField.nullable))
  }

  private def getTableType(tableType: CatalogTableType): String = {
    tableType match {
      case CatalogTableType.VIEW => "VIEW"
      case _ => "TABLE"
    }
  }

  private def genCurrentId: Int = {
    id += 1
    id
  }

  private def tableName(database: String, table: String): String = database + "." + table

}
