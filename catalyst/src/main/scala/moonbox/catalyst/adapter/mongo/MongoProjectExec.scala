package moonbox.catalyst.adapter.mongo

import moonbox.catalyst.core.CatalystContext
import moonbox.catalyst.core.parser.SqlParser
import moonbox.catalyst.core.plan.{CatalystPlan, ProjectExec}
import moonbox.catalyst.core.parser.udf.ArrayMap
import moonbox.catalyst.core.parser.udf.udfParser.ParserUtil
import org.apache.spark.sql.catalyst.expressions.{Alias, AttributeReference, GetStructField, Literal, NamedExpression}

class MongoProjectExec(projectList: Seq[NamedExpression], child: CatalystPlan)
  extends ProjectExec(projectList: Seq[NamedExpression], child: CatalystPlan) with MongoTranslateSupport {
  override def translate(context: CatalystContext) = {
    var fields = List[String]()
    for (expression <- projectList) {
      expression match {
        case a: AttributeReference =>
          val fieldName = expressionToBson(a)
          fields = s"${withQuotes(fieldName)}: 1" :: fields
        case alias: Alias =>
          alias.child match {
            case g: GetStructField =>
              fields = s"""${nestedDocumentToBson(g)}: 1""" :: fields
            case arrayMap: ArrayMap =>
              fields = withQuotes(alias.name) + ": {$map: {" + handleArrayMap(arrayMap) + "}}" :: fields
            case other =>
              fields = s"${withQuotes(alias.name)}: " + expressionToBson(other) :: fields
          }
        case other =>
          fields = expressionToBson(other) :: fields
      }
    }

    val projectBson =
      if (fields.nonEmpty)
        fields.mkString("{$project: {_id: 0, ", ", ", "}}")
      else
        "{$project: {_id: 1}}"
    val res: Seq[String] = child.translate(context) :+ projectBson
    res
  }

  def handleArrayMap(arrayMap: ArrayMap): String = {
    var input = expressionToBson(arrayMap.left)
    val asVal: String = "as_value_holder"
    if (input.length >= 2)
      input = input.substring(1, input.length - 1)
    input = withQuotes("$" + input)

    val mapExprString = arrayMap.right match {
      case a: Literal =>
        a.value.toString
      case _ =>
        throw new IllegalArgumentException("array_map argument error")
    }
    val rep = mapExprString.replaceAll(ParserUtil.VARIABLE_NAME, asVal)
    val mapExpression = new SqlParser().parser.parseExpression(rep)
    with2Dollar = true
    s"input: ${input}, as: ${withQuotes(asVal)}, in: ${expressionToBson(mapExpression)}"
  }

  def getArrayMapPrettyName(arrayMap: ArrayMap): String ={
    withQuotes(s"${arrayMap.prettyName}(${arrayMap.left.toString}, ${arrayMap.right.toString})")
  }
}
