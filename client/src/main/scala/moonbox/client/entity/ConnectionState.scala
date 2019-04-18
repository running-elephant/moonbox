/*-
 * <<
 * Moonbox
 * ==
 * Copyright (C) 2016 - 2019 EDP
 * ==
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * >>
 */

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
//      "token" :: token.orNull :: Nil,
//      "session_id" :: sessionId.orNull :: Nil,
      "read_timeout" :: s"$readTimeout ms" :: Nil,
      "data_fetch_address" :: dataFetchAddress :: Nil,
//      "data_fetch_port" :: dataFetchAddress.getPort :: Nil,
      "is_local" :: isLocal :: Nil
    )
  }
}

case class JobState(message: String,
                    state: String)
