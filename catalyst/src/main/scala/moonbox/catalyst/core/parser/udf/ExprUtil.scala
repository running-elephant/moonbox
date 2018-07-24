/*-
 * <<
 * Moonbox
 * ==
 * Copyright (C) 2016 - 2018 EDP
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

import org.apache.spark.sql.catalyst.expressions.codegen.CodegenContext
import org.apache.spark.sql.catalyst.parser.CatalystSqlParser
import org.apache.spark.sql.catalyst.expressions._
import org.apache.spark.sql.types._

object ExprUtil {

    def genArrayExistsCode(expression: Expression, ctx: CodegenContext, dataType: DataType, c1: String, c2: String): String  = {
        val exp: Expression = CatalystSqlParser.parseExpression(expression.toString())
        translateExpression(exp, ctx, dataType, c1, c2)
    }

    private def mkExpression(ctx: CodegenContext, set:Set[Int], arrayElement: String,  literal: Literal) : String= {
        //notice: genComp return 0 1 -1
        set.map{elem => s"""(${ctx.genComp(literal.dataType, arrayElement, literal.genCode(ctx).value)}==$elem)"""}.mkString("||")
    }

    private def translateExpression(expression: Expression, ctx: CodegenContext, dataType: DataType, c1: String, c2: String): String  = {
        expression match {
            case EqualTo(e: Expression, l: Literal)             => mkExpression(ctx, Set(0), c2, l)
            case EqualTo(l: Literal, e: Expression)             => mkExpression(ctx, Set(0), c2, l)
            case GreaterThan(e: Expression, l: Literal)         => mkExpression(ctx, Set(1), c2, l)
            case GreaterThan(l: Literal, e: Expression)         => mkExpression(ctx, Set(-1), c2, l)
            case GreaterThanOrEqual(e: Expression, l: Literal)  => mkExpression(ctx, Set(0, 1), c2, l)
            case GreaterThanOrEqual(l: Literal, e: Expression)  => mkExpression(ctx, Set(-1, 0), c2, l)
            case LessThan(e: Expression, l: Literal)            => mkExpression(ctx, Set(-1), c2, l)
            case LessThan(l: Literal, e: Expression)            => mkExpression(ctx, Set(1), c2, l)
            case LessThanOrEqual(e: Expression, l: Literal)     => mkExpression(ctx, Set(-1, 0), c2, l)
            case LessThanOrEqual(l: Literal, e: Expression)     => mkExpression(ctx, Set(0, 1), c2, l)
            case EqualNullSafe(e: Expression, l: Literal)       => mkExpression(ctx, Set(-1, 0, 1), c2, l)
            case And(left, right)                               => s"""(${translateExpression(left, ctx, dataType, c1, c2)}) &&  (${translateExpression(right, ctx, dataType, c1, c2)})"""
            case Or(left, right)                                => s"""(${translateExpression(left, ctx, dataType, c1, c2)}) ||  (${translateExpression(right, ctx, dataType, c1, c2)})"""
            case IsNull(e: Expression)                          => ""  //TODO
            case IsNotNull(e: Expression)                       => ""
        }
    }

    //inSpark: spark and add datatype are different
    def genArrayExistsCode(expression: Expression, dataType: DataType, inSpark: Boolean = true ): String  = {
        val exp: Expression = CatalystSqlParser.parseExpression(expression.toString())
        translateExpression2(exp, dataType, inSpark)
    }

    def genArrayFilterCode(expression: Expression, dataType: DataType, inSpark: Boolean = true): String  = {
        val exp: Expression = CatalystSqlParser.parseExpression(expression.toString())
        translateExpression2(exp, dataType, inSpark)
    }

    private def getTypeFunction(dataType: DataType, inSpark: Boolean): (String, String, String, String) = {
        dataType match {
            case StringType =>
                if (inSpark) {
                    ("UTF8String.fromString", "compare", "UTF8String", "toString" )
                } else {
                    ("String.valueOf", "compareTo", "String", "toString" )
                }
            case LongType =>    ("Long.valueOf",    "compareTo", "Long", "longValue")
            case IntegerType => ("Integer.valueOf", "compareTo", "Integer", "intValue")
            case FloatType =>   ("Float.valueOf",   "compareTo", "Float", "floatValue")
            case ShortType =>   ("Short.valueOf",   "compareTo", "Short", "shortValue")
            case DoubleType =>  ("Double.valueOf",  "compareTo", "Double", "doubleValue")
            case BooleanType => ("Boolean.valueOf", "compareTo", "Boolean", "booleanValue")
            //case DecimalType => "Decimal.valueOf"  //spark reserved
            case a: ArrayType => getTypeFunction(a.elementType, inSpark)  //array type [xx, yy, zz]
            case s: StructType => s.map{ e =>
                getTypeFunction(e.dataType, inSpark)
            }.head
            case _ => throw new Exception("translateExpression2 unknown datatype")
        }
    }


    private def mkExpression2(set: Set[Int], dataType: DataType, l: Literal, inSpark: Boolean): String = {
        //notice: compare return ==0, 0<, >0
        val (constructFun, compareFun, dType, _) = getTypeFunction(dataType, inSpark)
        set.map {
            case 0 =>  s"""((($dType)x).${compareFun}($constructFun("${l.toString()}")) == 0)"""
            case 1 =>  s"""((($dType)x).${compareFun}($constructFun("${l.toString()}")) > 0)"""
            case -1 => s"""((($dType)x).${compareFun}($constructFun("${l.toString()}")) < 0)"""
        }.mkString("||")
    }


    private def translateExpression2(expression: Expression, dataType: DataType, inSpark: Boolean): String  = {
        expression match {
            case EqualTo(e: Expression, l: Literal)             => mkExpression2(Set(0), dataType, l, inSpark)
            case EqualTo(l: Literal, e: Expression)             => mkExpression2(Set(0), dataType, l, inSpark)
            case GreaterThan(e: Expression, l: Literal)         => mkExpression2(Set(1), dataType,l, inSpark)
            case GreaterThan(l: Literal, e: Expression)         => mkExpression2(Set(-1), dataType,l, inSpark)
            case GreaterThanOrEqual(e: Expression, l: Literal)  => mkExpression2(Set(0, 1), dataType,l, inSpark)
            case GreaterThanOrEqual(l: Literal, e: Expression)  => mkExpression2(Set(0, -1), dataType,l, inSpark)
            case LessThan(e: Expression, l: Literal)            => mkExpression2(Set(-1), dataType,l, inSpark)
            case LessThan(l: Literal, e: Expression)            => mkExpression2(Set(1), dataType,l, inSpark)
            case LessThanOrEqual(e: Expression, l: Literal)     => mkExpression2(Set(-1, 0), dataType,l, inSpark)
            case LessThanOrEqual(l: Literal, e: Expression)     => mkExpression2(Set(0, 1), dataType,l, inSpark)
            case EqualNullSafe(e: Expression, l: Literal)       => mkExpression2(Set(-1, 0, 1), dataType,l, inSpark)
            case And(left, right)                               => s"""(${translateExpression2(left, dataType, inSpark)}) &&  (${translateExpression2(right, dataType, inSpark)})"""
            case Or(left, right)                                => s"""(${translateExpression2(left, dataType, inSpark)}) ||  (${translateExpression2(right, dataType, inSpark)})"""
            case IsNull(e: Expression)                          => ""  //TODO
            case IsNotNull(e: Expression)                       => ""
        }
    }

    def genArrayMapCode(expression: Expression, dataType: DataType, inSpark: Boolean = true): String  = {
        val exp = expression.toString()
        val (constructFun, _, dType, valueFun) = getTypeFunction(dataType, inSpark)
        s"""$constructFun(${exp.replace("x", s"(($dType)x).$valueFun()")})"""

    }

    def main(args: Array[String]): Unit = {
        val expression: Expression = CatalystSqlParser.parseExpression("x>50 and x<70")
        println(expression)
    }
}
