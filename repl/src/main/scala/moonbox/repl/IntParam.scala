package moonbox.repl

object IntParam {
	def unapply(arg: String): Option[Int] = {
		try {
			Some(arg.toInt)
		} catch {
			case e: NumberFormatException => None
		}
	}
}