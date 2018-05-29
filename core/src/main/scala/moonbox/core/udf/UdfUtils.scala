package moonbox.core.udf

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
}
