package moonbox.catalyst.core

import org.apache.spark.sql.catalyst.expressions.Expression
import org.apache.spark.sql.types.StructType

class CatalystContext {
  //ES
  var nestFields = Set.empty[String] //es nest fields name
  var version = Seq.empty[Int] //es version
  def isES50: Boolean = version.headOption.getOrElse(5) == 5

  var hasLimited: Boolean = false //es has default limit
  var filterFunctionSeq: Seq[Expression] = Seq.empty[Expression]
  var projectFunctionSeq: Seq[(Expression, Int)] = Seq.empty[(Expression, Int)]

  var projectElementSeq: Seq[(String, String)] = Seq.empty[(String, String)]
  var aggElementSeq: Seq[(String, String)] = Seq.empty[(String, String)]
  var hasAgg = false


  //Mongo
  var tableSchema: StructType = _
  val index2FieldName = collection.mutable.Map[Int, String]()
}


object CatalystContext {

}