package moonbox.catalyst.core

import org.apache.spark.sql.catalyst.expressions.Expression
import org.apache.spark.sql.types.StructType

case class ProjectElement(colName: String, colJson: String, aliasName: String, pushDown: Boolean)

class CatalystContext {
  //ES
  var nestFields = Set.empty[String] //es nest fields name
  var version = Seq.empty[Int] //es version
  def isES50: Boolean = version.headOption.getOrElse(5) == 5

  var hasLimited: Boolean = false //es has default limit
  var filterFunctionSeq: Seq[Expression] = Seq.empty[Expression]
  var projectFunctionSeq: Seq[(Expression, Int)] = Seq.empty[(Expression, Int)]
  var projectElementMap: Map[Int, ProjectElement] = Map.empty[Int, ProjectElement]
  var aggElementMap: Map[Int, ProjectElement] = Map.empty[Int, ProjectElement]
  var hasAgg = false

  def colId2aliasMap: Map[Int, String] = {
    if (hasAgg) {
      aggElementMap.map { elem => (elem._1, elem._2.aliasName) }
    }
    else {
      projectElementMap.map { elem => (elem._1, elem._2.aliasName) }
    }
  }

  def colId2colNameMap: Map[Int, String] = {
    if (hasAgg) {
      aggElementMap.map { elem => (elem._1, elem._2.colName) }
    }
    else {
      projectElementMap.map { elem => (elem._1, elem._2.colName) }
    }
  }

  def colName2colIdMap: Map[String, Int] = {
    colId2colNameMap.map(elem => (elem._2, elem._1))
  }

  //Mongo
  var tableSchema: StructType = _
  val index2FieldName = collection.mutable.Map[Int, String]()
}


object CatalystContext {

}