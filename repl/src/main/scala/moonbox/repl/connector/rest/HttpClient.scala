/*-
 * <<
 * Moonbox
 * ==
 * Copyright (C) 2016 - 2018 EDP
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

package moonbox.repl.connector.rest

import java.net.URI
import java.nio.charset.StandardCharsets

import moonbox.protocol.client._
import org.apache.http.HttpStatus
import org.apache.http.client.{HttpClient => ApacheHttpClient}
import org.apache.http.client.config.RequestConfig
import org.apache.http.client.entity.EntityBuilder
import org.apache.http.client.methods.{HttpGet, HttpPost, HttpUriRequest}
import org.apache.http.entity.ContentType
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils


class HttpClient(host: String, port: Int, socketTimeout: Int) {

  val _client: ApacheHttpClient = HttpClients.createDefault()
  val PROTOCOL_PREFIX = "http"
  val baseUrl: String = s"$PROTOCOL_PREFIX://$host:$port"
  //  var DEFAULT_SOCKET_TIMEOUT = 1000 * 60 * 30 // 30 minutes
  var DEFAULT_CONNECTION_TIMEOUT = 1000 * 60 * 2 // 2 minutes

  /**
    *
    * @param msg
    * @param api
    * @return http response entity as a json string
    */
  def post(msg: Message, api: String): String = {
    val json = toJson(msg)
    val httpPost = new HttpPost()
    httpPost.setEntity(EntityBuilder.create().setContentType(ContentType.APPLICATION_JSON).setContentEncoding("UTF-8").setText(json).build())
    val uri = s"$baseUrl/$api"
    httpPost.setURI(new URI(uri))
    httpPost.setConfig(createRequestConfig())
    doPost(httpPost)
  }

  def get(uri: String): String = {
    val httpGet = new HttpGet()
    httpGet.setURI(new URI(uri))
    httpGet.setConfig(createRequestConfig())
    doPost(httpGet)
  }

  private def doPost(request: HttpUriRequest): String = {
    val response = _client.execute(request)
    response.getStatusLine.getStatusCode match {
      case HttpStatus.SC_OK =>
        var charset = ContentType.getOrDefault(response.getEntity).getCharset
        if (charset == null) {
          charset = StandardCharsets.UTF_8
        }
        EntityUtils.toString(response.getEntity, charset)
      case code =>
        val msg = s"Status code: $code, ${response.getStatusLine.getReasonPhrase}"
        throw new Exception(msg)
    }
  }

  private def createRequestConfig(connectionTimeout: Int = DEFAULT_CONNECTION_TIMEOUT): RequestConfig = {
    RequestConfig.custom
      .setSocketTimeout(socketTimeout)
      .setConnectTimeout(connectionTimeout)
      .build
  }

}
