package moonbox.common.util

import java.util
import java.util.concurrent.CopyOnWriteArrayList

import moonbox.common.MbLogging

import scala.collection.JavaConverters._
import scala.reflect.ClassTag
import scala.util.control.NonFatal

trait ListenerBus[L <: AnyRef, E] extends MbLogging {
	val listeners = new CopyOnWriteArrayList[L]()

	final def addListener(listener: L): Unit = {
		listeners.add(listener)
	}

	final def removeListener(listener: L): Unit = {
		listeners.remove(listener)
	}

	def postToAll(event: E): Unit = {
		val iter: util.Iterator[L] = listeners.iterator()
		while (iter.hasNext) {
			val listener: L = iter.next()
			try {
				doPostEvent(listener, event)
			} catch {
				case NonFatal(e) =>
					logError(s"Listener ${Utils.getFormattedClassName(listener)} threw en exception", e)
			}
		}
	}

	protected def doPostEvent(listener: L, event: E): Unit

	def findListenersByClass[T <: L : ClassTag](): Seq[T] = {
		val c: Class[_] = implicitly[ClassTag[T]].runtimeClass
		listeners.asScala.filter(_.getClass == c).map(_.asInstanceOf[T])
	}
}
