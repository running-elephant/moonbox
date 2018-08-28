package moonbox.security.strategy

object StrategyUtils {

  val DIGITS_LOWER = Array('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f')
  val DIGITS_UPPER = Array('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F')

  /**
    * parse a string of indexes to sorted tuples of Int.
    * indexes format: "(start0, length0), (start1, length1), (start2, length2)" // start >= 0
    *
    * @param columnString the input column with string format
    * @param indexes      the indexes with string format
    * @return sorted indexes
    */
  def parseIndex1(columnString: String, indexes: String): Seq[(Int, Int)] = {
    indexes.stripPrefix("(").stripSuffix(")")
      .replaceAll("\\s+", "")
      .split("\\),\\(")
      .toSeq.map { pair =>
      val p = pair.split(",")
      p.length match {
        case 1 => (p(0).toInt, columnString.length - p(0).toInt)
        case 2 => (p(0).toInt, p(1).toInt)
        case _ => null
      }
    }.filterNot(t => t == null || t._1 >= columnString.length).map {
      case (start, len) =>
        var s = start
        var l = len
        if (l < 0) throw new IllegalArgumentException("Invalid indexes parameters of strategy function.")
        if (s < 0) s = 0
        if (s + l > columnString.length) l = columnString.length - s
        (s, l)
    }.sortWith((t1, t2) => t1._1 < t2._1)
  }

  /**
    * parse a string of indexes to sorted tuples of Int.
    * indexes format: "(start0, end0), (start1, end1), (start2, end2)"  // start inclusive, end exclusive
    *
    * @param columnString the input column with string format
    * @param indexes      the indexes with string format
    * @return sorted indexes
    */
  def parseIndex2(columnString: String, indexes: String): Seq[(Int, Int)] = {
    indexes.stripPrefix("(").stripSuffix(")")
      .replaceAll("\\s+", "")
      .split("\\),\\(")
      .toSeq.map { pair =>
      val p = pair.split(",")
      p.length match {
        case 1 => (p(0).toInt, columnString.length - p(0).toInt)
        case 2 => (p(0).toInt, p(1).toInt)
        case _ => null
      }
    }.filterNot(t => t == null || t._1 > columnString.length || t._2 <= 0 || t._1 >= t._2).map {
      case (start, end) =>
        var s = start
        var e = end
        if (s < 0) s = 0
        if (e > columnString.length) e = columnString.length
        (s, e)
    }
  }

  def encodeHexString(data: Array[Byte]): String = {
    encodeHexString(data, DIGITS_LOWER)
  }

  private def encodeHexString(data: Array[Byte], toDigits: Array[Char]): String = {
    val l = data.length
    val out = new Array[Char](l << 1)
    // two characters form the hex value.
    var i, j = 0
    while (i < l) {
      out(j) = toDigits((0xF0 & data(i)) >>> 4)
      j += 1
      out(j) = toDigits(0x0F & data(i))
      j += 1
      i += 1
    }
    new String(out)
  }

}
