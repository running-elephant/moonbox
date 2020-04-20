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

import java.io.File

import com.typesafe.config.ConfigFactory
import org.apache.commons.codec.Charsets
import org.json.JSONObject

import scala.collection.JavaConverters._
import scala.collection.mutable

object MoonboxCluster {

  private var args: Array[String] = _

  def main(args: Array[String]) {
    this.args = args
    val home = sys.env.getOrElse("MOONBOX_HOME",
      throw new Exception("MOONBOX_HOME not found in env"))
    val host = sys.env.getOrElse("MOONBOX_MASTER_HOST",
      throw new Exception("MOONBOX_MASTER_HOST not found in env."))

    val configFilePath = s"$home${File.separator}conf${File.separator}moonbox-defaults.conf"
    val file = new File(configFilePath)
    if (!file.exists()) {
      println("moonbox-defaults.conf does not exist.")
      System.exit(-1)
    }

    val config = ConfigFactory.parseFile(file)

    val restPort = if (config.hasPath("moonbox.deploy.rest.port")) {
      config.getInt("moonbox.deploy.rest.port")
    } else 9090
    val url = s"http://$host:$restPort/management"
    var path: String = ""
    var key: String = ""
    args.headOption match {
      case Some(arg) =>
        if (arg.equalsIgnoreCase("apps")) {
          path = url + "/apps-info"
          key = "apps"
          getInfo(path, key)
        } else if (arg.equalsIgnoreCase("workers")) {
          path = url + "/cluster-info"
          key = "cluster"
          getInfo(path, key)
        } else if (arg.equalsIgnoreCase("drivers")) {
          path = url + "/drivers-info"
          key = "drivers"
          getInfo(path, key)
        } else if (arg.equalsIgnoreCase("config-set")) {
          path = url + "/config-set"
          setConfig(path)
        } else {
          printUsageAndExit(-1)
        }
      case None =>
        printUsageAndExit(-1)
    }
  }

  private def getInfo(path: String, key: String): Unit = {
    val response: String = HttpClient.doGet(path, Charsets.UTF_8.name())

    val array = new JSONObject(response).getJSONArray(key).asScala
    println(s"$key size is ${array.size}")
    println(array.mkString("\n"))
  }

  private def setConfig(path: String): Unit = {
    assert(args.size == 2, "config-set command should have 1 parameters like key1=value1,key2=value2")
    val config = new mutable.HashMap[String, String]
    args(1).split(",").foreach { keyValues =>
      val kv = keyValues.trim.split("=")
      assert(kv.length >= 2, "please check config format.")
      config.put(kv(0).trim, keyValues.substring(kv(0).length + 1).trim)
    }
    val jsonObject = new JSONObject()
      .put("config", config.toMap.asJava)

    val response: String = HttpClient.doPost(path, jsonObject.toString, Charsets.UTF_8.name())

    val message = new JSONObject(response).getString("message")
    println(message)
  }

  private def printUsageAndExit(exitCode: Int): Unit = {
    // scalastyle: off println
    System.err.println(
      "Usage: moonbox-cluster [options]\n" +
        "options:\n" +
        "   apps             List current running apps.\n" +
        "   drivers          List current running batch drivers.\n" +
        "   workers          List current running workers.\n" +
        "   config-set       Set grid config, like key1=value1,key2=value2.\n"
    )
    System.exit(exitCode)
  }
}
