/*-
 * <<
 * Moonbox
 * ==
 * Copyright (C) 2016 - 2019 EDP
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

package moonbox.catalog

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
