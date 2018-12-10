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

package moonbox.core.datasys

import java.util.ServiceLoader
import java.util.concurrent.ConcurrentHashMap

import moonbox.common.MbLogging
import org.apache.spark.sql.datasys.SparkDataSystemProvider

import scala.collection.JavaConverters._

abstract class DataSystem(props: Map[String, String]) {

	protected def contains(params: String*): Boolean = params.forall(props.contains)

	def tableNames(): Seq[String]

    def tableProperties(tableName: String): Map[String, String]

	def tableName(): String

	def test(): Boolean = true
}

object DataSystem extends MbLogging {

	private val registeredDataSystemProvider = {
		val providers = new ConcurrentHashMap[String, DataSystemProvider]()
		providers.put("spark", new SparkDataSystemProvider)
		providers
	}

	private val registeredDataSource = new ConcurrentHashMap[String, String]()

	load()

	private def load() = {
		val serviceLoader = ServiceLoader.load(classOf[DataSystemRegister])
		for ((provider, dataSystems) <- serviceLoader.asScala.groupBy(_.shortName())) {
			val instance = dataSystems.toList match {
				case Nil => throw new ClassNotFoundException(s"Failed to find data system: ${provider.toLowerCase()}")
				case head :: Nil if head.isInstanceOf[DataSystemProvider] =>
					head.asInstanceOf[DataSystemProvider]
				case datasys =>
					throw new Exception
			}
			registeredDataSystemProvider.put(provider, instance.asInstanceOf[DataSystemProvider])
		}
	}

	private def lookupDataSystemProvider(provider: String): DataSystemProvider = {
		registeredDataSystemProvider.asScala.getOrElse(provider, {
			val serviceLoader = ServiceLoader.load(classOf[DataSystemRegister])
			serviceLoader.asScala.filter(_.shortName().equalsIgnoreCase(provider)).toList match {
				case Nil => throw new ClassNotFoundException(s"Failed to find data system: ${provider.toLowerCase()}")
				case head :: Nil if head.isInstanceOf[DataSystemProvider] =>
					head.asInstanceOf[DataSystemProvider]
				case datasys =>
					val sysName = datasys.map(_.getClass.getName)
					val internal = datasys.filter(_.getClass.getName.startsWith("moonbox.core.datasys"))
					if (internal.size == 1) {
						logWarning(s"Multiple datasys provider found for $provider, use ${internal.head.getClass.getName} default.")
						internal.head match {
							case provider: DataSystemProvider =>
								provider
							case _ => throw new Exception(s"${internal.head.getClass.getName} is not an instance of DataSystemProvider.")
						}

					} else {
						throw new Exception(s"Multiple datasys provider found for $provider: ${sysName.mkString(", ")}")
					}
			}
		})
	}

	def registerDataSource(shortName: String, datasource: String): Unit = synchronized {
		if (shortName != null) {
			registeredDataSource.put(shortName, datasource)
		}
		logInfo(s"registerDataSource: ($shortName, $datasource)")
	}

	def lookupDataSource(provider: String): String = {
		val datasoruce = registeredDataSource.get(provider)
		if (datasoruce == null) {
			provider
		} else {
			datasoruce
		}
	}

	def lookupDataSystem(props: Map[String, String]): DataSystem = {
		val provider = props.getOrElse("type", throw new NoSuchElementException("type must be config in options."))
		lookupDataSystemProvider(provider).createDataSystem(props)
	}
}
