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

package moonbox.common.message

/**
  * jdbc client                                jdbc server                         moonbox grid
  *
  * JdbcLoginInbound     ----->	     JdbcLoginInbound -> LoginInbound     ----->     LoginInbound
  *                                                                                       |
  *                                                                                       V
  * JdbcLoginOutbound    <-----     JdbcLoginOutbound <- LoginOutbound    <-----     LoginOutbound
  * --------------------------------------------------------
  * JdbcQueryInbound     ----->	     JdbcQueryInbound -> QueryInbound     ----->     QueryInbound
  *                                                                                       |
  *                                                                                       V
  * JdbcQueryOutbound    <-----     JdbcQueryOutbound <- QueryOutbound    <-----     QueryOutbound
  *
  *
  */

trait JdbcMessage
trait JdbcInboundMessage extends JdbcMessage
trait JdbcOutboundMessage extends JdbcMessage
trait OneWayMessage extends JdbcInboundMessage

/**
  * @param messageId     The identifier of the dataFetch connection
  * @param jobId         used to retrieve data from redis cache as key
  * @param startRowIndex The start index of the whole ResultSet (including all blocks)
  * @param fetchSize     The count of rows in this block
  * @param totalRows     The total count of rows in the  whole ResultSet (including all blocks)
  */
case class DataFetchState(messageId: Long, jobId: String, startRowIndex: Long, fetchSize: Long, totalRows: Long)

case class JdbcLoginInbound(messageId: Long, user: String, password: String, database: String) extends JdbcInboundMessage
case class JdbcLoginOutbound(messageId: Long, err: Option[String], message: Option[String]) extends JdbcOutboundMessage

case class JdbcLogoutInbound(messageId: Long) extends JdbcInboundMessage
case class JdbcLogoutOutbound(messageId: Long,  err: Option[String], message: Option[String]) extends JdbcOutboundMessage

case class JdbcQueryInbound(messageId: Long, fetchSize: Int, sql: String) extends JdbcInboundMessage // specify the data fetch size, default is 0
case class JdbcQueryOutbound(messageId: Long, err: Option[String], data: Option[Seq[Seq[Any]]], schema: Option[String], dataSize: Option[Long]) extends JdbcOutboundMessage

case class DataFetchInbound(dataFetchState: DataFetchState) extends JdbcInboundMessage
case class DataFetchOutbound(dataFetchState: DataFetchState, err: Option[String], data: Option[Seq[Seq[Any]]], schema: Option[String]) extends JdbcOutboundMessage

case class JdbcCancelInbound(messageId: Long, cancelMsg: Long) extends JdbcInboundMessage
case class JdbcCancelOutbound(messageId: Long, error: Option[String], state: Option[Boolean]) extends JdbcOutboundMessage // true refers to cancel successfully, otherwise failed

case class EchoInbound(messageId: Long, content: Any) extends JdbcInboundMessage
case class EchoOutbound(messageId: Long, content: Option[Any]) extends JdbcOutboundMessage

