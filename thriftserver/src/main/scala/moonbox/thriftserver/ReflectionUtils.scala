package moonbox.thriftserver

object ReflectionUtils {
  def setSuperField(obj : Object, fieldName: String, fieldValue: Object) {
    setAncestorField(obj, 1, fieldName, fieldValue)
  }

  def setAncestorField(obj: AnyRef, level: Int, fieldName: String, fieldValue: AnyRef) {
    val ancestor = Iterator.iterate[Class[_]](obj.getClass)(_.getSuperclass).drop(level).next()
    val field = ancestor.getDeclaredField(fieldName)
    field.setAccessible(true)
    field.set(obj, fieldValue)
  }

  def getSuperField[T](obj: AnyRef, fieldName: String): T = {
    getAncestorField[T](obj, 1, fieldName)
  }

  def getAncestorField[T](clazz: Object, level: Int, fieldName: String): T = {
    val ancestor = Iterator.iterate[Class[_]](clazz.getClass)(_.getSuperclass).drop(level).next()
    val field = ancestor.getDeclaredField(fieldName)
    field.setAccessible(true)
    field.get(clazz).asInstanceOf[T]
  }

  def invokeStatic(clazz: Class[_], methodName: String, args: (Class[_], AnyRef)*): AnyRef = {
    invoke(clazz, null, methodName, args: _*)
  }

  def invoke(
      clazz: Class[_],
      obj: AnyRef,
      methodName: String,
      args: (Class[_], AnyRef)*): AnyRef = {

    val (types, values) = args.unzip
    val method = clazz.getDeclaredMethod(methodName, types: _*)
    method.setAccessible(true)
    method.invoke(obj, values.toSeq: _*)
  }
}
