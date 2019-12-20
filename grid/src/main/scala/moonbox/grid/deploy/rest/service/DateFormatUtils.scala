package moonbox.grid.deploy.rest.service

import java.text.SimpleDateFormat
import java.util.{Date, Locale}

object DateFormatUtils {
	private val createDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ROOT)
	def formatDate(date: Date) = createDateFormat.format(date)
}
