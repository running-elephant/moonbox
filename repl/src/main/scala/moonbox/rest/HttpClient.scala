package moonbox.rest


import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.{CloseableHttpResponse, HttpGet, HttpPost}
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.{CloseableHttpClient, HttpClientBuilder, HttpClients}
import org.apache.http.message.BasicNameValuePair
import org.apache.http.util.EntityUtils

import scala.collection.JavaConversions._

object HttpClient {
	def doPost(url: String, param: Map[String, String], charset: String): String = {
		val client = HttpClients.createDefault()
		try {
			val post: HttpPost = new HttpPost(url)
			if (param.nonEmpty) {
				val list = param.map { case (k, v) =>
					new BasicNameValuePair(k, v)
				}.toList
				val entity = new UrlEncodedFormEntity(list, charset)
				post.setEntity(entity)
			}
			val response = client.execute(post)
			EntityUtils.toString(response.getEntity, charset)
		} finally {
			if (client != null) {
				client.close()
			}
		}
	}

	def doPost(url: String, json: String, charset: String): String = {
		val client: CloseableHttpClient = HttpClientBuilder.create().build()
		try {
			val post: HttpPost = new HttpPost(url)
			val entity: StringEntity = new StringEntity(json)
			entity.setContentEncoding(charset)
			entity.setContentType("application/json")
			post.setEntity(entity)
			val response = client.execute(post)
			EntityUtils.toString(response.getEntity, charset)
		} finally {
			if (client != null) {
				client.close()
			}
		}
	}

}
