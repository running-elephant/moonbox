package moonbox.grid.deploy.transport.model

import moonbox.common.message.JdbcOutboundMessage

trait ResponseCallback {
  def apply(response: JdbcOutboundMessage)
}
