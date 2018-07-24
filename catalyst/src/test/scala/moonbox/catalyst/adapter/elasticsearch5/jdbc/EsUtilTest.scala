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

package moonbox.catalyst.adapter.elasticsearch5.jdbc

import java.util.Properties

object EsUtilTest {

  def url2Prop(url: String): Properties = {
    import scala.collection.JavaConversions._

    val prop = new Properties

    val prefix = "jdbc:es://"
    val hostAndPort = url.stripPrefix(s"$prefix").split("/")(0)
    val indexOrParam = url.stripPrefix(s"$prefix").split("/")(1)
    val indexName = if (indexOrParam.contains("?")) {
      val index = indexOrParam.split('?')(0)
      indexOrParam.split('?')(1).split('&').map { elem =>
        val pair = elem.split('=')
        pair(0) match {
          case "type" => prop.update("type", pair(1))
          case "user" => prop.update("user", pair(1))
          case "password" => prop.update("password", pair(1))
          case _ => prop.update(pair(0), pair(1))
        }
      }
      index
    } else {
      indexOrParam
    }

    prop.put("nodes", hostAndPort)
    prop.put("database", indexName)
    //prop.put("type", mtype) ////prop.put("es.read.field.as.array.include", "user")
    prop.put("user", "admin")
    prop.put("password", "123456")

    prop
  }
}
