package org.apache.spark.sql.udf

import java.{lang, util}

import org.apache.spark.sql.types.DataType
import org.apache.spark.util.{ParentClassLoader, Utils}
import org.codehaus.commons.compiler.{CompilerFactoryFactory, IClassBodyEvaluator}

object JaninoCodeGen {

    def getGenClass(classBody: String): Class[_] = {
        // Compile the class body.
        val cbe: IClassBodyEvaluator = CompilerFactoryFactory.getDefaultCompilerFactory.newClassBodyEvaluator

        val parentClassLoader = new ParentClassLoader(Utils.getContextOrSparkClassLoader)
        cbe.setParentClassLoader(parentClassLoader)
        cbe.setDefaultImports(Array[String](
            "org.apache.spark.unsafe.types.UTF8String",
            "java.util.ArrayList",
            "java.lang.String"))

        cbe.cook(classBody)
        val c: Class[_] = cbe.getClazz
        c
    }

    def exists(array: Array[AnyRef], dataType: DataType, lambda: String): Boolean = {
        val filterLambda = lambda.replace("x", "a[i]")
        val classBody: String =
            s"""public static boolean exists(Object [] a) {
               |     boolean contains = false;
               |     for(int i=0; i<a.length; i++) {
               |         if(${filterLambda}) {  //here
               |            contains = true;
               |            break;
               |         }
               |     }
               |     return contains;
               |}
            """.stripMargin
        val c = getGenClass(classBody)
        val method = c.getMethod("exists", classOf[Array[AnyRef]])
        method.invoke(null, array).asInstanceOf[Boolean]
    }


    def filter(array: Array[AnyRef], dataType: DataType, lambda: String): Array[AnyRef] = {
        val filterLambda = lambda.replace("x", "a[i]")
        val classBody: String =
            s"""public static Object[] filter(Object [] a) {
               |     java.util.ArrayList<Object> list = new ArrayList<Object>();
               |     for(int i=0; i<a.length; i++) {
               |         if(${filterLambda}) {  //here
               |            list.add(a[i]);
               |         }
               |     }
               |     Object[] objects = list.toArray();
               |     return objects;
               |}
            """.stripMargin
        val c = getGenClass(classBody)
        val method = c.getMethod("filter", classOf[Array[AnyRef]])
        method.invoke(null, array).asInstanceOf[Array[AnyRef]]
    }


    def map(array: Array[AnyRef], dataType: DataType, lambda: String): Array[AnyRef] = {
        val mapLambda = lambda.replace("x", "a[i]")
        val classBody: String =
            s"""public static Object[] map(Object [] a)  {
              |     for(int i=0; i< a.length; i++) {
              |         System.out.println(a[i]);
              |         a[i] = ${mapLambda};    //here
              |     }
              |     return a;
              |}
            """.stripMargin
        val c = getGenClass(classBody)
        val method = c.getMethod("map", classOf[Array[AnyRef]])
        val ret = method.invoke(null, array)
        ret.asInstanceOf[Array[AnyRef]]
    }

    def test1(array: Array[Any]): Array[AnyRef] = {
        val classBody: String =
            s"""public static Object[] map(Object [] a)  {
               |     for(int i=0; i< a.length; i++) {
               |         System.out.println(a[i]);
               |     }
               |     return a;
               |}
            """.stripMargin
        val c = getGenClass(classBody)
        val method = c.getMethods.filterNot(_.getName == "map2").toSeq.head
        val ret = method.invoke(null, array)
        ret.asInstanceOf[Array[AnyRef]]
    }


    def main(args: Array[String]): Unit = {
        val long1: lang.Long = new java.lang.Long(18)
        val long2: lang.Long = new java.lang.Long(20)

        val array = new util.ArrayList[AnyRef]()
        array.add(long1)
        array.add(long2)
        val array1 = array.toArray.asInstanceOf[Array[Any]]

        test1(array1)

    }

}
