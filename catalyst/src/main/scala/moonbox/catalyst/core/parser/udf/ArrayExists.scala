/*-
 * <<
 * Moonbox
 * ==
 * Copyright (C) 2016 - 2019 EDP
 * ==
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * >>
 */

package moonbox.catalyst.core.parser.udf

import org.apache.spark.sql.catalyst.expressions.Expression
import org.apache.spark.sql.catalyst.analysis.TypeCheckResult
import org.apache.spark.sql.catalyst.expressions.BinaryExpression
import org.apache.spark.sql.catalyst.expressions.codegen.CodegenFallback
import org.apache.spark.sql.catalyst.util.ArrayData
import org.apache.spark.sql.types._
import org.apache.spark.sql.udf.JaninoCodeGen


case class ArrayExists(left: Expression, right: Expression) extends BinaryExpression with CodegenFallback {
    override def dataType: DataType = BooleanType

    override def checkInputDataTypes(): TypeCheckResult = {
        TypeCheckResult.TypeCheckSuccess
    }

    override def nullable: Boolean = {
        left.nullable || right.nullable || left.dataType.asInstanceOf[ArrayType].containsNull
    }

    override def nullSafeEval(array: Any, value: Any): Any = {
        val elementType = left.dataType.asInstanceOf[ArrayType].elementType
        val data = array.asInstanceOf[ArrayData].toArray[AnyRef](elementType)
        if (elementType != NullType) {
            val convertedLambda = ExprUtil.genArrayExistsCode(right, elementType)
            val a = JaninoCodeGen.exists(data, elementType, convertedLambda)
            a
        }
        else {
            null
        }
    }


    override def prettyName: String = "array_exists"
}
