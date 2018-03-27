package moonbox.common

import moonbox.common.util.Utils
import org.apache.log4j.{LogManager, PropertyConfigurator}
import org.slf4j.impl.StaticLoggerBinder
import org.slf4j.{Logger, LoggerFactory}


object MbLogging {
	@volatile private var initialized = false
	val initLock = new Object()
	try {
		// We use reflection here to handle the case where users remove the
		// slf4j-to-jul bridge order to route their logs to JUL.
		val bridgeClass = Utils.classForName("org.slf4j.bridge.SLF4JBridgeHandler")
		bridgeClass.getMethod("removeHandlersForRootLogger").invoke(null)
		val installed = bridgeClass.getMethod("isInstalled").invoke(null).asInstanceOf[Boolean]
		if (!installed) {
			bridgeClass.getMethod("install").invoke(null)
		}
	} catch {
		case e: ClassNotFoundException =>
	}
}

trait MbLogging {

	import MbLogging._

	@transient private var logger : Logger = null

	protected def logName = {
		this.getClass.getName.stripSuffix("$")
	}

	protected def log: Logger = {
		if (logger == null) {
			initializeLogIfNecessary()
			logger = LoggerFactory.getLogger(logName)
		}
		logger
	}

	protected def initializeLogIfNecessary(): Unit = {
		if (!initialized) {
			initLock.synchronized {
				if (!initialized) {
					initializeLogging()
				}
			}
		}
	}

	private def initializeLogging(): Unit = {
		val binderClass = StaticLoggerBinder.getSingleton.getLoggerFactoryClassStr
		val usingLog4j12 = "org.slf4j.impl.Log4jLoggerFactory".equals(binderClass)
		if (usingLog4j12) {
			val log4j12Initialized = LogManager.getRootLogger.getAllAppenders.hasMoreElements
			// scalastyle:off println
			if (!log4j12Initialized) {
				val defaultLogProps = "moonbox/common/log4j.properties"
				val customLogProps = Utils.getDefaultLogConfig()
				if (customLogProps.isDefined) {
					PropertyConfigurator.configure(customLogProps.get)
					logInfo(s"Using custom log4j profile: ${customLogProps.get}")
				}
				else {
					Option(Option(Thread.currentThread().getContextClassLoader)
						.getOrElse(getClass.getClassLoader)
						.getResource(defaultLogProps)) match {
						case Some(url) =>
							PropertyConfigurator.configure(url)
							System.err.println(s"Using  default log4j profile: $defaultLogProps")
						case None =>
							System.err.println(s"Unable to load $defaultLogProps")
					}
				}
			}
			// scalastyle:on println
		}
		initialized = true
		log
	}

	protected def logInfo(msg: => String) {
		if (log.isInfoEnabled) log.info(msg)
	}

	protected def logDebug(msg: => String) {
		if (log.isDebugEnabled) log.debug(msg)
	}

	protected def logTrace(msg: => String) {
		if (log.isTraceEnabled) log.trace(msg)
	}

	protected def logWarning(msg: => String) {
		if (log.isWarnEnabled) log.warn(msg)
	}

	protected def logError(msg: => String) {
		if (log.isErrorEnabled) log.error(msg)
	}

	protected def logInfo(msg: => String, throwable: Throwable) {
		if (log.isInfoEnabled) log.info(msg, throwable)
	}

	protected def logDebug(msg: => String, throwable: Throwable) {
		if (log.isDebugEnabled) log.debug(msg, throwable)
	}

	protected def logTrace(msg: => String, throwable: Throwable) {
		if (log.isTraceEnabled) log.trace(msg, throwable)
	}

	protected def logWarning(msg: => String, throwable: Throwable) {
		if (log.isWarnEnabled) log.warn(msg, throwable)
	}

	protected def logError(msg: => String, throwable: Throwable) {
		if (log.isErrorEnabled) log.error(msg, throwable)
	}

	protected def isTraceEnabled: Boolean = {
		log.isTraceEnabled
	}

}
