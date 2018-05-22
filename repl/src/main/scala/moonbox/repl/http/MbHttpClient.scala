package moonbox.repl.http

import java.net.URI
import java.nio.charset.StandardCharsets

import com.fasterxml.jackson.databind.{DeserializationFeature, SerializationFeature}
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import moonbox.common.message.RestEntity
import org.apache.http.client.HttpClient
import org.apache.http.client.entity.EntityBuilder
import org.apache.http.client.methods.{HttpGet, HttpPost}
import org.apache.http.entity.ContentType
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils
import org.json4s.jackson.JsonMethods


class MbHttpClient(host: String, port: Int) {

  val _client: HttpClient = HttpClients.createDefault()
  val PROTOCOL_PREFIX = "http"
  val baseUrl: String = s"$PROTOCOL_PREFIX://$host:$port"

  def toJson(msg: AnyRef): String = {
    val mapper = JsonMethods.mapper
      .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
      .configure(SerializationFeature.INDENT_OUTPUT, true)
      .registerModule(DefaultScalaModule)
    mapper.writeValueAsString(msg)
  }

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
    val response = _client.execute(httpPost)
    var charset = ContentType.getOrDefault(response.getEntity).getCharset
    if (charset == null){
      charset = StandardCharsets.UTF_8
    }
    EntityUtils.toString(response.getEntity, charset)
  }

  def get(uri: String): String = {
    val httpGet = new HttpGet()
    httpGet.setURI(new URI(uri))
    val response = _client.execute(httpGet)
    var charset = ContentType.getOrDefault(response.getEntity).getCharset
    if (charset == null){
      charset = StandardCharsets.UTF_8
    }
    EntityUtils.toString(response.getEntity, charset)
  }

}
