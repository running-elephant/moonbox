package moonbox.grid.deploy


import moonbox.grid.deploy.ConnectionType.ConnectionType

object ConnectionType extends Enumeration {
    type ConnectionType = Value

    val JDBC, CLIENT, REST, ODBC , SYSTEM = Value
}

case class ConnectionInfo(localAddress: String, remoteAddress: String, connectionType: ConnectionType) {
	override def toString: String = {
		s"$localAddress $remoteAddress $connectionType"
	}
}
