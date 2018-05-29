package moonbox.core.udf;

import javax.tools.*;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class JavaReflect {

    public static void main(String[] args) throws Exception {
        String src ="public class Chef {" +
                        "public void cook(){" +
                            "System.out.println(\"do dinner\");" +
                        "}" +
                    "};";

        JavaReflect reflect = new JavaReflect();
        Class clazz = reflect.doReflect("Chef", src);
        Object object = clazz.newInstance();
        Method method = clazz.getMethod("cook");
        method.invoke(object);
    }

    public static Class<?> doReflect(String className, String src) {
        Class<?> clazz = null;
        try {
            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

            DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();

            final JavaByteObject byteObject = new JavaByteObject(className);

            StandardJavaFileManager standardFileManager =  compiler.getStandardFileManager(diagnostics, null, null);

            JavaFileManager fileManager = createFileManager(standardFileManager, byteObject);

            JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, null, null, getCompilationUnits(className, src));

            if (!task.call()) {
                diagnostics.getDiagnostics().forEach(System.out::println);
            }
            fileManager.close();

            //loading and using our compiled class
            final ClassLoader inMemoryClassLoader = createClassLoader(byteObject);

            clazz = inMemoryClassLoader.loadClass(className);

        } catch (Exception e){
            e.printStackTrace();
        }

        return clazz;
    }

    public static JavaFileManager createFileManager(StandardJavaFileManager fileManager,
                                                     JavaByteObject byteObject) {
        return new ForwardingJavaFileManager<StandardJavaFileManager>(fileManager) {
            @Override
            public JavaFileObject getJavaFileForOutput(Location location,
                                                       String className, JavaFileObject.Kind kind,
                                                       FileObject sibling) throws IOException {
                return byteObject;
            }
        };
    }

    public static ClassLoader createClassLoader(final JavaByteObject byteObject) {
        return new ClassLoader() {
            @Override
            public Class<?> findClass(String name) throws ClassNotFoundException {
                //no need to search class path, we already have byte code.
                byte[] bytes = byteObject.getBytes();
                return defineClass(name, bytes, 0, bytes.length);
            }
        };
    }

    public static Iterable<? extends JavaFileObject> getCompilationUnits(String className, String src) {
        JavaStringObject stringObject = new JavaStringObject(className, src);
        return Arrays.asList(stringObject);
    }
}
