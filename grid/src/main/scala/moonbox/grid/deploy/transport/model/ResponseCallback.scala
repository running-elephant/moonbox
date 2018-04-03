package moonbox.grid.deploy.transport.model

trait ResponseCallback {
  def apply(response: JdbcOutboundMessage)
}
