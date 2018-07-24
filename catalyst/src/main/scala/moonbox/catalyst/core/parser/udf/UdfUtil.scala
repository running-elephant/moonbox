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

package org.apache.spark.sql.udf

import moonbox.catalyst.core.parser.udf.{ArrayExists, ArrayFilter, ArrayMap}
import org.apache.spark.sql.UDFRegistration
import org.apache.spark.sql.catalyst.analysis.FunctionRegistry.FunctionBuilder
import org.apache.spark.sql.catalyst.analysis.{FunctionRegistry, SimpleFunctionRegistry}
import org.apache.spark.sql.catalyst.expressions.{Expression, ExpressionDescription, ExpressionInfo, RuntimeReplaceable}

import scala.reflect.ClassTag
import scala.util.{Failure, Success, Try}


object UdfUtil {
    //for accessing private sql package -  UDFRegistration (register adaptor function)
    def buildUdfRegister(reg: FunctionRegistry): UDFRegistration = {
        new UDFRegistration(reg)
    }

    //spark or lower jdbc should call it
    def selfFunctionRegister(): FunctionRegistry = {
        val registry: SimpleFunctionRegistry = FunctionRegistry.builtin
        val expressions: Map[String, (ExpressionInfo, FunctionBuilder)] = Map(
            expression[ArrayExists]("array_exists"),
            expression[ArrayMap]("array_map"),
            expression[ArrayFilter]("array_filter")
        )
        expressions.foreach { case (name, (info, builder)) => registry.registerFunction(name, info, builder) }

        //register adaptor function
        //val udf: UDFRegistration = UdfUtil.buildUdfRegister(registry)
        //adaptorFunctionRegister(udf)

        registry
    }

    def expression[T <: Expression](name: String)
                                   (implicit tag: ClassTag[T]): (String, (ExpressionInfo, FunctionBuilder)) = {

        // For `RuntimeReplaceable`, skip the constructor with most arguments, which is the main
        // constructor and contains non-parameter `child` and should not be used as function builder.
        val constructors = if (classOf[RuntimeReplaceable].isAssignableFrom(tag.runtimeClass)) {
            val all = tag.runtimeClass.getConstructors
            val maxNumArgs = all.map(_.getParameterCount).max
            all.filterNot(_.getParameterCount == maxNumArgs)
        } else {
            tag.runtimeClass.getConstructors
        }
        // See if we can find a constructor that accepts Seq[Expression]
        val varargCtor = constructors.find(_.getParameterTypes.toSeq == Seq(classOf[Seq[_]]))
        val builder = (expressions: Seq[Expression]) => {
            if (varargCtor.isDefined) {
                // If there is an apply method that accepts Seq[Expression], use that one.
                Try(varargCtor.get.newInstance(expressions).asInstanceOf[Expression]) match {
                    case Success(e) => e
                    case Failure(e) =>
                        // the exception is an invocation exception. To get a meaningful message, we need the
                        // cause.
                        throw new Exception(e.getCause.getMessage)
                }
            } else {
                // Otherwise, find a constructor method that matches the number of arguments, and use that.
                val params = Seq.fill(expressions.size)(classOf[Expression])
                val tmp = constructors.find(_.getParameterTypes.toSeq == params)

                val f = constructors.find(_.getParameterTypes.toSeq == params).getOrElse {
                    throw new Exception(s"Invalid number of arguments for function $name")
                }
                Try(f.newInstance(expressions: _*).asInstanceOf[Expression]) match {
                    case Success(e) => e
                    case Failure(e) =>
                        // the exception is an invocation exception. To get a meaningful message, we need the
                        // cause.
                        throw new Exception(e.getCause.getMessage)
                }
            }
        }

        (name, (expressionInfo[T](name), builder))
    }

    def expressionInfo[T <: Expression : ClassTag](name: String): ExpressionInfo = {
        val clazz = scala.reflect.classTag[T].runtimeClass
        val df = clazz.getAnnotation(classOf[ExpressionDescription])
        if (df != null) {
            new ExpressionInfo(clazz.getCanonicalName, null, name, df.usage(), df.extended())
        } else {
            new ExpressionInfo(clazz.getCanonicalName, name)
        }
    }
}

