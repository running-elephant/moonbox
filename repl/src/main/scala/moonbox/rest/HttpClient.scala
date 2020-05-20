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

package moonbox.rest


import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.{HttpGet, HttpPost}
import org.apache.http.entity.{ContentType, StringEntity}
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
			val entity: StringEntity = new StringEntity(json, ContentType.APPLICATION_JSON)
			post.setEntity(entity)
			val response = client.execute(post)
			EntityUtils.toString(response.getEntity, charset)
		} finally {
			if (client != null) {
				client.close()
			}
		}
	}

	def doGet(url: String, charset: String): String = {
		val client: CloseableHttpClient = HttpClientBuilder.create().build()
		try {
			val get: HttpGet = new HttpGet(url)
			val response = client.execute(get)
			EntityUtils.toString(response.getEntity, charset)
		} finally {
			if (client != null) {
				client.close()
			}
		}
	}

}
