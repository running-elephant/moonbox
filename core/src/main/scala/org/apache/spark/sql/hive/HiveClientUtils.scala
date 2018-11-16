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

package org.apache.spark.sql.hive

import java.util.concurrent.ConcurrentHashMap

import org.apache.spark.SparkConf
import org.apache.spark.deploy.SparkHadoopUtil
import org.apache.spark.sql.hive.client.HiveClient

object HiveClientUtils {
  private val clients = new ConcurrentHashMap[String, HiveClient]()

  def getHiveClient(props: Map[String, String]) = {
    Option(clients.get(props("metastore.url"))).getOrElse {
      val sparkConf = new SparkConf()
        .set("spark.hadoop.javax.jdo.option.ConnectionURL", props("metastore.url"))
        .set("spark.hadoop.javax.jdo.option.ConnectionDriverName", props("metastore.driver"))
        .set("spark.hadoop.javax.jdo.option.ConnectionUserName", props("metastore.user"))
        .set("spark.hadoop.javax.jdo.option.ConnectionPassword", props("metastore.password"))
        .setAll(props.filterKeys(_.startsWith("spark.hadoop.")))
        .setAll(props.filterKeys(_.startsWith("spark.sql.")))
      val client = HiveUtils.newClientForMetadata(sparkConf, SparkHadoopUtil.get.newConfiguration(sparkConf))
      clients.put(props("metastore.url"), client)
      client
    }
  }
}
