package moonbox.grid.deploy.rest.service

import java.text.{DateFormat, SimpleDateFormat}
import java.util.{Date, Locale}

object DateFormatUtils {

	private val localDateFormatter = new ThreadLocal[DateFormat]() {
		override def initialValue(): DateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ROOT)
	}

	def formatDate(date: Date) = localDateFormatter.get().format(date)

}
