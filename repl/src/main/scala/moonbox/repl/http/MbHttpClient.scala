package moonbox.repl.http

import java.net.URI
import java.nio.charset.StandardCharsets

import moonbox.common.message.RestEntity
import org.apache.http.HttpStatus
import org.apache.http.client.HttpClient
import org.apache.http.client.config.RequestConfig
import org.apache.http.client.entity.EntityBuilder
import org.apache.http.client.methods.{HttpGet, HttpPost}
import org.apache.http.entity.ContentType
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils


class MbHttpClient(host: String, port: Int, socketTimeout: Int) {

  val _client: HttpClient = HttpClients.createDefault()
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
  def post(msg: RestEntity, api: String): String = {
    val json = toJson(msg)
    val httpPost = new HttpPost()
    httpPost.setEntity(EntityBuilder.create().setContentType(ContentType.APPLICATION_JSON).setContentEncoding("UTF-8").setText(json).build())
    val uri = s"$baseUrl/$api"
    httpPost.setURI(new URI(uri))
    httpPost.setConfig(createRequestConfig())
    val response = _client.execute(httpPost)
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

  def get(uri: String): String = {
    val httpGet = new HttpGet()
    httpGet.setURI(new URI(uri))
    httpGet.setConfig(createRequestConfig())
    val response = _client.execute(httpGet)
    var charset = ContentType.getOrDefault(response.getEntity).getCharset
    if (charset == null) {
      charset = StandardCharsets.UTF_8
    }
    EntityUtils.toString(response.getEntity, charset)
  }

  private def createRequestConfig(connectionTimeout: Int = DEFAULT_CONNECTION_TIMEOUT): RequestConfig = {
    RequestConfig.custom
      .setSocketTimeout(socketTimeout)
      .setConnectTimeout(connectionTimeout)
      .build
  }

}
