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

package moonbox.catalyst.core.udf;

import org.apache.spark.sql.types.DataType;
import org.apache.spark.util.ParentClassLoader;
import org.apache.spark.util.Utils;
import org.codehaus.commons.compiler.CompilerFactoryFactory;
import org.codehaus.commons.compiler.IClassBodyEvaluator;

import java.lang.reflect.Method;

public class ArrayCodeGen {
    public static Object[] map(Object[] objects, DataType dataType, String lambda) {

        String mapLambda = lambda.replace("x", "a[i]");

        String template =
                "public static Object[] map2(Object [] a)  { \n" +
                        "     for(int i=0; i< a.length; i++) { \n" +
                        "        System.out.println(a[i]); \n" +
                        "         a[i] = ${lambda};    //here \n" +
                        "     }\n" +
                        "     return a;\n" +
                        "}";
        String classBody = template.replace("${lambda}", mapLambda);

        try {
            IClassBodyEvaluator cbe = CompilerFactoryFactory.getDefaultCompilerFactory().newClassBodyEvaluator();

            ParentClassLoader parentClassLoader = new ParentClassLoader(Utils.getContextOrSparkClassLoader());
            cbe.setParentClassLoader(parentClassLoader);
            cbe.setDefaultImports(new String[]{
                    "org.apache.spark.unsafe.types.UTF8String"});

            cbe.cook(classBody);
            Class<?> c = cbe.getClazz();


            Method[] m = c.getMethods();
            int j = 0;
            for (; j < m.length; j++) {
                if (m[j].getName().equals("map2")) {
                    break;
                }
            }
            Method method = m[j];
            Object ret = method.invoke(null, (Object) objects);
            Object[] ret1 = (Object[] )ret;
            return ret1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Object[1];
    }


    public static Object[] filter(Object[] objects, DataType dataType, String lambda) {

        String mapLambda = lambda.replace("x", "a[i]");
        System.out.println(mapLambda);

        String template =
                "public static Object[] filter2(Object [] a)  { \n" +
                        "     java.util.ArrayList<Object> list = new ArrayList<Object>(); \n" +
                        "     for(int i=0; i< a.length; i++) { \n" +
                        "         if(${lambda}) {  \n"  +
                        "            list.add(a[i]);\n"         +
                        "         } \n"                         +
                        "     }\n"                              +
                        "     Object[] objects = list.toArray();" +
                        "     return objects;\n" +
                        "}";
        String classBody = template.replace("${lambda}", mapLambda);

        try {
            IClassBodyEvaluator cbe = CompilerFactoryFactory.getDefaultCompilerFactory().newClassBodyEvaluator();

            ParentClassLoader parentClassLoader = new ParentClassLoader(Utils.getContextOrSparkClassLoader());
            cbe.setParentClassLoader(parentClassLoader);
            cbe.setDefaultImports(new String[]{
                    "org.apache.spark.unsafe.types.UTF8String",
                    "java.util.ArrayList"});

            cbe.cook(classBody);
            Class<?> c = cbe.getClazz();


            Method[] m = c.getMethods();
            int j = 0;
            for (; j < m.length; j++) {
                if (m[j].getName().equals("filter2")) {
                    break;
                }
            }
            Method method = m[j];
            Object ret = method.invoke(null, (Object) objects);
            Object[] ret1 = (Object[] )ret;
            return ret1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Object[1];
    }


    public static void main(String[] args) {
        Object[] array = new Object[3];
        array[0]=  new Long(10L);
        array[1]=  new Long(20L);
        array[2]=  new Long(30L);


        Object[] obj = map(array, null,"x");
        System.out.println((Object)obj);

    }
}



