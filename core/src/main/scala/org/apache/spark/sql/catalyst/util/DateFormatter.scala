package org.apache.spark.sql.catalyst.util

import java.time.{LocalDate, ZoneId}
import java.util.Locale

import DateTimeUtil.{convertSpecialDate, localDateToDays}

sealed trait DateFormatter extends Serializable {
  def parse(s: String): Int // returns days since epoch
  def format(days: Int): String
}

class Iso8601DateFormatter(
                            pattern: String,
                            zoneId: ZoneId,
                            locale: Locale) extends DateFormatter with DateTimeFormatterHelper {

  @transient
  private lazy val formatter = getOrCreateFormatter(pattern, locale)

  override def parse(s: String): Int = {
    val specialDate = convertSpecialDate(s.trim, zoneId)
    specialDate.getOrElse {
      val localDate = LocalDate.parse(s, formatter)
      localDateToDays(localDate)
    }
  }

  override def format(days: Int): String = {
    LocalDate.ofEpochDay(days).format(formatter)
  }
}

object DateFormatter {
  val defaultPattern: String = "uuuu-MM-dd"
  val defaultLocale: Locale = Locale.US

  def apply(format: String, zoneId: ZoneId, locale: Locale): DateFormatter = {
    new Iso8601DateFormatter(format, zoneId, locale)
  }

  def apply(format: String, zoneId: ZoneId): DateFormatter = {
    apply(format, zoneId, defaultLocale)
  }

  def apply(zoneId: ZoneId): DateFormatter = apply(defaultPattern, zoneId)
}

