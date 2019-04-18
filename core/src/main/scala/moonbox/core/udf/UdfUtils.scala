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

package moonbox.core.udf

import org.apache.spark.sql.catalyst.analysis.FunctionRegistry.FunctionBuilder
import org.apache.spark.sql.catalyst.expressions.{Expression, ScalaUDF}
import org.apache.spark.sql.execution.aggregate.ScalaUDAF
import org.apache.spark.sql.expressions.UserDefinedAggregateFunction
import org.apache.spark.sql.util.UtilsWrapper

object UdfUtils {

	def newInstance(clazz: Class[_]): Any = {
		val constructor = clazz.getDeclaredConstructors.head
		constructor.setAccessible(true)
		constructor.newInstance()
	}

	def getMethod(clazz: Class[_], method: String) = {
		val candidate = clazz.getDeclaredMethods.filter(_.getName == method).filterNot(_.isBridge)
		if (candidate.isEmpty) {
			throw new Exception(s"No method $method found in class ${clazz.getCanonicalName}")
		} else if (candidate.length > 1) {
			throw new Exception(s"Multiple method $method found in class ${clazz.getCanonicalName}")
		} else {
			candidate.head
		}
	}

	def javaSourceFunctionBuilder(udfName: String, src: String, className: String, methodName: Option[String]): FunctionBuilder = {
		val clazz = SourceCompiler.compileJava(src, className)
		val superClassName = clazz.getSuperclass.getTypeName
		if (superClassName.equals(classOf[UserDefinedAggregateFunction].getName)) { // java udaf
			(e: Seq[Expression]) => ScalaUDAF(e, JavaSourceUDAF(src, className))
		} else { // java udf
			val (func, returnType) = JavaSourceUDF(src, className, methodName)
			(e: Seq[Expression]) => ScalaUDF(func, returnType, e, udfName = Some(udfName))
		}
	}

	def scalaSourceFunctionBuilder(udfName: String, src: String, className: String, methodName: Option[String]): FunctionBuilder = {
		val clazz = SourceCompiler.compileScala(SourceCompiler.prepareScala(src, className))
		val superClassName = clazz.getSuperclass.getTypeName
		if (superClassName.equals(classOf[UserDefinedAggregateFunction].getName)) { // scala udaf
			(e: Seq[Expression]) => ScalaUDAF(e, ScalaSourceUDAF(src, className))
		} else { // scala udf
			val (func, returnType) = ScalaSourceUDF(src, className, methodName)
			(e: Seq[Expression]) => ScalaUDF(func, returnType, e, udfName = Some(udfName))
		}
	}

	def nonSourceFunctionBuilder(udfName: String, className: String, methodName: Option[String]): FunctionBuilder = {
		val clazz = Class.forName(className, true,  UtilsWrapper.getContextOrSparkClassLoader)
		val superClassName = clazz.getSuperclass.getTypeName
		if (superClassName.equals(classOf[UserDefinedAggregateFunction].getName)) { // udaf
			(e: Seq[Expression]) => ScalaUDAF(e, NonSourceUDAF(className))
		} else { // udf
		val (func, returnType) = NonSourceUDF(className, methodName)
			(e: Seq[Expression]) => ScalaUDF(func, returnType, e, udfName = Some(udfName))
		}
	}
}
