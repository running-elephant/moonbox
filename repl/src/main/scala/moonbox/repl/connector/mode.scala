package moonbox.repl.connector

object ConnectionType extends Enumeration {
  type ConnectionType = Value
  val JDBC, REST = Value
}

object RuntimeMode extends Enumeration {
  type RuntimeMode = Value
  val LOCAL, CLUSTER = Value
}
