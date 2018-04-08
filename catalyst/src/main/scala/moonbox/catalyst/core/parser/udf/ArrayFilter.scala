package moonbox.catalyst.core.parser.udf

import org.apache.spark.sql.catalyst.expressions.{BinaryExpression, Literal}
import org.apache.spark.sql.catalyst.expressions.codegen.CodegenFallback
import org.apache.spark.sql.catalyst.util.GenericArrayData
import org.apache.spark.sql.catalyst.expressions.Expression
import org.apache.spark.sql.catalyst.analysis.TypeCheckResult
import org.apache.spark.sql.catalyst.util.ArrayData
import org.apache.spark.sql.types._
import org.apache.spark.sql.udf.JaninoCodeGen

case class ArrayFilter(left: Expression, right: Expression) extends BinaryExpression with CodegenFallback {

    def this(e: Expression) = this(e, Literal(true))
    override def dataType: DataType = left.dataType

    override def checkInputDataTypes(): TypeCheckResult = TypeCheckResult.TypeCheckSuccess


    override def nullSafeEval(array: Any, lambda: Any): Any = {
        val elementType = left.dataType.asInstanceOf[ArrayType].elementType
        val data = array.asInstanceOf[ArrayData].toArray[AnyRef](elementType)

        if (elementType != NullType) {
            val convertedLambda = ExprUtil.genArrayFilterCode(right, elementType)
            val newData: Array[AnyRef] = JaninoCodeGen.filter(data, elementType, convertedLambda)
            new GenericArrayData(newData.asInstanceOf[Array[Any]])
        }
        else {
            new GenericArrayData(data.asInstanceOf[Array[Any]])
        }
    }

    override def prettyName: String = "array_filter"
}
