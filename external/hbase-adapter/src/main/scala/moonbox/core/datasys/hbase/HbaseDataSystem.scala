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

package moonbox.core.datasys.hbase

import moonbox.common.MbLogging
import moonbox.core.datasys.DataSystem
import org.apache.hadoop.hbase.client.{Admin, ConnectionFactory, HBaseAdmin}
import org.apache.hadoop.hbase.{HBaseConfiguration, HConstants}

class HbaseDataSystem(props: Map[String, String]) extends DataSystem(props) with MbLogging {
  checkOptions(HConstants.ZOOKEEPER_QUORUM)

  private def getClient: Admin = {
    val conf = HBaseConfiguration.create
    conf.set(HConstants.ZOOKEEPER_QUORUM, props(HConstants.ZOOKEEPER_QUORUM))
    conf.set(HConstants.ZOOKEEPER_CLIENT_PORT, props.getOrElse(HConstants.ZOOKEEPER_CLIENT_PORT, HConstants.DEFAULT_ZOOKEPER_CLIENT_PORT.toString))
    conf.set(HConstants.ZOOKEEPER_ZNODE_PARENT, props.getOrElse(HConstants.ZOOKEEPER_ZNODE_PARENT, HConstants.DEFAULT_ZOOKEEPER_ZNODE_PARENT))
    val connection = ConnectionFactory.createConnection(conf)
    connection.getAdmin
  }

  override def tableNames(): Seq[String] = {
    val admin = getClient
    val tables: Seq[String] = admin.listTableNames().map(_.getNameAsString)
    admin.close()
    tables
  }

  override def tableName(): String = {
    throw new Exception("Function tableName no implementation, for HBASE does not support physical mount")
  }

  override def tableProperties(tableName: String): Map[String, String] = {
    throw new Exception("Function tableProperties no implementation, for HBASE does not support physical mount")
  }

  override def test(): Unit = {
    try {
      val conf = HBaseConfiguration.create
      conf.set(HConstants.ZOOKEEPER_QUORUM, props(HConstants.ZOOKEEPER_QUORUM))
      conf.set(HConstants.ZOOKEEPER_CLIENT_PORT, props.getOrElse(HConstants.ZOOKEEPER_CLIENT_PORT, HConstants.DEFAULT_ZOOKEPER_CLIENT_PORT.toString))
      conf.set(HConstants.ZOOKEEPER_ZNODE_PARENT, props.getOrElse(HConstants.ZOOKEEPER_ZNODE_PARENT, HConstants.DEFAULT_ZOOKEEPER_ZNODE_PARENT))
      HBaseAdmin.checkHBaseAvailable(conf)
    } catch {
      case e: Throwable =>
        logError("hbase test failed", e)
        throw e
    }
  }
}
