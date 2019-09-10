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

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.spark.sql.execution.datasources.ums

import java.io.InputStream

import com.fasterxml.jackson.core.{JsonFactory, JsonParseException, JsonParser}
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.common.io.ByteStreams
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.FileStatus
import org.apache.hadoop.io.Text
import org.apache.hadoop.mapreduce.Job
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat
import org.apache.spark.TaskContext
import org.apache.spark.input.{PortableDataStream, StreamInputFormat}
import org.apache.spark.rdd.{BinaryFileRDD, RDD}
import org.apache.spark.sql.catalyst.InternalRow
import org.apache.spark.sql.catalyst.json.{CreateJacksonParser, JSONOptions, JacksonParser}
import org.apache.spark.sql.execution.datasources._
import org.apache.spark.sql.execution.datasources.json.{JsonInferSchema, JsonUtils}
import org.apache.spark.sql.execution.datasources.text.TextFileFormat
import org.apache.spark.sql.types._
import org.apache.spark.sql.{AnalysisException, Dataset, Encoders, SparkSession}
import org.apache.spark.unsafe.types.UTF8String
import org.apache.spark.util.Utils

import scala.collection.JavaConversions._

/**
  * Common functions for parsing JSON files
  */
abstract class JsonDataSource extends Serializable {
	def isSplitable: Boolean

	/**
	  * Parse a [[PartitionedFile]] into 0 or more [[InternalRow]] instances
	  */
	def readFile(conf: Configuration,
		file: PartitionedFile,
		parser: JacksonParser,
		requiredSchema: StructType
	): Iterator[InternalRow]

	final def inferSchema(sparkSession: SparkSession,
		inputPaths: Seq[FileStatus],
		parsedOptions: JSONOptions): Option[StructType] = {
		if (inputPaths.nonEmpty) {
			val jsonSchema = infer(sparkSession, inputPaths, parsedOptions)
			checkConstraints(jsonSchema)
			Some(jsonSchema)
		} else {
			None
		}
	}

	protected def infer(sparkSession: SparkSession,
		inputPaths: Seq[FileStatus],
		parsedOptions: JSONOptions): StructType

	/** Constraints to be imposed on schema to be stored. */
	private def checkConstraints(schema: StructType): Unit = {
		if (schema.fieldNames.length != schema.fieldNames.distinct.length) {
			val duplicateColumns = schema.fieldNames.groupBy(identity).collect {
				case (x, ys) if ys.length > 1 => "\"" + x + "\""
			}.mkString(", ")
			throw new AnalysisException(s"Duplicate column(s) : $duplicateColumns found, " +
				s"cannot save to JSON format")
		}
	}
}

object JsonDataSource {
	def apply(options: JSONOptions): JsonDataSource = {
		if (options.multiLine) {
			MultiLineJsonDataSource
		} else {
			TextInputJsonDataSource
		}
	}


}

object TextInputJsonDataSource extends JsonDataSource {
	override val isSplitable: Boolean = {
		// splittable if the underlying source is
		true
	}

	override def infer(sparkSession: SparkSession,
		inputPaths: Seq[FileStatus],
		parsedOptions: JSONOptions): StructType = {
		val json: Dataset[String] = createBaseDataset(sparkSession, inputPaths)
		inferFromDataset(json, parsedOptions)
	}

	// modified
	def inferFromDataset(json: Dataset[String], parsedOptions: JSONOptions): StructType = {

		val sampled: Dataset[String] = JsonUtils.sample(json, parsedOptions)
		val rdd: RDD[UTF8String] = sampled.queryExecution.toRdd.map(_.getUTF8String(0))
		//JsonInferSchema.infer(rdd, parsedOptions, CreateJacksonParser.utf8String)

		val array = rdd.take(1)
		if (array.length > 0) {
			UmsUtils.getPayloadSchema(array(0).toString)
		} else {
			StructType(Nil)
		}

	}

	private def createBaseDataset(sparkSession: SparkSession,
		inputPaths: Seq[FileStatus]): Dataset[String] = {

		val paths = inputPaths.map(_.getPath.toString)
		sparkSession.baseRelationToDataFrame(
			DataSource.apply(
				sparkSession,
				paths = paths,
				className = classOf[TextFileFormat].getName
			).resolveRelation(checkFilesExist = false))
			.select("value").as(Encoders.STRING)
	}

	// modified
	override def readFile(conf: Configuration,
		file: PartitionedFile,
		parser: JacksonParser,
		requiredSchema: StructType
	): Iterator[InternalRow] = {
		val linesReader: HadoopFileLinesReader = new HadoopFileLinesReader(file, conf)
		val actualSchema =  StructType(requiredSchema.filterNot(_.name == parser.options.columnNameOfCorruptRecord))
		val columnSafeParser = new ColumnSafeJacksonParser(actualSchema, parser.options)
		Option(TaskContext.get()).foreach(_.addTaskCompletionListener(_ => linesReader.close()))
		val safeParser = new FailureSafeParser[Text](
			input => columnSafeParser.parse(input, CreateJacksonParser.text, textToUTF8String),
			parser.options.parseMode,
			requiredSchema,
			parser.options.columnNameOfCorruptRecord)
		linesReader.flatMap { elem =>
			try {
				getPayloadData(elem, requiredSchema).flatMap(data => safeParser.parse(new Text(data)))
			} catch {
				// for non complete json record
				case e: JsonParseException =>
					Iterator.empty
			}
		}
	}

	private def getPayloadData(text: Text, requiredSchema: StructType): Iterator[String] = {
		val mapper = new ObjectMapper()

		val ums = mapper.readTree(text.toString)
		val payload = ums.get(UMSProtocol.PAYLOAD).iterator()
		val nameTypeIndex = ums.get(UMSProtocol.SCHEMA).get(UMSProtocol.FIELDS).toSeq.zipWithIndex.map { case (node, index) =>
			(node.get(UMSProtocol.NAME).asText, (node.get(UMSProtocol.TYPE).asText, index))}.toMap

		val existsFields = requiredSchema.fields.filter(field => nameTypeIndex.contains(field.name))

		val data = payload.map { node =>
			val record = node.get(UMSProtocol.TUPLE)
			existsFields.map { f =>
				// generate json according to ums schema, that make sure the json format is valid
				// such as one column is number type (not quoted), then change to string(must quoted).
				val value = nameTypeIndex(f.name) match {
					case (UMSProtocol.STRING | UMSProtocol.TIMESTAMP | UMSProtocol.DATETIME | UMSProtocol.DATE, index) =>
						record.get(index).toString
					case (UMSProtocol.DECIMAL | UMSProtocol.DOUBLE | UMSProtocol.FLOAT, index) =>
						val data = record.get(index).asText().trim
						if (data.startsWith(".")) {
							"0" + data
						} else if (data.startsWith("-.")) {
							"-0." + data.stripPrefix("-.")
						} else data
					case (_, index) =>
						record.get(index).asText()
				}
				"\"" + f.name + "\"" + ":" +  value
			}.mkString("{", ",", "}")
		}
		data
	}

	private def textToUTF8String(value: Text): UTF8String = {
		UTF8String.fromBytes(value.getBytes, 0, value.getLength)
	}
}

object MultiLineJsonDataSource extends JsonDataSource {
	override val isSplitable: Boolean = {
		false
	}

	override def infer(sparkSession: SparkSession,
		inputPaths: Seq[FileStatus],
		parsedOptions: JSONOptions): StructType = {
		val json: RDD[PortableDataStream] = createBaseRdd(sparkSession, inputPaths)
		val sampled: RDD[PortableDataStream] = JsonUtils.sample(json, parsedOptions)
		JsonInferSchema.infer(sampled, parsedOptions, createParser)
	}

	private def createBaseRdd(sparkSession: SparkSession,
		inputPaths: Seq[FileStatus]): RDD[PortableDataStream] = {
		val paths = inputPaths.map(_.getPath)
		val job = Job.getInstance(sparkSession.sessionState.newHadoopConf())
		val conf = job.getConfiguration
		val name = paths.mkString(",")
		FileInputFormat.setInputPaths(job, paths: _*)
		new BinaryFileRDD(
			sparkSession.sparkContext,
			classOf[StreamInputFormat],
			classOf[String],
			classOf[PortableDataStream],
			conf,
			sparkSession.sparkContext.defaultMinPartitions)
			.setName(s"JsonFile: $name")
			.values
	}

	private def createParser(jsonFactory: JsonFactory, record: PortableDataStream): JsonParser = {
		CreateJacksonParser.inputStream(
			jsonFactory,
			CodecStreams.createInputStreamWithCloseResource(record.getConfiguration, record.getPath()))
	}

	override def readFile(conf: Configuration,
		file: PartitionedFile,
		parser: JacksonParser,
		requiredSchema: StructType
	): Iterator[InternalRow] = {
		def partitionedFileString(ignored: Any): UTF8String = {
			Utils.tryWithResource {
				CodecStreams.createInputStreamWithCloseResource(conf, file.filePath)
			} { inputStream =>
				UTF8String.fromBytes(ByteStreams.toByteArray(inputStream))
			}
		}

		val safeParser = new FailureSafeParser[InputStream](
			input => parser.parse(input, CreateJacksonParser.inputStream, partitionedFileString),
			parser.options.parseMode,
			requiredSchema,
			parser.options.columnNameOfCorruptRecord)

		safeParser.parse(
			CodecStreams.createInputStreamWithCloseResource(conf, file.filePath))
	}
}

