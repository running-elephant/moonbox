package moonbox.core.udf

import org.apache.spark.sql.catalyst.analysis.FunctionRegistry.FunctionBuilder
import org.apache.spark.sql.catalyst.expressions.{Expression, ScalaUDF}
import org.apache.spark.sql.execution.aggregate.ScalaUDAF
import org.apache.spark.sql.expressions.UserDefinedAggregateFunction

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
		val clazz = Class.forName(className)
		val superClassName = clazz.getSuperclass.getTypeName
		if (superClassName.equals(classOf[UserDefinedAggregateFunction].getName)) { // udaf
			(e: Seq[Expression]) => ScalaUDAF(e, NonSourceUDAF(className))
		} else { // udf
		val (func, returnType) = NonSourceUDF(className, methodName)
			(e: Seq[Expression]) => ScalaUDF(func, returnType, e, udfName = Some(udfName))
		}
	}
}
