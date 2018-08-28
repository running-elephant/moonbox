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

package moonbox.catalyst.adapter.elasticsearch5.util

import moonbox.catalyst.adapter.elasticsearch5.client.{ActionResponse, EsRestClient}
import moonbox.catalyst.adapter.util.SparkUtil.colId2colNameMap
import moonbox.common.MbLogging
import org.apache.spark.sql.types.StructType
import org.elasticsearch.client.Response
import org.json.JSONObject

import scala.collection.mutable.ArrayBuffer

/** use java iterator, because scala iterator can not set break point in idea evn*/
class EsRowIter[T](index: String,
                   tpe: String,
                   query: String,
                   schema: StructType,
                   mapping: Seq[(String, String)],
                   convert: (Option[StructType], Seq[Any]) => T,
                   limitSize: Int,
                   client: EsRestClient) extends java.util.Iterator[T] with MbLogging{  //T ==> ROW

    private val actionRsp: ActionResponse = new ActionResponse()
    private val result: ArrayBuffer[T] = new ArrayBuffer[T]()
    private var hasProcessLines: Long = 0
    private var shouldProcessLines: Long = 0
    private var isFirstTime: Boolean = true
    private var scrollId: String = _
    private var continue: Boolean = false
    private var resultIter: Iterator[T] = _

    private var runtime: Int = 0

    private def sendBulkMessage(jquery: String, firstQuery: Boolean): Unit = {
        val response: Response = client.performRequestWithScroll(index, tpe, jquery, firstQuery)
        actionRsp.clear()
        result.clear()

        if (!client.isSucceeded(response)) {
            throw new Exception("performScrollRequest to Server return ERROR " + response.getStatusLine.getStatusCode)
        }
        val jsonReqObject = new JSONObject(query)
        val requestLines = jsonReqObject.optLong("size", 10000l) //default 10000

        val content = client.getContent(response)
        val jsonRspObject: JSONObject = new JSONObject(content)
        val responseLines = client.getFieldAsLong(jsonRspObject, "hits/total")
        if(limitSize != -1 ){ //has limit size
            shouldProcessLines = math.min(limitSize, responseLines) //min
        }
        else {
            shouldProcessLines = math.max(limitSize, responseLines) //max
        }
        //if has limit size, we should use the min size of send and receive size, if no limit size, we should use the max size of send and receive size
        var fetchSize: Long = client.handleResponse(content, actionRsp)   //handle
        runtime = runtime +1
        hasProcessLines += fetchSize

        logInfo(s"EsRowIter: $hasProcessLines $requestLines $responseLines count: $runtime ")

        if(client.containsAggs(jsonRspObject)){
            continue = false
        }else{
            scrollId = client.getFieldAsString(jsonRspObject, "_scroll_id")  //update scroll id
            if (hasProcessLines < shouldProcessLines && hasProcessLines != 0) {
                continue = true
            }else{
                continue = false
            }
        }

        val resultColumn: Seq[Seq[Any]] = actionRsp.getResult(schema, colId2colNameMap(mapping))
        val resultRow: Seq[T] = resultColumn.map(convert(Some(schema), _)) //SparkUtil.resultListToJdbcRow(Some(schema), _)
        result.append(resultRow: _*)
        resultIter = result.iterator
    }

    def getNextBulk(): Boolean = {
        if(isFirstTime){  //if the first , get the first one
            sendBulkMessage(query, true)
            isFirstTime = false
        }

        if(!resultIter.hasNext) {   //not the first, if result has one, true; else, try another one
            if (continue) {
                val leftQuery = s"""{"scroll":"2m","scroll_id":"$scrollId"}"""
                sendBulkMessage(leftQuery, false)
                resultIter.hasNext
            }else{
                false
            }
        }
        else {
            true
        }
    }

    def close(): Unit = {
        if(client != null) {
            client.close()
        }
    }

    override def hasNext = {
        getNextBulk()
    }

    override def next() = {
        resultIter.next()
    }
}
