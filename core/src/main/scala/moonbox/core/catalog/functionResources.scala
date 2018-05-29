package moonbox.core.catalog

import java.util.Locale

import moonbox.common.exception.UnsupportedException

abstract class FunctionResourceType(val `type`: String) {
	override def toString: String = `type`
}

object JarResource extends FunctionResourceType("jar")
object FileResource extends FunctionResourceType("file")
object ArchiveResource extends FunctionResourceType("archive")
object JavaResource extends FunctionResourceType("java")
object ScalaResource extends FunctionResourceType("scala")

object FunctionResourceType {
	def fromString(resourceType: String): FunctionResourceType = {
		resourceType.toLowerCase(Locale.ROOT) match {
			case "jar" => JarResource
			case "file" => FileResource
			case "archive" => ArchiveResource
			case "java" => JavaResource
			case "scala" => ScalaResource
			case other =>
				throw new UnsupportedException(s"Resource type '$other' is not supported.")
		}
	}
}

case class FunctionResource(resourceType: FunctionResourceType, uri: String)

object FunctionResource {
	def apply(resourceType: String, resource: String): FunctionResource = {
		apply(FunctionResourceType.fromString(resourceType), resource)
	}
}
