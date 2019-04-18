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

package moonbox.common.config

import java.util.concurrent.ConcurrentHashMap

abstract class ConfigEntry[T](
	val key: String,
	val valueConverter: String => T,
	val stringConverter: T => String) {

	import ConfigEntry._

	registerEntry(this)

	def defaultValueString: String

	def defaultValue: Option[T] = None

	def readFrom(reader: ConfigReader): T
}

class ConfigEntryWithDefault[T](
	key: String,
	_defaultValue: T,
	valueConverter: String => T,
	stringConverter: T => String) extends ConfigEntry(key, valueConverter, stringConverter) {
	override def defaultValue: Option[T] = Some(_defaultValue)

	override def defaultValueString: String = stringConverter(_defaultValue)

	override def readFrom(reader: ConfigReader): T = {
		reader.get(key).map(valueConverter).getOrElse(_defaultValue)
	}
}

class ConfigEntryWithDefaultString[T](
	key: String,
	_defaultValue: String,
	valueConverter: String => T,
	stringConverter: T => String) extends ConfigEntry(key, valueConverter, stringConverter) {

	override def defaultValue: Option[T] = Some(valueConverter(_defaultValue))

	override def defaultValueString: String = _defaultValue

	override def readFrom(reader: ConfigReader): T = {
		val value: String = reader.get(key).getOrElse(_defaultValue)
		valueConverter(value)
	}
}

class OptionalConfigEntry[T](
	key: String,
	val _valueConverter: String => T,
	val _stringConverter: T => String) extends ConfigEntry[Option[T]](key, s => Some(_valueConverter(s)), v => v.map(_stringConverter).orNull){

	override def defaultValueString: String = "<undefined>"

	override def readFrom(reader: ConfigReader): Option[T] = {
		reader.get(key).map(_valueConverter)
	}
}

class FallbackConfigEntry[T] (
	key: String,
	val fallback: ConfigEntry[T])
	extends ConfigEntry[T](key, fallback.valueConverter, fallback.stringConverter) {

	override def defaultValueString: String = s"<value of ${fallback.key}>"

	override def readFrom(reader: ConfigReader): T = {
		reader.get(key).map(valueConverter).getOrElse(fallback.readFrom(reader))
	}

}



object ConfigEntry {
	private val knownConfigs = new ConcurrentHashMap[String, ConfigEntry[_]]()

	def registerEntry(entry: ConfigEntry[_]): Unit = {
		val existing: ConfigEntry[_] = knownConfigs.putIfAbsent(entry.key, entry)
		require(existing == null, s"Config entry ${entry.key} already registered!")
	}

	def findEntry(key: String): ConfigEntry[_] = knownConfigs.get(key)
}
