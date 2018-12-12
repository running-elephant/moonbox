package moonbox.client.exception

case class BackendException(message: String) extends Exception(message)