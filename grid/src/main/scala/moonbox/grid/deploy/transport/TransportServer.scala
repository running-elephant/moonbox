package moonbox.grid.deploy.transport

import moonbox.common.MbConf
import moonbox.grid.deploy.MbService
import moonbox.grid.deploy.transport.server.JdbcServer

class TransportServer(conf: MbConf, service: MbService) {
  def start(): Int = {
    new JdbcServer("localhost", 8080, conf, service).start()
    0
  }

  def stop(): Unit = {}
}
