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

package moonbox.repl.connector

trait Connector {

  protected var max_count: Int = 500 /* max rows to show in console */
  protected var truncate: Int = 50 /* the column length to truncate, 0 denotes unabridged */

  def prepare(host: String, port: Int, user: String, pwd: String, db: String): Boolean
  def process(sqls: Seq[String]): Unit
  def close(): Unit
  def shutdown(): Unit
  def cancel(): Unit
  def connectionState: ConnectionState
  def setTimeout(timeSeconds: Int): Unit = {
    Option(connectionState) match {
      case Some(x) => x.timeout = timeSeconds * 1000
      case None => throw new Exception("Connection state is uninitialized.")
    }
  }
  def setFetchSize(size: Long): Unit = {
    Option(connectionState) match {
      case Some(x) => x.fetchSize = size
      case None => throw new Exception("Connection state is uninitialized.")
    }
  }
  def setMaxColumnLength(length: Int): Unit = {
    Option(connectionState) match {
      case Some(x) => x.maxColumnLength = length
      case None => throw new Exception("Connection state is uninitialized.")
    }
  }
  def setMaxRowsShow(size: Int): Unit = {
    Option(connectionState) match {
      case Some(x) => x.maxRowsShow = size
      case None => throw new Exception("Connection state is uninitialized.")
    }
  }
}
