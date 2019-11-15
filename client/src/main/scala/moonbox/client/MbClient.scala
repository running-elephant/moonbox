package moonbox.client

import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean

import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled.wrappedBuffer
import moonbox.client.utils.{CancelSettableFuture, Util}
import moonbox.network.TransportContext
import moonbox.network.client.{ResponseCallback, TransportClient}
import moonbox.network.server.NoOpRpcHandler
import moonbox.network.util.JavaUtils
import moonbox.protocol.protobuf._

import scala.collection.JavaConverters._

class MbClient(val masterHost: String, val masterPort: Int,
	val user: String, password: String, val appType: String) {

	private val transportContext = new TransportContext(new NoOpRpcHandler, true)
	private val clientFactory = transportContext.createClientFactory
	private var client: TransportClient = _
	private var connectTimeout = 30
	private var readTimeout = 3600
	private var fetchSize = 1000
	private var maxRows = 100
	private var result: CancelSettableFuture[Array[Byte]] = _
	private val executing = new AtomicBoolean(false)

	init()

	private def init(): Unit = {
		val masterClient = clientFactory.createClient(masterHost, masterPort, connectTimeout)
		val request = AccessRequestPB.newBuilder().setUsername(user)
				.setPassword(password)
				.setAppType(appType).build()
		val responseBytes = masterClient.sendSync(wrappedBuffer(request.toByteArray), connectTimeout * 1000)

		val response = AccessResponsePB.getDefaultInstance.getDefaultInstanceForType.getParserForType.parseFrom(JavaUtils.byteBufToByteArray(responseBytes))
		val hostPort = response.getHostPort
		masterClient.close()
		client = clientFactory.createClient(hostPort.getHost, hostPort.getPort, connectTimeout)
	}

	def query(sqls: Seq[String], timeout: Int = readTimeout): MbRowSet = {
		result = new CancelSettableFuture[Array[Byte]] {
			override def interruptTask(): Unit = {
				val cancelRequestPB = ExecutionCancelRequestPB.newBuilder().build()
				client.sendSync(wrappedBuffer(cancelRequestPB.toByteArray), connectTimeout)
			}
		}

		val request = ExecutionRequestPB.newBuilder().setMaxRows(maxRows).setFetchSize(fetchSize).addAllSqls(sqls.asJava).build()

		client.send(wrappedBuffer(request.toByteArray), new ResponseCallback {
			def onSuccess(response: ByteBuf) {
				result.set(response.array())
			}

			def onFailure(e: Throwable) {
				result.setException(e)
			}
		})
		executing.compareAndSet(false, true)

		var response: Array[Byte] = null
		try {
			response = result.get(timeout, TimeUnit.SECONDS)
		} catch {
			case e: InterruptedException =>
		} finally {
			executing.compareAndSet(true, false)
		}

		var hasMore: Boolean = false
		var schema: Schema = null
		var iterator: Iterator[RowPB] = null
		decode(response)

		def decode(data: Array[Byte]): Unit = {
			val resultPB = ExecutionResultPB.getDefaultInstance.getParserForType.parseFrom(data)
			hasMore = resultPB.getHasMore
			val resultDataPB = resultPB.getData
			iterator = resultDataPB.getRowList.asScala.toIterator
			schema = Util.convertSchemaPB(resultDataPB.getSchema)
		}

		val iter = new Iterator[MbRow] {
			override def hasNext: Boolean = {
				if (iterator.hasNext) true
				else {
					if (hasMore) {
						val requestNext = ExecutionResultRequestPB.newBuilder().build()
						val nextResult = client.sendSync(wrappedBuffer(requestNext.toByteArray), readTimeout)
						decode(nextResult.array())
						iterator.hasNext
					} else {
						false
					}
				}
			}

			override def next(): MbRow = Util.convertRowPB(iterator.next(), schema)
		}
		new MbRowSet(iter, schema)
	}

	def cancel(): Unit = {
		if (this.executing.get()) {
			result.cancel(true)
		}
	}

	def setConnectTimeout(timeout: Int): Unit = {
		this.connectTimeout = timeout
	}

	def setReadTimeout(timeout: Int): Unit = {
		this.readTimeout = timeout
	}

	def setFetchSize(size: Int): Unit = {
		this.fetchSize = fetchSize
	}

	def setMaxRows(maxRows: Int): Unit = {
		this.maxRows = maxRows
	}

	def isActive: Boolean = {
		client.isActive
	}

	def close(): Unit = {
		val closeRequest = CloseSessionRequestPB.newBuilder().build()
		client.sendSync(wrappedBuffer(closeRequest.toByteArray), connectTimeout)
		client.close()
	}

}
