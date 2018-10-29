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

package moonbox.util

import java.util.Properties

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

object MoonboxJDBCUtils {
  val URL_PREFIX: String = "jdbc:moonbox://"
  val DB_NAME = "database"
  val USER_KEY = "user"
  val PASSWORD_KEY = "password"
  val FETCH_SIZE = "fetchsize"
  val DEFAULT_PORT = 8080
  val HOSTS_AND_PORTS = "nodes" //host1:port1,host2:port2,host3:port3
  val MODE_KEY = "mode"

  def parseURL(url: String, defaults: Properties): Properties = {
    val resProps = if (defaults != null) defaults else new Properties()
    if (url != null && url.toUpperCase().startsWith(URL_PREFIX.toUpperCase)) {
      val props = url.substring(URL_PREFIX.length).split("\\?")
      val hpAndDB = props(0).split("/")
      resProps.setProperty(HOSTS_AND_PORTS, hpAndDB(0))
      if (hpAndDB.length == 2)
        resProps.setProperty(DB_NAME, hpAndDB(1))
      if (props.length == 2) {
        props(1).split("&").map { kv: String =>
          val temp = kv.split("=")
          if (temp.length == 2)
            (temp(0), temp(1))
          else null
        }.filter(_ != null).foreach(kv => resProps.setProperty(kv._1, kv._2))
      }
    }
    resProps
  }

  def parseHostsAndPorts(hostsAndPorts: String): Seq[(String, String)] = {
    if (hostsAndPorts != null && hostsAndPorts.length > 0)
      hostsAndPorts.split(",").map { hp =>
        val h_p = hp.split(":")
        if (h_p.length == 2) {
          (h_p(0).trim, h_p(1).trim)
        } else null
      }.filter(_ != null).toSeq
    else null
  }

  def splitHostPort(hp: String): (String, Int) = {
    if (hp == null) {
      throw new Exception("Host and port cannot be null.")
    } else {
      val res = hp.split(":")
      (res(0).trim, res(1).trim.toInt)
    }
  }

  def splitSql(sql: String, splitter: Char): Seq[String] = {
    val stack = new mutable.Stack[Char]()
    val splitIndex = new ArrayBuffer[Int]()
    for ((char, idx) <- sql.toCharArray.zipWithIndex) {
      if (char == splitter) {
        if (stack.isEmpty) splitIndex += idx
      }
      if (char == '(') stack.push('(')
      if (char == ')') stack.pop()
    }
    splits(sql, splitIndex.toArray, 0).map(_.stripPrefix(splitter.toString).trim).filter(_.length > 0)
  }

  private def splits(sql: String, idxs: Array[Int], offset: Int): Seq[String] = {
    if (idxs.nonEmpty) {
      val head = idxs.head
      val (h, t) = sql.splitAt(head - offset)
      h +: splits(t, idxs.tail, head)
    } else sql :: Nil
  }

}
