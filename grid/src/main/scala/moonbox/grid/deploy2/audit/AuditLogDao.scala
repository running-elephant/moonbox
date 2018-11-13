package moonbox.grid.deploy2.audit

import java.util.concurrent.ConcurrentLinkedQueue

import moonbox.common.MbLogging

import scala.collection.mutable.ArrayBuffer

trait AuditLogDao extends MbLogging{
    protected val _queue = new ConcurrentLinkedQueue[AuditEvent]()

    def createTableIfNotExist()

    def postEvent(event: AuditEvent) //sync post a event
    def postEvents(event: Seq[AuditEvent]) //sync post some events
    def getBatchSize: Int //get every sink batch size
    def close()

    def hasPlentySize(): Boolean = {
        _queue.size() >= getBatchSize
    }

    def postBatchEvent(): Unit = {
        val batchSize = getBatchSize
        val events = getBatchEvent(batchSize)
        logDebug(s"send $batchSize : ${events.size} ")
        postEvents(events)
    }

    def postAsynEvent(event: AuditEvent): Boolean = { //async post a event
        _queue.add(event)
    }

    def getBatchEvent(batchSize: Int): Seq[AuditEvent] = {
        val buffer: ArrayBuffer[AuditEvent] = ArrayBuffer.empty[AuditEvent]
        while (buffer.size < batchSize && !_queue.isEmpty) {
            buffer += _queue.poll()
        }
        buffer
    }

}

