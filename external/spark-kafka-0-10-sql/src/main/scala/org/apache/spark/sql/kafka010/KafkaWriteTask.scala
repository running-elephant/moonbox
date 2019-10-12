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

package org.apache.spark.sql.kafka010

import java.util.concurrent.Future
import java.{util => ju}

import org.apache.kafka.clients.producer.{Callback, KafkaProducer, ProducerRecord, RecordMetadata}
import org.apache.spark.sql.catalyst.InternalRow
import org.apache.spark.sql.catalyst.expressions.{Cast, Literal, UnsafeProjection}
import org.apache.spark.sql.kafka010.UmsCommon._
import org.apache.spark.sql.types.{BinaryType, StringType, StructType}

import scala.collection.JavaConverters._
import scala.collection.mutable.ArrayBuffer

/**
  * A simple trait for writing out data in a single Spark task, without any concerns about how
  * to commit or abort tasks. Exceptions thrown by the implementation of this class will
  * automatically trigger task aborts.
  */
private[kafka010] class KafkaWriteTask(
                                        producerConfiguration: ju.Map[String, String],
                                        umsParameters: ju.Map[String, String],
                                        inputSchema: StructType,
                                        topic: Option[String]) {
  // used to synchronize with Kafka callbacks
  @volatile private var failedWrite: Exception = null
  private lazy val projection = createProjection

  implicit def toScalaMap(javaMap: ju.Map[String, String]): Map[String, String] = javaMap.asScala.toMap

  private var producer: KafkaProducer[Array[Byte], Array[Byte]] = _

  /**
    * Writes key value data out to topics.
    */
  def execute(iterator: Iterator[InternalRow]): Unit = {
    producer = CachedKafkaProducer.getOrCreate(new ju.HashMap[String, Object](producerConfiguration))
    while (iterator.hasNext && failedWrite == null) {
      val currentRow = iterator.next()
      val projectedRow = projection(currentRow)
      val topic = projectedRow.getUTF8String(0)
      val key = projectedRow.getBinary(1)
      val value = projectedRow.getBinary(2)
      if (topic == null) {
        throw new NullPointerException(s"null topic present in the data. Use the " +
          s"${KafkaSourceProvider.TOPIC_OPTION_KEY} option for setting a default topic.")
      }
      val record = new ProducerRecord[Array[Byte], Array[Byte]](topic.toString, key, value)
      val callback = new Callback() {
        override def onCompletion(recordMetadata: RecordMetadata, e: Exception): Unit = {
          if (failedWrite == null && e != null) {
            failedWrite = e
          }
        }
      }
      producer.send(record, callback)
    }
  }

  /**
    * Writes ums value out to topics.
    */
  def umsExecute(iterator: Iterator[InternalRow]): Unit = {
    producer = CachedKafkaProducer.getOrCreate(new ju.HashMap[String, Object](producerConfiguration))
    var p = 0
    val tupleBuffer = new ArrayBuffer[UmsTuple]
    val umsPayloadSize = umsParameters.getOrElse(UMS_PAYLOAD_SIZE, DEFAULT_UMS_PAYLOAD_SIZE.toString).toInt
    val umsProtocol = UmsProtocol(umsParameters(UMS_PROTOCOL))
    val umsSchema = UmsSchema(umsParameters(UMS_NAMESPACE), inputSchema)
    val key = genKey(umsParameters(UMS_PROTOCOL), umsParameters(UMS_NAMESPACE))

    def genTuple(row: InternalRow): UmsTuple = {
      genUmsTuple(inputSchema, row)
    }

    def genUmsJson: String = {
      val ums = Ums(umsProtocol, umsSchema, tupleBuffer)
      JsonUtils.genJsonString(ums)
    }

    def sendRecord: Future[RecordMetadata] = {
      val value = genUmsJson
      val record = new ProducerRecord[Array[Byte], Array[Byte]](topic.get, key.getBytes("UTF-8"), value.getBytes("UTF-8"))
      val callback = new Callback() {
        override def onCompletion(recordMetadata: RecordMetadata, e: Exception): Unit = {
          if (failedWrite == null && e != null) {
            failedWrite = e
          }
        }
      }
      producer.send(record, callback)
    }

    while (iterator.hasNext && failedWrite == null) {
      val currentRow = iterator.next()
      p += 1
      tupleBuffer.append(genTuple(currentRow))
      if (p == umsPayloadSize) {
        sendRecord
        p = 0
        tupleBuffer.clear()
      }
    }
    if (tupleBuffer.nonEmpty) {
      sendRecord
    }
  }

  def close(): Unit = {
    checkForErrors()
    if (producer != null) {
      producer.flush()
      checkForErrors()
      producer = null
    }
  }

  private def createProjection: UnsafeProjection = {
    val inputAttributes = inputSchema.toAttributes.attrs
    val topicExpression = topic.map(Literal(_)).orElse {
      inputAttributes.find(_.name == KafkaWriter.TOPIC_ATTRIBUTE_NAME)
    }.getOrElse {
      throw new IllegalStateException(s"topic option required when no " +
        s"'${KafkaWriter.TOPIC_ATTRIBUTE_NAME}' attribute is present")
    }
    topicExpression.dataType match {
      case StringType => // good
      case t =>
        throw new IllegalStateException(s"${KafkaWriter.TOPIC_ATTRIBUTE_NAME} " +
          s"attribute unsupported type $t. ${KafkaWriter.TOPIC_ATTRIBUTE_NAME} " +
          "must be a StringType")
    }
    val keyExpression = inputAttributes.find(_.name == KafkaWriter.KEY_ATTRIBUTE_NAME)
      .getOrElse(Literal(null, BinaryType))
    keyExpression.dataType match {
      case StringType | BinaryType => // good
      case t =>
        throw new IllegalStateException(s"${KafkaWriter.KEY_ATTRIBUTE_NAME} " +
          s"attribute unsupported type $t")
    }
    val valueExpression = inputAttributes
      .find(_.name == KafkaWriter.VALUE_ATTRIBUTE_NAME).getOrElse(
      throw new IllegalStateException("Required attribute " +
        s"'${KafkaWriter.VALUE_ATTRIBUTE_NAME}' not found")
    )
    valueExpression.dataType match {
      case StringType | BinaryType => // good
      case t =>
        throw new IllegalStateException(s"${KafkaWriter.VALUE_ATTRIBUTE_NAME} " +
          s"attribute unsupported type $t")
    }
    UnsafeProjection.create(
      Seq(topicExpression, Cast(keyExpression, BinaryType),
        Cast(valueExpression, BinaryType)), inputAttributes)
  }

  private def checkForErrors(): Unit = {
    if (failedWrite != null) {
      throw failedWrite
    }
  }
}

