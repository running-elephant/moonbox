package moonbox.core.udf

import org.apache.spark.sql.expressions.UserDefinedAggregateFunction
import org.apache.spark.sql.util.UtilsWrapper

object NonSourceUDAF {
	def apply(className: String): UserDefinedAggregateFunction = {
		val clazz = Class.forName(className, true,  UtilsWrapper.getContextOrSparkClassLoader)
		UdfUtils.newInstance(clazz).asInstanceOf[UserDefinedAggregateFunction]
	}
}
