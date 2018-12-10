package moonbox.client.entity

import java.net.InetSocketAddress

case class ConnectionState(serverAddress: InetSocketAddress,
                           username: String,
                           readTimeout: Int,
                           token: Option[String] = None,
                           sessionId: Option[String] = None,
                           dataFetchAddress: InetSocketAddress,
                           isLocal: Boolean = true) {
  def toSeq= {
    Seq("server_address" :: serverAddress :: Nil,
//      "port" :: port :: Nil,
      "username" :: username :: Nil,
      "token" :: token.orNull :: Nil,
      "session_id" :: sessionId.orNull :: Nil,
      "read_timeout" :: s"$readTimeout ms" :: Nil,
      "data_fetch_address" :: dataFetchAddress :: Nil,
//      "data_fetch_port" :: dataFetchAddress.getPort :: Nil,
      "is_local" :: isLocal :: Nil
    )
  }
}

case class JobState(message: String,
                    state: String)