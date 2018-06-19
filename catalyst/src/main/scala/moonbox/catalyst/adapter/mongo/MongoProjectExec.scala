package moonbox.catalyst.adapter.mongo

import moonbox.catalyst.core.CatalystContext
import moonbox.catalyst.core.parser.SqlParser
import moonbox.catalyst.core.parser.udf.{ArrayExists, ArrayFilter, ArrayMap}
import moonbox.catalyst.core.parser.udf.udfParser.ParserUtil
import moonbox.catalyst.core.plan.{CatalystPlan, ProjectExec}
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
              fields = withQuotes(alias.name) + ": {$map: " + handleArrayMap(arrayMap) + "}" :: fields
              with2Dollar = false
            case arrayFilter: ArrayFilter =>
              fields = withQuotes(alias.name) + ": {$filter: " + handleArrayFilter(arrayFilter) + "}" :: fields
              with2Dollar = false
            case AttributeReference(name, _, _, _) =>
              fields = s"${withQuotes(alias.name)}: " + "\"" + s"$$$name" + "\"" :: fields
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

  private def handleArrayMap(arrayMap: ArrayMap): String = {
    var input = expressionToBson(arrayMap.left)
    val asVal: String = "array_map_as_holder"
    if (input.startsWith("\""))
      input = input.replaceAll("\"","")
    input = withQuotes("$" + input)
    val mapExprString = arrayMap.right match {
      case a: Literal =>
        a.value.toString
      case _ =>
        throw new IllegalArgumentException("array_map argument error")
    }
    val rep = mapExprString.replaceAll(ParserUtil.VARIABLE_NAME, asVal)
    // TODO: get parser from session
    val mapExpression = new SqlParser().parser.parseExpression(rep)
    with2Dollar = true
    s"{input: ${input}, as: ${withQuotes(asVal)}, in: ${expressionToBson(mapExpression)}}"
  }

  private def handleArrayFilter(arrayFilter: ArrayFilter): String = {
    var input = expressionToBson(arrayFilter.left)
    val asVal = "array_filter_as_holder"
    if (input.startsWith("\"")){
      input = input.replaceAll("\"", "")
    }
    input = withQuotes("$" + input)
    val filterExprString = arrayFilter.right match {
      case a: Literal =>
        a.value.toString
      case _ =>
        throw new IllegalArgumentException("array_map argument error")
    }
    val rep = filterExprString.replaceAll(ParserUtil.VARIABLE_NAME, asVal)
    // TODO: get parser from session
    val filterExpression = new SqlParser().parser.parseExpression(rep)
    with2Dollar = true
    s"{input: $input, as: ${withQuotes(asVal)}, cond: ${predicateToBson(filterExpression)}}"
  }

  def getArrayMapPrettyName(arrayMap: ArrayMap): String = {
    withQuotes(s"${arrayMap.prettyName}(${arrayMap.left.toString}, ${arrayMap.right.toString})")
  }
}
