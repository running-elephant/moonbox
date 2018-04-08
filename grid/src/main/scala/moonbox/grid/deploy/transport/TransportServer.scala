package moonbox.grid.deploy.transport

import moonbox.common.MbConf
import moonbox.grid.deploy.MbService
import moonbox.grid.deploy.transport.server.JdbcServer

class TransportServer(host: String, port: Int, conf: MbConf, service: MbService) {
  def start(): Int = {
    new JdbcServer(host, port, conf, service).start()
  }

  def stop(): Unit = {}
}
