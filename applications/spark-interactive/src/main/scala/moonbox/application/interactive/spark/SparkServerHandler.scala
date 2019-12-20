package moonbox.application.interactive.spark

import java.math.BigDecimal
import java.sql.{Date, Timestamp}
import java.util
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

import akka.actor.ActorRef
import com.google.protobuf.ByteString
import io.netty.buffer.{ByteBuf, Unpooled}
import moonbox.common.{MbConf, MbLogging}
import moonbox.network.server.{RpcCallContext, RpcHandler}
import moonbox.network.util.JavaUtils
import moonbox.protocol.protobuf.{DataTypePB, ExecutionRequestPB, _}
import org.apache.spark.sql.Row
import org.apache.spark.sql.types._

import scala.collection.JavaConverters._
import scala.concurrent.Future
import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global

class SparkServerHandler(mbConf: MbConf, actorRef: ActorRef) extends RpcHandler with MbLogging {

	private val sessionIdToRunner = new ConcurrentHashMap[String, Runner]

	override def receive(request: ByteBuf, context: RpcCallContext): Unit = {
		try {
			val req = AppRequestMessage.getDefaultInstance.getParserForType
					.parseFrom(JavaUtils.byteBufToByteArray(request))
			if (req.hasOpenSession) {
				handleOpenSession(req.getOpenSession, context)
			}
			else if (req.hasExecute) {
				handleExecute(req.getExecute, context)
			}
			else if (req.hasExecuteResult) {
				handleExecuteResult(req.getExecuteResult, context)
			}
			else if (req.hasExecteCancel) {
				handleExecuteCancel(req.getExecteCancel, context)
			}
			else if (req.hasCloseSession) {
				handleCloseSession(req.getCloseSession, context)
			}
			else {
				context.sendFailure(new UnsupportedOperationException("Unsupport app request: " + req))
			}
		}
		catch {
			case e: Exception =>
				logError("process error: ", e)
				context.sendFailure(e)
		}
	}

	private def handleOpenSession(openSession: OpenSessionRequestPB, context: RpcCallContext) {
		val sessionId = newSessionId()
		val configMap: util.Map[String, String] = openSession.getConfigMap
		val session = openSession.getSession.getSessionMap
		val user = session.get("user")
		val org = session.get("org")
		logInfo(s"$org@$user ask to open session.")
		Future(new Runner(sessionId, org, user, Option(configMap.get("database")), mbConf, configMap.asScala.toMap, actorRef)).onComplete {
			case Success(runner) =>
				sessionIdToRunner.put(sessionId, runner)
				val responsePB: OpenSessionResponsePB = OpenSessionResponsePB.newBuilder.setSessionId(sessionId).build
				logInfo(s"session $sessionId opened for user $org@$user.")
				context.reply(Unpooled.wrappedBuffer(responsePB.toByteArray))
			case Failure(e) =>
				context.sendFailure(e)
		}
	}

	private def handleCloseSession(closeSession: CloseSessionRequestPB, context: RpcCallContext) {
		val sessionId = closeSession.getSessionId
		Option(sessionIdToRunner.remove(sessionId)).foreach { runner =>
			runner.cancel()
		}
		val responsePB: CloseSessionResponsePB = CloseSessionResponsePB.newBuilder.build
		context.reply(Unpooled.wrappedBuffer(responsePB.toByteArray))
	}

	private def handleExecute(executeRequest: ExecutionRequestPB, context: RpcCallContext) {
		val sessionId: String = executeRequest.getSessionId
		Option(sessionIdToRunner.get(sessionId)) match {
			case Some(runner) =>
				val maxRows = executeRequest.getMaxRows
				val fetchSize = executeRequest.getFetchSize
				val sqlsList = executeRequest.getSqlsList.asScala
				Future(runner.query(sqlsList, fetchSize, maxRows)).onComplete {
					case Success(result) =>
						context.reply(Unpooled.wrappedBuffer(constructExecuteResult(result).toByteArray))
					case Failure(e) =>
						context.sendFailure(e)
				}
			case None =>
				throw new RuntimeException("session lost, please reconnect.")
		}
	}

	private def handleExecuteResult(resultRequest: ExecutionResultRequestPB, context: RpcCallContext): Unit = {
		val sessionId = resultRequest.getSessionId
		Option(sessionIdToRunner.get(sessionId)) match {
			case Some(runner) =>
				Future(runner.fetchResultData()).onComplete {
					case Success(fetchResult) =>
						context.reply(Unpooled.wrappedBuffer(constructExecuteResult(fetchResult).toByteArray))
					case Failure(e) =>
						context.sendFailure(e)
				}
			case None =>
				throw new RuntimeException("session lost, please reconnect.")
		}
	}

	private def handleExecuteCancel(cancelRequest: ExecutionCancelRequestPB, context: RpcCallContext): Unit = {
		val sessionId = cancelRequest.getSessionId
		Option(sessionIdToRunner.get(sessionId)) match {
			case Some(runner) =>
				val cancelResult = runner.cancel()
				context.reply(Unpooled.wrappedBuffer(constructExecuteResult(cancelResult).toByteArray))
			case None =>
				throw new RuntimeException("session lost, please reconnect.")
		}
	}

	private def newSessionId(): String = {
		UUID.randomUUID().toString
	}

	private def constructExecuteResult(queryResult: QueryResult): ExecutionResultPB = {
		ExecutionResultPB.newBuilder().setHasMore(queryResult.hasMore)
				.setData(
					ResultDataPB.newBuilder().setSchema(schemaConvert(queryResult.schema))
							.addAllRows(queryResult.data.map(row => rowConvert(row, queryResult.schema)).asJava).build()
				).build()
	}

	private def schemaConvert(schema: StructType): StructTypePB = {
		val fields = schema.map { field => {
			StructFieldPB.newBuilder()
					.setName(field.name)
					.setNullable(field.nullable)
					.setMetaData(field.metadata.json)
			    .setDataType(dataTypeConvert(field.dataType)).build()
		}
		}
		StructTypePB.newBuilder().addAllFields(fields.asJava).build()
	}

	private def dataTypeConvert(dt: DataType): DataTypePB = {
		dt match {
			case ByteType =>
				DataTypePB.newBuilder().setByteType(ByteTypePB.newBuilder()).build()

			case ShortType =>
				DataTypePB.newBuilder().setShortType(ShortTypePB.newBuilder()).build()

			case IntegerType =>
				DataTypePB.newBuilder().setIntegerType(IntegerTypePB.newBuilder()).build()

			case LongType =>
				DataTypePB.newBuilder().setLongType(LongTypePB.newBuilder()).build()

			case FloatType =>
				DataTypePB.newBuilder().setFloatType(FloatTypePB.newBuilder()).build()

			case DoubleType =>
				DataTypePB.newBuilder().setDoubleType(DoubleTypePB.newBuilder()).build()

			case decimal: DecimalType =>
				DataTypePB.newBuilder().setDecimalType(
					DecimalTypePB.newBuilder().setScale(decimal.scale).setPrecision(decimal.precision)
				).build()

			case BooleanType =>
				DataTypePB.newBuilder().setBooleanType(BooleanTypePB.newBuilder()).build()

			case char: CharType =>
				DataTypePB.newBuilder().setCharType(CharTypePB.newBuilder()).build()

			case varchar: VarcharType =>
				DataTypePB.newBuilder().setVarcharType(VarcharTypePB.newBuilder()).build()

			case StringType =>
				DataTypePB.newBuilder().setStringType(StringTypePB.newBuilder()).build()

			case DateType =>
				DataTypePB.newBuilder().setDateType(DateTypePB.newBuilder()).build()

			case TimestampType =>
				DataTypePB.newBuilder().setTimestampType(TimestampTypePB.newBuilder()).build()

			case BinaryType =>
				DataTypePB.newBuilder().setBinaryType(BinaryTypePB.newBuilder()).build()

			case array: ArrayType =>
				DataTypePB.newBuilder().setArrayType(
					ArrayTypePB.newBuilder().setElementType(dataTypeConvert(array.elementType))
				).build()

			case map: MapType =>
				DataTypePB.newBuilder().setMapType(
					MapTypePB.newBuilder()
							.setKeyType(dataTypeConvert(map.keyType))
							.setValueType(dataTypeConvert(map.valueType))
				).build()

			case struct: StructType =>
				DataTypePB.newBuilder().setStrucType(schemaConvert(struct)).build()

			case obj: ObjectType =>
				DataTypePB.newBuilder().setObjectType(ObjectTypePB.newBuilder()).build()

			case NullType =>
				DataTypePB.newBuilder().setNullType(NullTypePB.newBuilder()).build()

			case _ =>
				throw new Exception(s"unsupport datatype $dt")
		}
	}

	private def rowConvert(row: Row, schema: StructType): RowPB = {
		val fields = for (i <- 0 until row.length) yield {
			valueConvert(row.get(i), schema.fields(i).dataType)
		}
		RowPB.newBuilder().addAllFields(fields.asJava).build()
	}

	private def valueConvert(value: Any, dataType: DataType): Value = {
		val builder = Value.newBuilder()
		dataType match {
			case ByteType =>
				if (value != null) {
					builder.setIntValue(value.asInstanceOf[Byte])
				} else {
					builder.clearIntValue()
				}

			case ShortType =>
				if (value != null) {
					builder.setIntValue(value.asInstanceOf[Short])
				} else {
					builder.clearIntValue()
				}

			case IntegerType =>
				if (value != null) {
					builder.setIntValue(value.asInstanceOf[Int])
				} else {
					builder.clearIntValue()
				}

			case LongType =>
				if (value != null) {
					builder.setLongValue(value.asInstanceOf[Long])
				} else {
					builder.clearLongValue()
				}

			case FloatType =>
				if (value != null) {
					builder.setFloatValue(value.asInstanceOf[Float])
				} else {
					builder.clearFloatValue()
				}

			case DoubleType =>
				if (value != null) {
					builder.setDoubleValue(value.asInstanceOf[Double])
				} else {
					builder.clearDoubleValue()
				}

			case decimal: DecimalType =>
				if (value != null) {
					val bigDecimal = value.asInstanceOf[BigDecimal]
					val bingInteger = BigIntegerPB.newBuilder().setIntVals(ByteString.copyFrom(bigDecimal.unscaledValue().toByteArray))
					builder.setBigDecimalValue(BigDecimalPB.newBuilder().setBigInteger(bingInteger).build())
				} else {
					builder.clearBigDecimalValue()
				}

			case BooleanType =>
				if (value != null) {
					builder.setBooleanValue(value.asInstanceOf[Boolean])
				} else {
					builder.clearBooleanValue()
				}

			case char: CharType =>
				if (value != null) {
					builder.setIntValue(value.asInstanceOf[Char])
				} else {
					builder.clearIntValue()
				}

			case varchar: VarcharType =>
				if (value != null) {
					builder.setStringValue(value.asInstanceOf[String])
				} else {
					builder.clearStringValue()
				}

			case string: StringType =>
				if (value != null) {
					builder.setStringValue(value.asInstanceOf[String])
				} else {
					builder.clearStringValue()
				}

			case DateType =>
				if (value != null) {
					builder.setLongValue(value.asInstanceOf[Date].getTime)
				} else {
					builder.clearLongValue()
				}

			case TimestampType =>
				if (value != null) {
					builder.setLongValue(value.asInstanceOf[Timestamp].getTime)
				} else {
					builder.clearLongValue()
				}

			case BinaryType =>
				if (value != null) {
					builder.setBytesValue(ByteString.copyFrom(value.asInstanceOf[Array[Byte]]))
				} else {
					builder.clearBytesValue()
				}

			case array: ArrayType =>
				if (value != null) {
					// value.asInstanceOf[mutable.WrappedArray[Any]].toArray
					val arrayValues = value.asInstanceOf[Seq[Any]].map { v =>
						valueConvert(v, array.elementType)
					}
					builder.setArrayValue(ArrayPB.newBuilder().addAllValues(arrayValues.asJava))
				} else {
					builder.clearArrayValue()
				}

			case map: MapType =>
				if (value != null) {
					val mapValues = value.asInstanceOf[Map[Any, Any]].map { case (k, v) =>
						k.toString -> valueConvert(v, map.valueType)
					}

					builder.setMapValue(MapPB.newBuilder().putAllValues(mapValues.asJava))
				} else {
					builder.clearMapValue()
				}

			case struct: StructType =>
				if (value != null) {
					builder.setStructValue(StructPB.newBuilder().setRow(rowConvert(value.asInstanceOf[Row], struct)))
				} else {
					builder.clearStructValue()
				}

			case NullType =>
				builder.setNullValue(NullPB.NULL_VALUE)

			// case obj: ObjectType | _ =>
			case _ =>
				throw new Exception(s"unsupport datatype $dataType")
		}
		builder.build()
	}

}
