import java.io.File
import java.net.URLClassLoader

class Loader(resource: String) {
	val classLoader = createClassLoader()

	def loadClass(clazz: String): Class[_] = {
		classLoader.loadClass(clazz)
	}

	private def createClassLoader() = {
		new URLClassLoader(Array(new File(resource).toURI.toURL),
			ClassLoader.getSystemClassLoader)
	}

}
