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

package moonbox.protocol.util

import com.google.protobuf.{ByteString, Timestamp}
import moonbox.message.protobuf._
import moonbox.protocol.DataType

import scala.collection.JavaConverters._

object ProtoOutboundMessageBuilder {

	def loginOutbound(token: String, error: String): LoginOutbound = {
		val builder = LoginOutbound.newBuilder()
		if (error != null) {
			builder.setError(error)
		}
		if (token != null) {
			builder.setToken(token)
		}
		builder.build()
	}

	def logoutOutbound(error: String): LogoutOutbound = {
		val builder = LogoutOutbound.newBuilder()
		if (error != null) {
			builder.setError(error)
		}
		builder.build()
	}

	def openSessionOutbound(sessionId: String, workerHost: Option[String], workerPort: Option[Int], error: String): OpenSessionOutbound = {
		val builder = OpenSessionOutbound.newBuilder()
		Option(error).foreach(builder.setError)
		Option(sessionId).foreach(builder.setSessionId)
		workerHost.foreach(builder.setWorkerHost)
		workerPort.foreach(builder.setWorkerPort)
		builder.build()
	}

	def closeSessionOutbound(error: String): CloseSessionOutbound = {
		val builder = CloseSessionOutbound.newBuilder()
		if (error != null) {
			builder.setError(error)
		}
		builder.build()
	}

	def interactiveQueryOutbound(error: String): InteractiveQueryOutbound = {
		val builder = InteractiveQueryOutbound.newBuilder()
		Option(error).foreach(builder.setError)
		builder.build()
	}

	def interactiveQueryOutbound(error: String, resultData: ResultData): InteractiveQueryOutbound = {
		val builder = InteractiveQueryOutbound.newBuilder()
		if (resultData != null) {
			builder.setResultData(resultData)
		}
		Option(error).foreach(builder.setError)
		builder.build()
	}

	def resultData(cursor: String, schema: String, data: Data, hasNext: Boolean): ResultData = {
		ResultData.newBuilder()
			.setSchema(schema)
			.setData(data)
			.setHasNext(hasNext)
			.build()
	}

	def resultData(schema: String, data: Data, hasNext: Boolean): ResultData = {
		ResultData.newBuilder()
			.setSchema(schema)
			.setData(data)
			.setHasNext(hasNext)
			.build()
	}

	def interactiveNextResultOutbound(error: String, schema: String, data: Seq[Seq[Any]], hasNext: Boolean): InteractiveNextResultOutbound = {
		val pData = protoData(data, schema)
		val resData: ResultData = resultData(schema, pData, hasNext)
		interactiveNextResultOutbound(error, resData)
	}

	def interactiveNextResultOutbound(error: String, cursor: String, schema: String, data: Seq[Seq[Any]], hasNext: Boolean): InteractiveNextResultOutbound = {
		val pData = protoData(data, schema)
		val resData: ResultData = resultData(schema, cursor, pData, hasNext)
		interactiveNextResultOutbound(error, resData)
	}

	def interactiveNextResultOutbound(error: String, data: ResultData): InteractiveNextResultOutbound = {
		val builder = InteractiveNextResultOutbound.newBuilder()
		if (error != null) {
			builder.setError(error)
		}
		if (data != null) {
			builder.setData(data)
		}
		builder.build()
	}

	def batchQueryOutbound(jobId: String, error: String): BatchQueryOutbound = {
		val builder = BatchQueryOutbound.newBuilder()
		if (error != null) {
			builder.setError(error)
		}
		if (jobId != null) {
			builder.setJobId(jobId)
		}
		builder.build()
	}

	def batchQueryProgressOutbound(message: String, appId: String, state: String): BatchQueryProgressOutbound = {
		val builder = BatchQueryProgressOutbound.newBuilder()
		if (message != null) {
			builder.setMessage(message)
		}
		if (state != null) {
			builder.setState(state)
		}
		builder.build()
	}

	def interactiveQueryCancelOutbound(error: String): InteractiveQueryCancelOutbound = {
		val builder = InteractiveQueryCancelOutbound.newBuilder()
		Option(error).foreach(builder.setError)
		builder.build()
	}

	def batchQueryCancelOutbound(error: String): BatchQueryCancelOutbound = {
		val builder = BatchQueryCancelOutbound.newBuilder()
		Option(error).foreach(builder.setError)
		builder.build()
	}

	def protoData(data: Seq[Seq[Any]], schema: String): Data = {
		val parsed = SchemaUtil.parse(schema)
		val dataTypes = SchemaUtil.schemaToDataType(parsed).map(_._2)
		val rows = data map (row => protoRow(row, dataTypes))
		Data.newBuilder().addAllRow(rows.asJava).build()
	}

	def protoRow(row: Seq[Any], dataTypes: Array[DataType]): Row = {
		val cells = row.zipWithIndex.map(elem => protoCell(elem._1, dataTypes(elem._2)))
		Row.newBuilder().addAllCell(cells.asJava).build()
	}

	def protoCell(data: Any, dataType: DataType): Cell = {
		import DataType._
		val builder = Cell.newBuilder()
		dataType match {
			case DECIMAL =>
				if (data != null) {
					builder.setBigDecimal(protoBDecimal(data.asInstanceOf[java.math.BigDecimal])) /* java.math.BigDecimal => proto.BDecimal */
				}
				else {
					builder.clearBigDecimal()
				}
			case BINARY =>
				if (data != null) {
					builder.setByteArray(ByteString.copyFrom(data.asInstanceOf[Array[Byte]]))
				} else {
					builder.clearByteArray()
				}
			case BOOLEAN =>
				if (data != null) {
					builder.setBooleanValue(data.asInstanceOf[Boolean])
				} else {
					builder.clearBooleanValue()
				}
			case VARCHAR | STRING =>
				if (data != null) {
					builder.setStringValue(data.asInstanceOf[String])
				} else {
					builder.clearStringValue()
				}
			case TIMESTAMP =>
				if (data != null) {
					builder.setLongValue(data.asInstanceOf[java.sql.Timestamp].getTime) /* java.sql.timestamp => long */
				} else {
					builder.clearLongValue()
				}
			case DOUBLE =>
				if (data != null) {
					builder.setDoubleValue(data.asInstanceOf[Double])
				} else {
					builder.clearDoubleValue()
				}

			case FLOAT =>
				if (data != null) {
					builder.setFloatValue(data.asInstanceOf[Float])
				} else {
					builder.clearFloatValue()
				}

			case INTEGER =>
				if (data != null) {
					builder.setIntValue(data.asInstanceOf[Int])
				} else {
					builder.clearIntValue()
				}

			case LONG =>
				if (data != null) {
					builder.setLongValue(data.asInstanceOf[Long])
				} else {
					builder.clearLongValue()
				}

			case SHORT =>
				if (data != null) {
					builder.setIntValue(data.asInstanceOf[Short]) /* short => int */
				} else {
					builder.clearIntValue()
				}

			case BYTE =>
				if (data != null) {
					builder.setIntValue(data.asInstanceOf[Byte]) /* byte => int */
				} else {
					builder.clearIntValue()
				}

			case DATE =>
				if (data != null) {
					builder.setLongValue(data.asInstanceOf[java.sql.Date].getTime) /* java.sql.Date => long */
				} else {
					builder.clearLongValue()
				}

			case _ =>
				if (data != null) {
					builder.setByteArray(ByteString.copyFrom(JavaSerializer.serialize[Object](data.asInstanceOf[Object]))) /* other types => Array[Byte] serialized by java serializer */
				} else {
					builder.clearByteArray()
				}
			// case CHAR =>
			// case NULL =>
			// case OBJECT =>
			// case STRUCT =>
			// case MAP =>
			// case ARRAY =>
		}
		builder.build()
	}

	def protoBDecimal(decimal: java.math.BigDecimal): BDecimal = {
		val bInteger = BInteger.newBuilder()
			.setValue(ByteString.copyFrom(decimal.unscaledValue().toByteArray))
			.build()
		BDecimal.newBuilder()
			.setScale(decimal.scale())
			.setIntVal(bInteger)
			.build()
	}

	def protoTimeStamp(timestamp: java.sql.Timestamp): Timestamp = {
		val seconds = timestamp.getTime / 1000
		val nanos = timestamp.getNanos
		Timestamp.newBuilder()
			.setSeconds(seconds)
			.setNanos(nanos)
			.build()
	}
}
