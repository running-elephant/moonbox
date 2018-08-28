package moonbox.security.strategy

import java.nio.charset.StandardCharsets
import java.security.MessageDigest

/**
  * Mask function to apply to a column with String type
  */
object Mask {
  val name = "mask"
  /**
    * col denotes the column
    * indexes denotes the indexes to mask. e.g.
    * ---------------------------------------------
    * mask(id_card_code, "(0, 2), (5, 3), (10, 8)")
    * ---------------------------------------------
    * 0, 5 and 10 denotes the mask start index
    * 2, 3 and 8 denotes the mask length
    * ---------------------------------------------
    */
  val function = (column: Any, indexes: String) => {
    val stringBuilder = new StringBuilder
    val columnString = column.toString
    if (indexes != null && indexes.length > 0) {
      val idxs = StrategyUtils.parseIndex1(columnString, indexes)
      idxs.foreach { case (start, len) =>
        val end = stringBuilder.size
        if (end < start) {
          stringBuilder.append(columnString.substring(end, start))
          stringBuilder.append("*" * len)
        } else if (end < start + len) {
          stringBuilder.append("*" * (start + len - end))
        } else {
          stringBuilder.append(columnString.substring(start + len, end))
        }
      }
      if (stringBuilder.size < columnString.length) {
        stringBuilder.append(columnString.substring(stringBuilder.size, columnString.length))
      }
      stringBuilder.toString()
    } else {
      columnString
    }
  }
}

object Truncate {
  val name: String = "truncate"
  /**
    * start inclusive, end exclusive.
    */
  val function = (column: Any, index: String) => {
    val columnString = column.toString
    val idxes = StrategyUtils.parseIndex2(columnString, index)
    if (idxes.nonEmpty) {
      val (start, end) = idxes.head
      columnString.substring(start, end)
    } else throw new IllegalArgumentException("Invalid indexes parameters of truncate strategy function.")
  }
}

/*----------------Hide Strategies---------------*/
/* The return type of this builtin strategy is always Double */
object HideNumber {
  val name: String = "hideNumber"
  val function = (column: AnyVal, target: String) => {
    column match {
      case _: Double => target.toDouble
      case _: Int => target.toInt
      case _: Float => target.toFloat
      case _: Byte => target.toByte
      case _: Long => target.toLong
      case _: Short => target.toShort
    }
  }
}

object HideBoolean {
  val name: String = "hideBoolean"
  val function = (_: Boolean, target: String) => target.toBoolean
}

object HideByte {
  val name: String = "hideByte"
  val function = (_: Byte, target: String) => target.toByte
}

object HideShort {
  val name: String = "hideShort"
  val function = (_: Short, target: String) => target.toShort
}

object HideInt {
  val name: String = "hideInt"
  val function = (_: Int, target: String) => target.toInt
}

object HideLong {
  val name: String = "hideLong"
  val function = (_: Long, target: String) => target.toLong
}

object HideFloat {
  val name: String = "hideFloat"
  val function = (_: Float, target: String) => target.toFloat
}

object HideDouble {
  val name: String = "hideDouble"
  val function = (_: Double, target: String) => target.toDouble
}

object HideString {
  val name: String = "hideString"
  val function = (_: String, target: String) => target
}

/*----------------Shift Strategies---------------*/
/* The return type of this builtin strategy is always Double */
object Shift {
  val name: String = "shift"
  val function = (column: AnyVal, value: String) => {
    val v = value.toInt
    column match {
      case col: Double => col + v
      case col: Int => col + v
      case col: Float => col + v
      case col: Byte => col + v
      case col: Long => col + v
      case col: Short => col + v
    }
  }
}

object ShiftByte {
  val name: String = "shiftByte"
  val function = (column: Byte, value: String) => column + value.toByte
}

object ShiftShort {
  val name: String = "shiftShort"
  val function = (column: Short, value: String) => column + value.toShort
}

object ShiftInt {
  val name: String = "shiftInt"
  val function = (column: Int, value: String) => column + value.toInt
}

object ShiftLong {
  val name: String = "shiftLong"
  val function = (column: Long, value: String) => column + value.toLong
}

object ShiftFloat {
  val name: String = "shiftFloat"
  val function = (column: Float, value: String) => column + value.toFloat
}

object ShiftDouble {
  val name: String = "shiftDouble"
  val function = (column: Double, value: String) => column + value.toDouble
}

/**
  * Generate a fixed-length Integer
  */
object Hash {
  val name: String = "hash"
  val function = (column: Any) => {
    val md = MessageDigest.getInstance("MD5")
    StrategyUtils.encodeHexString(md.digest(column.toString.getBytes(StandardCharsets.UTF_8)))
  }
}
