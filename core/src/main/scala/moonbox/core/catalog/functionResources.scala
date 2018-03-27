package moonbox.core.catalog

import java.util.Locale

import moonbox.common.exception.UnsupportedException

abstract class FunctionResourceType(val `type`: String)

object JarResource extends FunctionResourceType("jar")
object FileResource extends FunctionResourceType("file")
object ArchiveResource extends FunctionResourceType("archive")
object ScriptResource extends FunctionResourceType("script")

object FunctionResourceType {
	def fromString(resourceType: String): FunctionResourceType = {
		resourceType.toLowerCase(Locale.ROOT) match {
			case "jar" => JarResource
			case "file" => FileResource
			case "archive" => ArchiveResource
			case "script" => ScriptResource
			case other =>
				throw new UnsupportedException(s"Resource type '$other' is not supported.")
		}
	}
}

case class FunctionResource(resourceType: FunctionResourceType, uri: String)

object FunctionResource {
	def apply(resourceType: String, uri: String): FunctionResource = {
		apply(FunctionResourceType.fromString(resourceType), uri)
	}
}
