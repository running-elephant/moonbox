package moonbox.repl

trait FrontCommand {

	def command: Seq[String]
	def accept(cmd: String): Boolean
	def description: String
}

object ShowHistory extends FrontCommand {

	override def command: Seq[String] = Seq("history", "h")

	override def accept(cmd: String): Boolean = command.contains(cmd.toLowerCase)

	override def description: String = {
		"show history MQLs"
	}
}

object ShowState extends FrontCommand {

	override def command: Seq[String] = Seq("state", "status")

	override def accept(cmd: String): Boolean = command.contains(cmd.toLowerCase)

	override def description: String = {
		"show connection state"
	}
}

object ShowHelp extends FrontCommand {

	override def command: Seq[String] = Seq("help")

	override def accept(cmd: String): Boolean = command.contains(cmd.toLowerCase)

	override def description: String = {
		"show all MQLs"
	}
}

object ShowCommands extends FrontCommand {

	override def command: Seq[String] = Seq("commands", "cmds")

	override def accept(cmd: String): Boolean = command.contains(cmd.toLowerCase)

	override def description: String = {
		"show commands in common use"
	}
}

object Reconnect extends FrontCommand {

	override def command: Seq[String] = Seq("reconnect", "r")

	override def accept(cmd: String): Boolean = command.contains(cmd.toLowerCase)

	override def description: String = {
		"reconnect to moonbox server"
	}
}

object Exit extends FrontCommand {

	override def command: Seq[String] = Seq("exit", "quit", "q")

	override def accept(cmd: String): Boolean = command.contains(cmd.toLowerCase)

	override def description: String = {
		"exit shell"
	}
}

object ClearScreen extends FrontCommand {

	override def command: Seq[String] = Seq("clear", "cls")

	override def accept(cmd: String): Boolean = command.contains(cmd.toLowerCase)

	override def description: String = {
		"clear screen"
	}
}

object SetTruncate extends FrontCommand {

	override def command: Seq[String] = Seq("%set truncate=[int]")

	override def accept(cmd: String): Boolean = {
		cmd.toLowerCase.startsWith("%set") && cmd.stripPrefix("%set").trim.startsWith("truncate")
	}

	override def description: String = {
		"set the column length to truncate, 0 denotes unabridged"
	}
}

object SetMaxRows extends FrontCommand {

	override def command: Seq[String] = Seq("%set max_rows=[int]")

	override def accept(cmd: String): Boolean = {
		cmd.toLowerCase.startsWith("%set") && cmd.stripPrefix("%set").trim.startsWith("max_rows")
	}

	override def description: String = {
		"set max rows to show in console"
	}
}

object SetTimeout extends FrontCommand {

	override def command: Seq[String] = Seq("%set timeout=[int]")

	override def accept(cmd: String): Boolean = {
		cmd.toLowerCase.startsWith("%set") && cmd.stripPrefix("%set").trim.startsWith("timeout")
	}

	override def description: String = {
		"set connection timeout(second) to moonbox server"
	}
}

object SetFetchSize extends FrontCommand {

	override def command: Seq[String] = Seq("%set fetch_size=[int]")

	override def accept(cmd: String): Boolean = {
		cmd.toLowerCase.startsWith("%set") && cmd.stripPrefix("%set").trim.startsWith("fetch_size")
	}

	override def description: String = {
		"set size for per data fetch"
	}
}

