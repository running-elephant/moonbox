package moonbox.core.udf

import java.lang.reflect.Method
import java.net.{URI, URL, URLClassLoader}
import javax.tools._

import org.apache.spark.sql.Row
import org.apache.spark.sql.expressions.{MutableAggregationBuffer, UserDefinedAggregateFunction}
import org.apache.spark.sql.types.{DataType, StructType}

import scala.reflect.ClassTag

object UdfHelper {

  /** get method input param number by kinds of params */
  def getParamNumber(udfName: String, src: String="", path: String="", className: String="", tpe: String): Int = {
    val clazz = reflect(src, path, className, tpe)
    getMethodByClass(udfName, clazz).getParameterCount
  }

  /** do reflect to get class **/
  def reflect(src: String, path: String, className: String, tpe: String): Class[_] = {
    val clazz: Class[_] = tpe.toLowerCase match {
      case "scala" =>
        val clazz = scalaCodeParser(src)
        clazz
      case "java" =>
        val clazz = javaCodeParser(src, className)
        clazz
      case "jar" =>
        val clazz = jarParser(path, className)
        clazz
    }
    clazz
  }

  /** create new object **/
  def getObjectByClass(clazz: Class[_]) = {
    val constructor = clazz.getDeclaredConstructors.head
    constructor.setAccessible(true)
    val o: Any = constructor.newInstance()
    o
  }

  /** get method by class and udf name **/
  def getMethodByClass(udfName: String, clazz: Class[_]) = {
    val methods = clazz.getDeclaredMethods
    var callMethod: Method = null
    for (i <- methods.indices) {
      val m: Method = methods(i)
      if (m.getName.equals(udfName)) {
        callMethod = m
      }
    }
    if(callMethod == null) {
      throw new Exception(s"getMethodByClass: not found $udfName in ${clazz.getCanonicalName}")
    }
    callMethod
  }

  /** get class by scala code */
  def scalaCodeParser(src: String): Class[_] = {
    import scala.reflect.runtime.universe
    import scala.tools.reflect.ToolBox
    val tb: ToolBox[universe.type] = universe.runtimeMirror(scala.reflect.runtime.universe.getClass.getClassLoader).mkToolBox()
    val tree: tb.u.Tree = tb.parse {src}
    val clazz = tb.compile(tree).apply().asInstanceOf[Class[_]]
    clazz
  }

  /** get class by java code */
  def javaCodeParser(src: String, className: String): Class[_] = {
    import scala.collection.JavaConversions._
    val compiler = ToolProvider.getSystemJavaCompiler
    val diagnostics = new DiagnosticCollector[JavaFileObject]
    val byteObject = new JavaByteObject(className)

    val standardFileManager = compiler.getStandardFileManager(diagnostics, null, null)

    val fileManager = JavaReflect.createFileManager(standardFileManager, byteObject)

    val task = compiler.getTask(null, fileManager, diagnostics, null, null, JavaReflect.getCompilationUnits(className, src))

    if (!task.call) {
      diagnostics.getDiagnostics.foreach(println(_))
    }
    fileManager.close()

    val inMemoryClassLoader = JavaReflect.createClassLoader(byteObject) //loading and using our compiled class
    val clazz = inMemoryClassLoader.loadClass(className)
    clazz
  }

  /** get class by jar package */
  def jarParser(path: String, className: String): Class[_] = {
    val url = new URI(path).toURL
    val classLoader = getClass.getClassLoader.asInstanceOf[URLClassLoader]
    val loaderMethod = classOf[URLClassLoader].getDeclaredMethod("addURL", classOf[URL])
    loaderMethod.setAccessible(true)
    loaderMethod.invoke(classLoader, url)
    val clazz = Class.forName(className)
    clazz
  }

  //if print Caused by: java.lang.ClassCastException: cannot assign instance of scala.collection.immutable.List$SerializationProxy to field org.apache.spark.sql.execution.aggregate.HashAggregateExec.aggregateExpressions of type scala.collection.Seq in instance of org.apache.spark.sql.execution.aggregate.HashAggregateExec
  //it means serialize error, Method Class and so on can not be serialized.
  /***create agg function - UDAF - USER API
    a) scala source(scala) need: 1. source code
    b) java source(java)   need: 1. source  2. class name
    c) extended jar(jar)   need: 1. jar path  2.class name
  */
  def generateAggFunction(src: String="", path: String="", className: String="", tpe: String): UserDefinedAggregateFunction = {

      new UserDefinedAggregateFunction with Serializable{
        @transient val clazz1: Class[_] = reflect(src, path, className, tpe) //use before serialize, Not serialize class and object, Only serialize schema and datatype
        @transient val obj1: Any = getObjectByClass(clazz1)

        lazy val clazz2: Class[_] = reflect(src, path, className, tpe) //use after serialize, serialize src string when need
        lazy val obj2: Any = getObjectByClass(clazz2)

        def getFieldByMethod[T: ClassTag](clazz: Class[_], obj: Any, methodName: String): T = {
          val method = getMethodByClass(methodName, clazz)
          method.invoke(obj).asInstanceOf[T]
        }

        val inputSchema_ = getFieldByMethod[StructType](clazz1, obj1, "inputSchema")
        val dataType_ = getFieldByMethod[DataType](clazz1, obj1,"dataType")
        val bufferSchema_ = getFieldByMethod[StructType](clazz1, obj1, "bufferSchema")
        val deterministic_ = getFieldByMethod[Boolean](clazz1, obj1, "deterministic")

        override def inputSchema(): StructType = {   //use before serialize
          inputSchema_
        }
        override def dataType(): DataType = {        //use before serialize
          dataType_
        }
        override def bufferSchema(): StructType = {  //use before serialize
          bufferSchema_
        }
        override def deterministic(): Boolean = {    //use before serialize
          deterministic_
        }
        lazy val method_update = getMethodByClass("update", clazz2)
        lazy val method_merge = getMethodByClass("merge", clazz2)
        lazy val method_initialize = getMethodByClass("initialize", clazz2)
        lazy val method_evaluate = getMethodByClass("evaluate", clazz2)
        override def update(buffer: MutableAggregationBuffer, input: Row): Unit = {     //use after serialize
          method_update.invoke(obj2, buffer, input)
        }

        override def merge(buffer1: MutableAggregationBuffer, buffer2: Row): Unit = {   //use after serialize
          method_merge.invoke(obj2, buffer1, buffer2)
        }

        override def initialize(buffer: MutableAggregationBuffer): Unit = {   //use after serialize
          method_initialize.invoke(obj2, buffer)
        }

        override def evaluate(buffer: Row): Any = {   //use after serialize
          method_evaluate.invoke(obj2, buffer)
        }
      }
  }

/**create user define function - UDF - USER API
  * support 0 ~ 22 parameters
  * a) scala source(scala) need: 1. source code
    b) java source(java)   need: 1. source  2. class name
    c) extended jar(jar)   need: 1. jar path  2.class name
 */
  def generateFunction(parameterCount: Int, udfName: String="", src: String="", path: String="", className: String="", tpe: String): AnyRef = {
    //Method and Class can not be serialized, use lazy val here
    lazy val clazz = reflect(src, path, className, tpe)
    lazy val obj = getObjectByClass(clazz)
    lazy val method = getMethodByClass(udfName, clazz)
    parameterCount match {
      case 0 => new Function0[Any] with Serializable {
        def apply(): Any = { method.invoke(obj) }
      }
      case 1 => new Function1[Object, Any] with Serializable {
        def apply(o: Object): Any = { method.invoke(obj, o) }
      }
      case 2 => new Function2[Object, Object, Any] with Serializable {
        def apply(o1: Object, o2: Object): Any = { method.invoke(obj, o1, o2)  }
      }
      case 3 => new Function3[Object, Object, Object, Any] with Serializable {
        def apply(o1: Object, o2: Object, o3: Object): Any = { method.invoke(obj, o1, o2, o3)  }
      }
      case 4 => new Function4[Object, Object, Object, Object, Any] with Serializable {
        def apply(o1: Object, o2: Object, o3: Object, o4: Object): Any = { method.invoke(obj, o1, o2, o3, o4)  }
      }
      case 5 => new Function5[Object, Object, Object, Object, Object, Any] with Serializable {
        def apply(o1: Object, o2: Object, o3: Object, o4: Object, o5: Object): Any = {
          method.invoke(obj, o1, o2, o3, o4, o5)
        }
      }
      case 6 => new Function6[Object, Object, Object, Object, Object, Object, Any] with Serializable {
        def apply(o1: Object, o2: Object, o3: Object, o4: Object, o5: Object, o6: Object): Any = {
          method.invoke(obj, o1, o2, o3, o4, o5, o6)
        }
      }
      case 7 => new Function7[Object, Object, Object, Object, Object, Object, Object, Any] with Serializable {
        def apply(o1: Object, o2: Object, o3: Object, o4: Object, o5: Object, o6: Object, o7: Object): Any = {
          method.invoke(obj, o1, o2, o3, o4, o5, o6, o7)
        }
      }
      case 8 => new Function8[Object, Object, Object, Object, Object, Object, Object, Object, Any] with Serializable {
        def apply(o1: Object, o2: Object, o3: Object, o4: Object, o5: Object, o6: Object, o7: Object, o8: Object): Any = {
          method.invoke(obj, o1, o2, o3, o4, o5, o6, o7, o8)
        }
      }
      case 9 => new Function9[Object, Object, Object, Object, Object, Object, Object, Object, Object, Any] with Serializable {
        def apply(o1: Object, o2: Object, o3: Object, o4: Object, o5: Object, o6: Object, o7: Object, o8: Object, o9: Object): Any = {
          method.invoke(obj, o1, o2, o3, o4, o5, o6, o7, o8, o9)
        }
      }
      case 10 => new Function10[Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Any] with Serializable {
        def apply(o1: Object, o2: Object, o3: Object, o4: Object, o5: Object, o6: Object, o7: Object, o8: Object, o9: Object, o10: Object): Any = {
          method.invoke(obj, o1, o2, o3, o4, o5, o6, o7, o8, o9, o10)  }
      }
      case 11 => new Function11[Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Any] with Serializable {
        def apply(o1: Object, o2: Object, o3: Object, o4: Object, o5: Object, o6: Object, o7: Object, o8: Object, o9: Object, o10: Object, o11: Object): Any = {
          method.invoke(obj, o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11)
        }
      }
      case 12 => new Function12[Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Any] with Serializable {
        def apply(o1: Object, o2: Object, o3: Object, o4: Object, o5: Object, o6: Object, o7: Object, o8: Object, o9: Object, o10: Object, o11: Object, o12: Object): Any = {
          method.invoke(obj, o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12)
        }
      }
      case 13 => new Function13[Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Any] with Serializable {
        def apply(o1: Object, o2: Object, o3: Object, o4: Object, o5: Object, o6: Object, o7: Object, o8: Object, o9: Object, o10: Object, o11: Object, o12: Object, o13: Object): Any = {
          method.invoke(obj, o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13)
        }
      }
      case 14 => new Function14[Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Any] with Serializable {
        def apply(o1: Object, o2: Object, o3: Object, o4: Object, o5: Object, o6: Object, o7: Object, o8: Object, o9: Object, o10: Object, o11: Object, o12: Object, o13: Object, o14: Object): Any = {
          method.invoke(obj, o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14)
        }
      }
      case 15 => new Function15[Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Any] with Serializable {
        def apply(o1: Object, o2: Object, o3: Object, o4: Object, o5: Object, o6: Object, o7: Object, o8: Object, o9: Object, o10: Object, o11: Object, o12: Object, o13: Object, o14: Object, o15: Object): Any = {
          method.invoke(obj, o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15)
        }
      }
      case 16 => new Function16[Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Any] with Serializable {
        def apply(o1: Object, o2: Object, o3: Object, o4: Object, o5: Object, o6: Object, o7: Object, o8: Object, o9: Object, o10: Object, o11: Object, o12: Object, o13: Object, o14: Object, o15: Object, o16: Object): Any = {
          method.invoke(obj, o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16)
        }
      }
      case 17 => new Function17[Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Any] with Serializable {
        def apply(o1: Object, o2: Object, o3: Object, o4: Object, o5: Object, o6: Object, o7: Object, o8: Object, o9: Object, o10: Object, o11: Object, o12: Object, o13: Object, o14: Object, o15: Object, o16: Object, o17: Object): Any = {
          method.invoke(obj, o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17)
        }
      }
      case 18 => new Function18[Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object,  Any] with Serializable {
        def apply(o1: Object, o2: Object, o3: Object, o4: Object, o5: Object, o6: Object, o7: Object, o8: Object, o9: Object, o10: Object, o11: Object, o12: Object, o13: Object, o14: Object, o15: Object, o16: Object, o17: Object, o18: Object): Any = {
          method.invoke(obj, o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18)
        }
      }
      case 19 => new Function19[Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Any] with Serializable {
        def apply(o1: Object, o2: Object, o3: Object, o4: Object, o5: Object, o6: Object, o7: Object, o8: Object, o9: Object, o10: Object, o11: Object, o12: Object, o13: Object, o14: Object, o15: Object, o16: Object, o17: Object, o18: Object, o19: Object): Any = {
          method.invoke(obj, o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19)
        }
      }
      case 20 => new Function20[Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Any] with Serializable {
        def apply(o1: Object, o2: Object, o3: Object, o4: Object, o5: Object, o6: Object, o7: Object, o8: Object, o9: Object, o10: Object, o11: Object, o12: Object, o13: Object, o14: Object, o15: Object, o16: Object, o17: Object, o18: Object, o19: Object, o20: Object): Any = {
          method.invoke(obj, o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19, o20)
        }
      }
      case 21 => new Function21[Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Any] with Serializable {
        def apply(o1: Object, o2: Object, o3: Object, o4: Object, o5: Object, o6: Object, o7: Object, o8: Object, o9: Object, o10: Object, o11: Object, o12: Object, o13: Object, o14: Object, o15: Object, o16: Object, o17: Object, o18: Object, o19: Object, o20: Object, o21: Object): Any = {
          method.invoke(obj, o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19, o20, o21)
        }
      }
      case 22 => new Function22[Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Any] with Serializable {
        def apply(o1: Object, o2: Object, o3: Object, o4: Object, o5: Object, o6: Object, o7: Object, o8: Object, o9: Object, o10: Object, o11: Object, o12: Object, o13: Object, o14: Object, o15: Object, o16: Object, o17: Object, o18: Object, o19: Object, o20: Object, o21: Object, o22: Object): Any = {
          method.invoke(obj, o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19, o20, o21, o22)
        }
      }
    }
  }
}
