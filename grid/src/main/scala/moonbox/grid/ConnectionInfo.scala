package moonbox.grid

import java.net.InetSocketAddress

import moonbox.grid.ConnectionType.ConnectionType

object ConnectionType extends Enumeration {
    type ConnectionType = Value

    val JDBC, REST, ODBC, TIMER, LOCAL = Value
}

case class ConnectionInfo(client: InetSocketAddress, local: InetSocketAddress, method: ConnectionType = ConnectionType.REST) {
    def getClient = client.toString.replaceFirst("/", "")

    def getLocal = local.toString.replaceFirst("/", "")

    def getAccess = method.toString.toLowerCase
}