package moonbox.grid.deploy2.audit

import java.io.{ByteArrayOutputStream, InputStream}
import java.util

import org.apache.http.entity.{ContentType, StringEntity}
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder
import org.apache.http.{HttpEntity, HttpHost}
import org.elasticsearch.client.{Response, RestClient, RestClientBuilder}
import org.json.{JSONArray, JSONObject}
import org.json4s.DefaultFormats

import scala.collection.JavaConverters._

class ElasticSearchRestClient(conf: Map[String, String]) {
    val nodes: Array[String] = conf("moonbox.audit.nodes").split(",")   // 1.1.1.1:9200,2.2.2.2:9200
    val user: Option[String] = conf.get("moonbox.audit.user")
    val password: Option[String] = conf.get("moonbox.audit.password")
    val version: Int = 5

    val restClient: RestClient = {
        val httpHost: Array[HttpHost] = nodes.map { node =>
            val array: Array[String] = node.split(":")
            if (array.length > 1) {
                new HttpHost(array(0), Integer.valueOf(array(1)), "http")
            } else {
                new HttpHost(array(0), 9200, "http")
            }
        }
        if (user.isDefined && password.isDefined) {             //support user and password for auth in ES
            import org.apache.http.auth.{AuthScope, UsernamePasswordCredentials}
            import org.apache.http.impl.client.BasicCredentialsProvider

            val credentialsProvider = new BasicCredentialsProvider
            credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(user.get, password.get))

            RestClient.builder(httpHost: _*).setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
                @Override
                override def customizeHttpClient(httpClientBuilder: HttpAsyncClientBuilder): HttpAsyncClientBuilder = {
                    httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider)
                }
            }).build()
        } else {
            RestClient.builder(httpHost: _*).build()
        }
    }

    /** bulk put data to */
    def sendBatchData[T <: Product : Manifest](index: String, mtype: String, schema: Map[String, Int], batchData: Seq[T]): (Boolean, Long) = {
        if (batchData.isEmpty) {
            return (true, 0)
        }

        val mappingIndex: Option[Int] = if (conf.contains("es.mapping.id")) { //field or property name containing the document id.
            schema.get(conf("es.mapping.id"))
        } else {
            None
        }

        //build bulk req string
        val bulkRequest = batchData.map { data =>
            val bulkHeader = if (mappingIndex.isDefined) {
                s"""{"index": {"_index": "$index", "_type": "$mtype", "_id": "${data.productElement(mappingIndex.get)}"}}\n"""
            } else {
                s"""{"index": {"_index": "$index", "_type": "$mtype"}}\n"""
            }

            //get string from JSON Object
            import org.json4s.native.Serialization.write
            implicit val jsonFormats: DefaultFormats.type = DefaultFormats
            val jsonString: String = data match {
                case a: AnyRef => write(a)
                case _ => ""
            }
            bulkHeader + s"$jsonString\n"
        }.mkString("")

        //send bulk req
        val entityReq: HttpEntity = new StringEntity(bulkRequest, ContentType.APPLICATION_JSON)
        val response: Response = restClient.performRequest("PUT", s"""/_bulk""", new util.Hashtable[String, String](), entityReq)

        //read bulk response
        handleBulkResponse(response)
    }

    private def handleBulkResponse(response: Response): (Boolean, Long) = {
        val content = getContent(response)
        val jsonObject = new JSONObject(content)
        val hasError = getFieldAsString(jsonObject, "errors")
        val indexResult = if (hasError == "false") {
            true
        } else {
            false
        }

        var succeedNum: Long = 0l
        val items: JSONArray = jsonObject.getJSONArray("items")
        for (item <- items.asScala) {
            val bulkResult = item.asInstanceOf[JSONObject]
            if (bulkResult.has("index")) {
                val index = bulkResult.getJSONObject("index")
                succeedNum += getFieldAsLong(index, "_shards/successful")
                if (getFieldAsLong(index, "_shards/failed") != 0) {
                    val seq_no = getFieldAsLong(index, "_seq_no")
                    throw new Exception(s"EsRestClient write failed for err=$seq_no, ret=$indexResult, suc=$succeedNum")
                }
            }
        }
        (indexResult, succeedNum)
    }

    private def buildESType(dataType: String): String = {
        //in es5, index became a boolean property, that is why setting it to not_analyzed doesn't have any effect.
        //If you want to have a non analyzed field, you need to use "type": "keyword"
        val indexOption = if (version >= 5) {
            "true"
        } else {
            "not_analyzed"
        }
        dataType.toLowerCase match {
            case "byte" => s"""{"type": "byte", "index": "$indexOption"}"""
            //case StringType  => "text"  //keyword：存储数据时候，不会分词建立索引; text: 存储数据时候，会自动分词，并生成索引
            case "boolean" => s"""{"type": "boolean", "index": "$indexOption"}"""
            case "double" => s"""{"type": "double", "index": "$indexOption"}"""
            case "short" => s"""{"type": "short", "index": "$indexOption"}"""
            case "float" => s"""{"type": "float", "index": "$indexOption"}"""
            case "int" => s"""{"type": "integer", "index": "$indexOption"}"""
            case "long" => s"""{"type": "long", "index": "$indexOption"}"""
            case "string" => s"""{"type": "keyword", "index": "$indexOption"}""" //only for es5 ,es 2.*版本里面是没有keyword text两个字段，只有string字段
            case "date" => s"""{"type": "date", "index": "$indexOption"}"""
            case "timestamp" => s"""{"type": "date", "format": "strict_date_optional_time||epoch_millis", "index": "$indexOption"}""" //long  <-- date
        }
    }

    private def generateMapping(index: String, mtype: String, schema: Map[String, String]): String = {
        val propContent = schema.map { field =>
            s""" "${field._1}": ${buildESType(field._2)}"""
        }.mkString(",\n")

        s"""{
           |"mappings": {
           |   "$mtype": {
           |       "_all": { "enabled": true  },
           |       "properties":{
           |            $propContent
           |        }
           |    }
           |  }
           |}""".stripMargin
    }

    private def isResponseOk(response: Response): Boolean = {
        response.getStatusLine.getStatusCode >= 200 && response.getStatusLine.getStatusCode < 300
    }

    def createIndexWithSchema(index: String, mtype: String, schema: Map[String, String]): Boolean = {
        val mapping = generateMapping(index, mtype, schema)
        println("putSchema" + mapping)

        val entityReq: HttpEntity = new StringEntity(mapping, ContentType.APPLICATION_JSON)
        val response: Response = restClient.performRequest("PUT", s"""/$index""", new util.Hashtable[String, String](), entityReq)
        isResponseOk(response)
    }

    private def getContent(response: Response): String = {
        val entityRsp: InputStream = response.getEntity.getContent

        val out = new ByteArrayOutputStream()
        val buffer = new Array[Byte](1024)
        var len = entityRsp.read(buffer, 0, buffer.length)
        while (len != -1) {
            out.write(buffer, 0, len)
            len = entityRsp.read(buffer, 0, buffer.length)
        }

        val jsonStr = new String(out.toByteArray)
        entityRsp.close()
        jsonStr
    }

    private def getFieldAsString(jsonObject: JSONObject, field: String) = {
        jsonObject.get(field).toString
    }

    private def getFieldAsLong(jsonObject: JSONObject, field: String) = {
        val fields = field.split("/")
        val obj = getParentField(jsonObject, fields)
        obj.getLong(fields(fields.length - 1))
    }

    private def getParentField(parent: JSONObject, fields: Seq[String]): JSONObject = {
        var obj = parent
        var i = 0
        fields.foreach { field =>
            if (i + 1 < fields.length) {
                obj = obj.getJSONObject(field)
                i = i + 1
            }
        }
        obj
    }

    def indexExist(index: String): Boolean = {
        val response = restClient.performRequest("GET", "/_aliases", new util.Hashtable[String, String]())
        val jsonStr = getContent(response)
        val jsonObject = new JSONObject(jsonStr)
        if (isResponseOk(response)) {
            jsonObject.keySet.asScala.contains(index)
        } else {
            throw new Exception("can not communicate Elasticsearch")
        }
    }

    def close() = {
        restClient.close()
    }
}
