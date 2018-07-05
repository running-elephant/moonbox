package moonbox.core.udf

import org.apache.spark.sql.expressions.UserDefinedAggregateFunction

object NonSourceUDAF {
	def apply(className: String): UserDefinedAggregateFunction = {
		val clazz = Class.forName(className)
		UdfUtils.newInstance(clazz).asInstanceOf[UserDefinedAggregateFunction]
	}
}
