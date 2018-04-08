package moonbox.catalyst.adapter.mongo

import org.apache.spark.sql.catalyst.analysis.UnresolvedAttribute
import org.apache.spark.sql.catalyst.expressions.aggregate._
import org.apache.spark.sql.catalyst.expressions.{Abs, Add, Alias, And, AttributeReference, BinaryArithmetic, BinaryComparison, BinaryExpression, Cast, Divide, Expression, GetStructField, IsNotNull, IsNull, LeafExpression, Literal, Multiply, Not, Or, Pmod, SortOrder, Subtract, UnaryExpression}
import org.apache.spark.sql.catalyst.plans.logical.{Aggregate, LogicalPlan}
import org.apache.spark.sql.types.StringType

trait MongoTranslateSupport {

  var containsId: Boolean = false
  var containsAggregate: Boolean = false
  var with2Dollar: Boolean = false

  def withQuotes(bson: String): String = {
    s""""${bson}"""".trim
  }

  def normalizeAlias(bson: String): String = {
    s""""${bson}"""".trim
  }

  // TODO: $year,$month,$week,$dayOfMonth,dayOfWeek,$dayOfYear,$hour,$minute,$second
  def symbolToBson(symobl: String, reverse: Boolean = false): String = {
    // TODO: not capture the Type "EqualNullSafe"
    symobl match {
      case ">" => if (!reverse) "$gt" else "$lt"
      case ">=" => if (!reverse) "$gte" else "$lte"
      case "<" => if (!reverse) "$lt" else "$gt"
      case "<=" => if (!reverse) "$lte" else "$gte"
      case "=" => "$eq"
      case "!=" => "$ne"
      case "max" | "MAX" => "$max"
      case "min" | "MIN" => "$min"
      case "avg" | "average" => "$avg"
      case "count" | "COUNT" => "$count"
      case "sum" | "SUM" => "$sum"
      case "first" | "FIRST" => "$first"
      case "last" | "LAST" => "$last"
      case "limit" | "LIMIT" => "$limit"
      case "sort" | "SORT" => "$sort"
      case "abs" | "ABS" => "$abs"
      case "+" | "add" | "ADD" => "$add"
      case "-" | "subtract" | "SUBTRACT" => "$subtract"
      case "*" | "multiply" | "MULTIPLY" => "$multiply"
      case "/" | "divide" | "DIVIDE" => "$divide"
      case "pmod" | "mod" | "MOD" => "$mod"
    }
  }

  def binaryArithmeticToBson(b: BinaryArithmetic): String = {
    // supported: Add, Subtract, Multiply, Divide
    // TODO: unsupported: BitwiseOr, BitwiseAnd, BitwiseXor, Pmod, Remainder ?
    b match {
      case a@Add(left, right) =>
        s"{${symbolToBson(a.symbol)}: [${withQuotesAndDollar(left)}, ${withQuotesAndDollar(right)}] }"
      case s@Subtract(left, right) =>
        s"{${symbolToBson(s.symbol)}: [${withQuotesAndDollar(left)}, ${withQuotesAndDollar(right)}] }"
      case m@Multiply(left, right) =>
        s"{${symbolToBson(m.symbol)}: [${withQuotesAndDollar(left)}, ${withQuotesAndDollar(right)}] }"
      case d@Divide(left, right) =>
        s"{${symbolToBson(d.symbol)}: [${withQuotesAndDollar(left)}, ${withQuotesAndDollar(right)}] }"
      case d@Pmod(left, right) =>
        s"{${symbolToBson(d.symbol)}: [${withQuotesAndDollar(left)}, ${withQuotesAndDollar(right)}] }"
    }
  }

  def binaryComparisonToBson(b: BinaryComparison, left: Expression, right: Expression): String = {
    s"{${expressionToBson(left)}: {${symbolToBson(b.symbol)}: ${expressionToBson(right)}}}"
  }

  def binaryExpressionToBson(binaryExpression: BinaryExpression): String = {
    binaryExpression match {
      case And(con1, con2) =>
        "{$and: [" + expressionToBson(con1) + ", " + expressionToBson(con2) + "]}"
      case Or(con1, con2) =>
        "{$or: [" + expressionToBson(con1) + ", " + expressionToBson(con2) + "]}"
      case b: BinaryComparison =>
        b.left match {
          case AttributeReference(_, _, _, _) =>
            s"{${withQuotes(expressionToBson(b.left))}: {${symbolToBson(b.symbol)}: ${expressionToBson(b.right)}}}"
          // handle expression in UDF
          case UnresolvedAttribute(_) =>
            s"{${withQuotes(expressionToBson(b.left))}: {${symbolToBson(b.symbol)}: ${expressionToBson(b.right)}}}"
          case g: GetStructField =>
            s"{${expressionToBson(b.left)}: {${symbolToBson(b.symbol)}: ${expressionToBson(b.right)}}}"
          case _ =>
            s"{${withQuotes(expressionToBson(b.right))}: {${symbolToBson(b.symbol, reverse = true)}: ${expressionToBson(b.left)}}}"
        }
      case b: BinaryArithmetic =>
        // Add, Subtract, Multiply, Divide, BitwiseOr, BitwiseAnd, BitwiseXor, Pmod, Remainder
        binaryArithmeticToBson(b)
    }
  }

  def withQuotesAndDollar(expression: Expression): String = {
    expression match {
      case l: Literal => expressionToBson(l)
      case _ =>
        val prefix = if (!with2Dollar) {
          "\"$"
        }
        else {
          with2Dollar = false
          "\"$$"
        }
        prefix + expressionToBson(expression) + "\""
    }
  }

  def nestedDocumentToBson(g: GetStructField, nestedFields: List[String]): List[String] = {
    val name = g.name.getOrElse(throw new Exception("nested field error."))
    val newNestedFields = name :: nestedFields
    g.child match {
      case s: GetStructField =>
        nestedDocumentToBson(s, newNestedFields)
      case a: AttributeReference =>
        a.name :: newNestedFields
    }
  }

  /**
    *
    * @param g
    * @return a string with the format: "a.b.c"
    */
  def nestedDocumentToBson(g: GetStructField): String = {
    withQuotes(nestedDocumentToBson(g, List[String]()).mkString("."))
  }

  def unaryExpressionToBson(unaryExpression: UnaryExpression): String = {
    unaryExpression match {
      case IsNotNull(child) =>
        s"{${withQuotes(expressionToBson(child))}: {${symbolToBson("!=")}: null}}"
      case IsNull(child) =>
        s"{${withQuotes(child.asInstanceOf[AttributeReference].name)}: {${symbolToBson("=")}: null}}"
      case Not(child) =>
        notExpressionToBson(child)
      case order: SortOrder =>
        var fieldName = expressionToBson(order.child)
        if (containsAggregate)
          fieldName = s"_id.${fieldName}"
        if (order.isAscending)
          s"${withQuotes(fieldName)}: 1"
        else
          s"${withQuotes(fieldName)}: -1"
      case Abs(child) =>
        s"{${symbolToBson("abs")}: ${expressionToBson(child)}}"
      case Alias(child, name) =>
        s"${normalizeAlias(name)}: ${expressionToBson(child)}"
      case g: GetStructField => // Multilayer nested: "parent.parent. ... .name"
        nestedDocumentToBson(g)
      case c: Cast =>
        expressionToBson(c.child)
    }
  }

  //  def scalaUDFToBson(s: ScalaUDF): String = {
  //    //    val funArgs1 = expressionToBson(s.children.head)
  //    val funArgs2 = s.children.last
  //    val expression = parseExpression(funArgs2.asInstanceOf[Literal].value.toString)
  //    expressionToBson(expression)
  //  }

  //  def parseExpression(expression: String): Expression = {
  //    val parser = new MbUDFParser(expression)
  //    val visitor = parser.visitor
  //    visitor.visit(parser.parser.arrowAndExpression())
  //    val parsedExpression = visitor.expression
  //    if (parsedExpression != null) {
  //      // TODO: parse the map function arguments with spark parser
  ////      sparkParser.parse(parsedExpression)
  //      null
  //    } else {
  //      throw new Exception("parse udf args error")
  //    }
  //  }

  def expressionToBson(expression: Expression): String = {
    expression match {
      case b: BinaryExpression =>
        binaryExpressionToBson(b)
      case u: UnaryExpression =>
        unaryExpressionToBson(u)
      case l: LeafExpression =>
        l match {
          case a: AttributeReference =>
            a.name
          case literal: Literal =>
            if (literal.dataType.isInstanceOf[StringType])
              withQuotes(literal.value.toString)
            else
              literal.value.toString
          case u: UnresolvedAttribute =>
            u.nameParts.mkString(".")
        }
      case a: AggregateExpression =>
        aggregateExpressionToBson(a)
      // TODO: ScalaUDF support
      //      case s: ScalaUDF =>
      //        scalaUDFToBson(s)
      case other =>
        throw new Exception(s"unsupported expression: ${other}")
    }
  }

  def aggregateExpressionToBson(expression: AggregateExpression): String = {
    if (!expression.isDistinct)
      aggregateFunctionToBson(expression.aggregateFunction)
    else {
      // TODO: capture the "DISTINCT" key word.
      aggregateFunctionToBson(expression.aggregateFunction)
    }
  }

  def aggregateFunctionToBson(expression: Expression): String = {
    expression match {
      case Max(child) => s"{${symbolToBson("max")}: ${withQuotesAndDollar(child)}}"
      case Min(child) => s"{${symbolToBson("min")}: ${withQuotesAndDollar(child)}}"
      case Average(child) => s"{${symbolToBson("avg")}: ${withQuotesAndDollar(child)}}"
      case Count(_) => s"{${symbolToBson("sum")}: 1}"
      case Sum(child) => s"{${symbolToBson("sum")}: ${withQuotesAndDollar(child)}}"
      case First(child, _) => s"{${symbolToBson("first")}: ${withQuotesAndDollar(child)}}"
      case Last(child, _) => s"{${symbolToBson("last")}: ${withQuotesAndDollar(child)}}"
    }
  }

  def extractFieldName(expr: Expression): String = {
    expr match {
      case b: BinaryComparison =>
        b.left match {
          case a: AttributeReference =>
            a.name
          case _ =>
            b.right.asInstanceOf[AttributeReference].name
        }
    }
  }

  def notExpressionToBson(expr: Expression): String = {
    val fieldName = extractFieldName(expr)
    "{" + fieldName + ": {$not: " + stripAttributeName(expressionToBson(expr)) + "}}"
  }

  def stripAttributeName(bson: String): String = {
    bson.substring(bson.indexOf(":") + 1, bson.length - 1).trim // {price: {$eq: 50}} -> {price: {$not: {$eq: 50}}}
  }

  def noAggregates(logicalPlan: LogicalPlan): Boolean = {
    var flag = true
    flag = logicalPlan match {
      case _: Aggregate =>
        false
      case other =>
        other.children.forall(noAggregates)
    }
    flag
  }
}
