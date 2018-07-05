package moonbox.core.catalog

import java.util.Locale

import moonbox.common.exception.UnsupportedException

/*abstract class FunctionResourceType(val `type`: String) {
	override def toString: String = `type`
}*/
trait FunctionResourceType {
	val `type`: String
}

trait SourceResource
trait NonSourceResource

/*object JarResource extends FunctionResourceType("jar") with NonSourceResource
object FileResource extends FunctionResourceType("file") with NonSourceResource
object ArchiveResource extends FunctionResourceType("archive") with NonSourceResource
object JavaResource extends FunctionResourceType("java") with SourceResource
object ScalaResource extends FunctionResourceType("scala") with SourceResource*/
case object JarResource extends FunctionResourceType with NonSourceResource {
	override val `type`: String = "jar"
}
case object FileResource extends FunctionResourceType with NonSourceResource {
	override val `type`: String = "file"
}
case object ArchiveResource extends FunctionResourceType with NonSourceResource {
	override val `type`: String = "archive"
}
case object JavaResource extends FunctionResourceType with SourceResource {
	override val `type`: String = "java"
}
case object ScalaResource extends FunctionResourceType with SourceResource {
	override val `type`: String = "scala"
}

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

object FunctionType {
	val UDF = "UDF"
	val UDAF = "UDAF"
}
