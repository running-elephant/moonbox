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

package moonbox.catalyst.adapter.elasticsearch5.client

import java.io._
import java.sql.{Date, Timestamp}
import java.util
import java.util.{ArrayList, Properties}

import moonbox.catalyst.adapter.elasticsearch5.client.AggWrapper.AggregationType
import org.apache.spark.sql.types._
import org.elasticsearch.client.RestClientBuilder
import org.json.JSONArray

import scala.collection.mutable
import scala.collection.JavaConverters._

//import com.alibaba.fastjson.{JSON, JSONArray, JSONObject}
import org.apache.http.entity.{ContentType, StringEntity}
import org.apache.http.{HttpEntity, HttpHost}
import org.elasticsearch.client.{Response, RestClient}
import org.json.JSONObject


case class ShapeType(name: String, tpe: AnyRef, level: Int)

class EsRestClient(param: Map[String, String]) {

    val nodes: Array[String] = param("nodes").split(",")   // 1.1.1.1:9200,2.2.2.2:9200
    //val port = param.getOrElse("es.port", "9200")
    val user: Option[String] = param.get("user")
    val password: Option[String] = param.get("password")
    var version: Seq[Int] = Seq(5,3,2)
    //TODO: more shape type
    val geoShapeMap = Set("POINT", "LINE_STRING", "POLYGON", "MULTI_POINT", "MULTI_LINE_STRING",
        "MULTI_POLYGON",  "GEOMETRY_COLLECTION", "ENVELOPE", "CIRCLE"
    )
    //TODO: more point type
    val geoPointMap = Set("LAT_LON_OBJECT",  "LAT_LON_STRING", "GEOHASH", "LON_LAT_ARRAY" )

    val restClient: RestClient = {
        val httpHost: Array[HttpHost] = nodes.map{node =>
            val array: Array[String] = node.split(":")
            if(array.length > 1) {
                new HttpHost(array(0), Integer.valueOf(array(1)), "http")
            }else {
                new HttpHost(array(0), 9200, "http")
            }
        }

        //https://www.elastic.co/guide/en/elasticsearch/client/java-rest/5.3/_basic_authentication.html
        if(user.isDefined && password.isDefined) {
            import org.apache.http.impl.client.BasicCredentialsProvider
            import org.apache.http.auth.AuthScope
            import org.apache.http.auth.UsernamePasswordCredentials
            import org.apache.http.impl.nio.client.HttpAsyncClientBuilder

            val credentialsProvider = new BasicCredentialsProvider
            credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(user.get, password.get))
            //support user and password for auth in ES
            RestClient.builder(httpHost: _*).setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
                @Override
                override def customizeHttpClient(httpClientBuilder: HttpAsyncClientBuilder): HttpAsyncClientBuilder = {
                    httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider)
                }
            }).build()

        }else {
            RestClient.builder(httpHost: _*).build()
        }
    }

    Version()

    def getContent(response: Response): String = {
        val entityRsp: InputStream = response.getEntity.getContent

        val out = new ByteArrayOutputStream()
        val buffer = new Array[Byte](1024)
        var len = entityRsp.read(buffer, 0, buffer.length)
        while(len != -1){
            out.write(buffer, 0, len)
            len = entityRsp.read(buffer, 0, buffer.length)
        }

        val jsonStr = new String(out.toByteArray)
        entityRsp.close()
        //println(jsonStr)
        jsonStr
    }

    /**{"name" : "node-1",
        "cluster_name" : "edp-es",
        "cluster_uuid" : "4cFGDzEQRD2HCSzV8442Mg",
        "version" : {
            "number" : "5.3.2",
            "build_hash" : "3068195",
            "build_date" : "2017-04-24T16:15:59.481Z",
            "build_snapshot" : false,
            "lucene_version" : "6.4.2"
        },
        "tagline" : "You Know, for Search"}
    **/

    /** like show databases, it shows indexs **/
    def getIndices(): Seq[String] = {
        val response = restClient.performRequest("GET", "/_aliases", new util.Hashtable[String, String]())
        val jsonStr = getContent(response)
        val jsonObject = new JSONObject(jsonStr)
        if(isSucceeded(response)){
            import scala.collection.JavaConversions._
            jsonObject.keySet.toSeq
        }else{
            Seq.empty[String]
        }
    }

    /** likes show tables, it shows (index - type) list**/
    def getIndicesAndType(): Seq[(String, String)] = {
        val response: Response = restClient.performRequest("GET", s"""/_mapping?pretty=true""", new util.Hashtable[String, String]())
        val jsonStr = getContent(response)
        val jsonObject = new JSONObject(jsonStr)
        if(isSucceeded(response)) {
            import scala.collection.JavaConversions._
            jsonObject.keySet.flatMap { index =>
                val body = jsonObject.getJSONObject(index)
                val mapping :JSONObject = body.getJSONObject("mappings")
                mapping.keySet().map { tpe => (index, tpe) }.toSeq
            }.toSeq

        }else {
            Seq.empty[(String, String)]
        }
    }


    /** https://www.elastic.co/guide/en/elasticsearch/reference/5.3/indices-stats.html **/
    def getStats(index: String): (Long, Long) = {  //(doc num, doc size)
        val response = restClient.performRequest("GET", s"$index/_stats/docs,store", new util.Hashtable[String, String]())
        val jsonStr = getContent(response)
        val jsonObject = new JSONObject(jsonStr)
        if(isSucceeded(response)){
            val count = getFieldAsLong(jsonObject, s"indices/${index}/total/docs/count")
            val size = getFieldAsLong(jsonObject, s"indices/${index}/total/store/size_in_bytes")
            (count, size)
        }else{
            (0L, 0L)
        }
    }

    private def Version(): Unit = {
        val response = restClient.performRequest("GET", "", new util.Hashtable[String, String]())
        val jsonStr = getContent(response)
        val jsonObject = new JSONObject(jsonStr)
        version = if(isSucceeded(response)){
            val number = jsonObject.getJSONObject("version").get("number").toString
            number.split('.').map(_.toInt)
        }else{
            Seq(5,3,2) //default version
        }
    }

    def getVersion(): Seq[Int] =  { version }


    def getSchema(index: String, mtype: String) : (StructType, Set[String]) = {
        val response: Response = restClient.performRequest("GET", s"""/$index/_mapping/$mtype""", new util.Hashtable[String, String]())
        val jsonStr = getContent(response)
        val jsonObject = new JSONObject(jsonStr)
        if(isSucceeded(response)){
            val mapping: JSONObject = getMapping(jsonObject, index, mtype)
            if(mapping == null) {
                throw new Exception("getSchema: communicate with es error1")
            }
            val nestSet = mutable.Set[String]()
            val prop = getProperity(index, mtype, mapping, "", nestSet)
            (prop, nestSet.toSet)
        }else {
            throw new Exception("getSchema: communicate with es error2")
        }

    }

    /**curl -i -XHEAD http://testserver1:9200/test_mb_100/_mapping/my_table **/
    def checkExist(index: String, mtype: String): Boolean = {
        val response: Response = restClient.performRequest("HEAD", s"""/$index/_mapping/$mtype""", new util.Hashtable[String, String]())
        if(isSucceeded(response)) {
            true
        }else{
            false
        }
    }

    //https://www.elastic.co/guide/en/elasticsearch/reference/5.3/docs-delete-by-query.html
    //https://stackoverflow.com/questions/34286357/delete-all-documents-of-a-type-in-elasticsearch-2-x
    /** delete content in index, but index exist. only use higher than 5.*,  in 2.*, need to install a delete by query plugin
      */
    def truncateIndex(index: String, mtype: String): Boolean = {
        val deleteJson = """{ "query": { "match_all": {} } }"""
        val entityReq: HttpEntity = new StringEntity(deleteJson, ContentType.APPLICATION_JSON)
        val response: Response = restClient.performRequest("POST", s"""/$index/$mtype/_delete_by_query""", new util.Hashtable[String, String](), entityReq)
        if (isSucceeded(response)) {
            true
        } else {
            false
        }
    }

    /** delete index in index, nothing left **/
    def deleteIndex(index: String): Boolean = {
        try {
            val response: Response = restClient.performRequest("DELETE", s"""/$index""", new util.Hashtable[String, String]())
            if(isSucceeded(response)) {
                true
            }else{
                false
            }
        }catch{
            case _: IOException => false   //no exist, in exception
        }
    }


    private def buildESType(dataType: DataType): String = {
        //in es5, index became a boolean property, that is why setting it to not_analyzed doesn't have any effect.
        //If you want to have a non analyzed field, you need to use "type": "keyword"
         val indexOption = if(getVersion().head >= 5){ "true" } else { "not_analyzed" }
         dataType match {
             case ByteType =>    s"""{"type": "byte", "index": "$indexOption"}"""
             //case StringType  => "text"  //keyword：存储数据时候，不会分词建立索引; text: 存储数据时候，会自动分词，并生成索引
             case BooleanType => s"""{"type": "boolean", "index": "$indexOption"}"""
             case DoubleType =>  s"""{"type": "double", "index": "$indexOption"}"""
             case BinaryType =>  s"""{"type": "binary", "index": "$indexOption"}"""
             case ShortType =>   s"""{"type": "short", "index": "$indexOption"}"""
             case FloatType =>   s"""{"type": "float", "index": "$indexOption"}"""
             case IntegerType => s"""{"type": "integer", "index": "$indexOption"}"""
             case LongType =>    s"""{"type": "long", "index": "$indexOption"}"""
             case StringType => if (getVersion().head >= 5) {
                     s"""{"type": "keyword", "index": "$indexOption"}"""
                 } else {
                     s"""{"type": "string", "index": "$indexOption"}"""
                 } //es 2.*版本里面是没有keyword text两个字段，只有string字段
             case DateType =>       s"""{"type": "date", "index": "$indexOption"}"""
             case TimestampType =>  s"""{"type": "date", "index": "$indexOption"}""" //long  <-- date
             case a: ArrayType =>   buildESType(a.elementType) //"array"
             case m: MapType =>  //TODO for maptype
                 s"""{"properties":{  }}""".stripMargin
             case s: StructType =>
                 val body = s.fields.map{field => s""" "${field.name}": ${buildESType(field.dataType)}""" }.mkString(",\n")
                 s"""{"properties": {$body}} """
         }
    }

    def generateUpdateMapping(index: String, mtype: String, schema: StructType) = {
        val propContent = schema.fields.map{ field =>
            s""" "${field.name}": ${buildESType(field.dataType)}"""
        }.mkString(",\n")

        s"""{
           |  "_all": { "enabled": true  },
           |  "properties":{
           |    $propContent
           |  }
           |}""".stripMargin
    }

    def generateCreateMapping(index: String, mtype: String, schema: StructType): String = {
        val propContent = schema.fields.map{ field =>
            s""" "${field.name}": ${buildESType(field.dataType)}"""
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

    //TODO: many other type
//    private def generateMapping(index: String, mtype: String, schema: StructType): String = {
//        val propContent = schema.fields.map{ field =>
//            val dType = buildESType(field.dataType)
//            s""" "${field.name}":{"type": "$dType", "index": "not_analyzed"}"""
//        }.mkString(",\n")
//
//        s"""{
//            |"mappings": {
//            |   "$mtype": {
//            |       "_all": { "enabled": true  },
//            |       "properties":{
//            |           $propContent
//            |       }
//            |    }
//            |  }
//            |}""".stripMargin
//    }

    /** PUT twitter/_mapping/user
      *  {
      *    "properties": {
      *      "name": {
      *        "type": "text"
      *      }
      *    }
      *  }
      * */
    def putSchema(index: String, mtype: String, schema: StructType): Boolean = {

        val mapping = generateCreateMapping(index, mtype, schema)
        System.out.println("putSchema" + mapping)

        val entityReq: HttpEntity = new StringEntity(mapping, ContentType.APPLICATION_JSON)
        val response: Response = restClient.performRequest("PUT", s"""/$index""", new util.Hashtable[String, String](), entityReq)
        if(isSucceeded(response)) {
            true
        }else{
            false
        }

    }
    //https://www.elastic.co/guide/en/elasticsearch/reference/5.3/indices-put-mapping.html
    /**PUT twitter/_mapping/user
    *   {
    *      "properties": {
    *        "name": {
    *          "type": "text"
    *        }
    *      }
    *   }
    **/

    def updateSchema(index: String, mtype: String, schema: StructType): Boolean = {
        val mapping = generateUpdateMapping(index, mtype, schema)
        val entityReq: HttpEntity = new StringEntity(mapping, ContentType.APPLICATION_JSON)
        val response: Response = restClient.performRequest("PUT", s"""/$index/_mapping/$mtype""", new util.Hashtable[String, String](), entityReq)
        if(isSucceeded(response)) {
            true
        }else{
            false
        }

    }

    //UPDATE xxx_tbl SET xxx_title="abcdefg" WHERE id=3;
    /** update one row in index, column (colName, colValue), colValue is "aaabbbccc" **/
    def update(index: String, mtype: String, id: String, columns: Seq[(String, String)]): Boolean = { //

        val updateBody = columns.map{case (key, value) =>
            s""""$key": $value"""
        }.mkString(",")

        val updateRequest = s"""{ $updateBody }"""
        val entityReq: HttpEntity = new StringEntity(updateRequest, ContentType.APPLICATION_JSON)
        val response: Response = restClient.performRequest("POST", s"/$index/$mtype/$id", new util.Hashtable[String, String](), entityReq)
        if(isSucceeded(response)) {
            true
        }else{
            false
        }
    }

    /** use logic from es connector, convert every type to json object **/
    private def buildJsonValue(dataType: DataType, value: Any): Any = {
        if(value == null){
            return JSONObject.NULL
        }
        dataType match {
            case a: ArrayType =>  buildArray(a, value)//java collection
            case m: MapType =>    buildMap(m, value)  //java map
            case s: StructType => buildStruct(s, value)
            case _: DecimalType => throw new Exception("Decimal types are not supported by Elasticsearch - consider using a different type (such as string)")
            case _: NumericType => value
            case _: TimestampType => value.asInstanceOf[Timestamp].getTime
            case _: DateType => value.asInstanceOf[Date].getTime
            case _: BooleanType => value.asInstanceOf[Boolean]
            case _: StringType => value.toString
            case _ => value
        }
    }

    private def buildMap(mapType: MapType, value: Any): util.Map[String, Any] = {
        value match{
            case sm: scala.collection.Map[_, _] => buildMap2(mapType, sm)
            case jm: java.util.Map[_, _] =>  buildMap2(mapType, jm.asScala)
            case _ => throw new Exception(s"buildMap $value $mapType")
        }
    }

    private def buildMap2(mapType: MapType, value: scala.collection.Map[_, _]): util.Map[String, Any] = {
        value.map{ v => (v._1.toString, buildJsonValue(mapType.valueType, v._2))}.toMap.asJava
    }

    private def buildArray(arrayType: ArrayType, value: Any): JSONArray = {
        value match{
            case a: Array[_] =>  buildSeq(arrayType.elementType, a)
            case s: Seq[_] =>    buildSeq(arrayType.elementType, s)
            case _ => throw new Exception(s"buildArray $value $arrayType")
        }
    }

    private def buildSeq(dataType: DataType, value: Seq[_]): JSONArray = {
        val jmap = value.map{ v=> buildJsonValue(dataType, v)}.asJava
        new JSONArray(jmap)
    }

    private def buildStruct(schema: StructType, batchData: Any): JSONObject = {
        val jSONObject: JSONObject = new JSONObject ()
        batchData match {
            case seq: Seq[_] =>
                schema.fields.zipWithIndex foreach {
                case (field, index) =>
                    val fieldName = field.name
                    val fieldValue = seq(index)
                    val fieldType = field.dataType

                    val jsonValue = buildJsonValue(fieldType, fieldValue)
                    jSONObject.put(fieldName, jsonValue)

            }
        }
        jSONObject
    }


    /** bulk put data to */
    def putBatchData(index: String, mtype: String, schema: StructType, batchData: Seq[Seq[Any]]): (Boolean, Long) = {
        val mappingIndex: Option[Int] = if(param.contains("es.mapping.id")) {  //field or property name containing the document id.
            Option(schema.fieldIndex(param("es.mapping.id")))
        }else { None }

        //build bulk req string
        val bulkRequest = batchData.map{ data =>
            val bulkHeader = if(mappingIndex.isDefined ) {
                s"""{"index": {"_index": "$index", "_type": "$mtype", "_id": "${data(mappingIndex.get)}"}}\n"""
            } else{
                s"""{"index": {"_index": "$index", "_type": "$mtype"}}\n"""
            }

            val bulkBody = buildStruct(schema, data).toString  //get string from JSON Object
            bulkHeader + s"$bulkBody\n"
        }.mkString("")

        //send bulk req
        val entityReq: HttpEntity = new StringEntity(bulkRequest, ContentType.APPLICATION_JSON)
        val response: Response = restClient.performRequest("PUT", s"""/_bulk""", new util.Hashtable[String, String](), entityReq)

        //read bulk response
        handleBulkResponse(response, batchData)
    }

    private def handleBulkResponse(response: Response, batchData: Seq[Seq[Any]]): (Boolean, Long) = {
        val content = getContent(response)
        val jsonObject = new JSONObject(content)
        val hasError = getFieldAsString(jsonObject, "errors")
        val indexResult = if(hasError == "false") { true }  else  { false }

        var succeedNum: Long = 0l
        val items: JSONArray = jsonObject.getJSONArray("items")
        for(item <- items.asScala){
            val bulkResult = item.asInstanceOf[JSONObject]
            if(bulkResult.has("index")){
                val index = bulkResult.getJSONObject("index")
                succeedNum += getFieldAsLong(index, "_shards/successful")
                if(getFieldAsLong(index, "_shards/failed") != 0) {
                    val seq_no = getFieldAsLong(index, "_seq_no")
                    throw new Exception(s"EsRestClient write failed for err=$seq_no, ret=$indexResult, suc=$succeedNum," +
                                        s"json=${batchData(seq_no.intValue())}")
                }
            }

        }
        (indexResult, succeedNum)
    }

    def getProperity(index: String, mtype: String, mapping: JSONObject, parent:String = "", nestField: mutable.Set[String] = mutable.Set.empty[String]): StructType = {
        val arrayInclude = param.get("es.read.field.as.array.include")
        val arraySet = if(arrayInclude.isDefined) {
            arrayInclude.get.split(",").map{_.stripSuffix(" ").stripPrefix(" ")}.toSet
        } else {
            Set.empty[String]
        }

        //es.mapping.date.rich
        val dateRich = param.get("es.mapping.date.rich")
        val dateTimeStamp = if(dateRich.isDefined) {
            dateRich.get == "true"
        }else {
            true //By default this is true
        }

        import org.apache.spark.sql.types._
        val properties = if(mapping.has("properties")){
            mapping.getJSONObject("properties")
        }else{
            mapping
        }
        var seq: Seq[StructField] = Seq.empty[StructField]
        import scala.collection.JavaConversions._

        for(prop <- properties.keySet if prop != "fielddata") {
            val rspType = properties.get(prop) match {
                case j:JSONObject => j.optString("type", "object")
                case s: String => s
            }
            if(rspType == "nested"){
                nestField.add(prop)  //save nest field, for get right result in json request
            }

            //IF a column is an array, it must be config to array. Nest could by detected from config
            val stype = if(arraySet.contains(prop)) { //array OR nest type only have one
                "array"
            }else{
                rspType
            }

            val dtype: DataType = stype match {
                case "text"    => StringType
                case "boolean" => BooleanType
                case "double"  => DoubleType
                case "binary"  => BinaryType
                case "short"   => ShortType
                case "float"   => FloatType
                case "integer" => IntegerType
                case "long"    => LongType
                case "keyword" => StringType
                case "date"    => if(dateTimeStamp) TimestampType else StringType       //date
                case "array"   => ArrayType(getProperity(index, mtype, properties.getJSONObject(prop), "", nestField)) // array
                case "nested"  =>
                    //nestField.add(prop)  //save nest field
                    getProperity(index, mtype, properties.getJSONObject(prop))     // nest
                case "object"  => getProperity(index, mtype, properties.getJSONObject(prop), prop, nestField)     // object
                case "geo_point" =>
                    val field = if(parent.isEmpty) prop else s"$parent.$prop"
                    val pointTypeOpt = sampleGeoField(index, mtype, field, "geo_point")
                    if(pointTypeOpt.isDefined){
                        val tye = pointTypeOpt.get
                        tye match {
                            case "LON_LAT_ARRAY"    => DataTypes.createArrayType(DoubleType)
                            case "GEOHASH"          => StringType
                            case "LAT_LON_STRING"   => StringType
                            case "LAT_LON_OBJECT"   =>
                                val lon = DataTypes.createStructField("lat", DoubleType, true)
                                val lat = DataTypes.createStructField("lon", DoubleType, true)
                                DataTypes.createStructType(Array(lon,lat))
                        }
                    }else{
                        throw new Exception("error to get geo point")
                    }
                    //"location" : { "lat" : 40.12, "lon" : -71.34 }
                case "geo_shape" =>
                    val field = if(parent.isEmpty) prop else s"$parent.$prop"
                    val shapeTypeOpt= sampleGeoField(index, mtype, field, "geo_shape")
                    if(shapeTypeOpt.isDefined){
                        val fields = new ArrayList[StructField]()
                        fields.add(DataTypes.createStructField("type", StringType, true))
                        val coordinate = "coordinates"
                        val shapeType = shapeTypeOpt.get
                        shapeType match {
                            case "POINT" =>  fields.add(DataTypes.createStructField(coordinate, DataTypes.createArrayType(DoubleType), true))
                            case "LINE_STRING" => fields.add(DataTypes.createStructField(coordinate, createNestedArray(DoubleType, 2), true))
                            case "POLYGON"  =>{
                                fields.add(DataTypes.createStructField(coordinate, createNestedArray(DoubleType, 3), true))
                                fields.add(DataTypes.createStructField("orientation", StringType, true))
                            }
                            case "MULTI_POINT"  =>fields.add(DataTypes.createStructField(coordinate, createNestedArray(DoubleType, 2), true))
                            case "MULTI_LINE_STRING"  => fields.add(DataTypes.createStructField(coordinate, createNestedArray(DoubleType, 3), true))
                            case "MULTI_POLYGON"  => fields.add(DataTypes.createStructField(coordinate, createNestedArray(DoubleType, 4), true))
                            case "GEOMETRY_COLLECTION"  => throw new Exception(s"Geoshape GEOMETRY_COLLECTION not supported")
                            case "ENVELOPE"  => fields.add(DataTypes.createStructField(coordinate, createNestedArray(DoubleType, 2), true))
                            case "CIRCLE"  => {
                                fields.add(DataTypes.createStructField(coordinate, DataTypes.createArrayType(DoubleType), true))
                                fields.add(DataTypes.createStructField("radius", StringType, true))
                            }
                        }  //[13.400544, 52.530286]
                        val geoShape = DataTypes.createStructType(fields)
                        geoShape
                    }
                    else{
                        throw new Exception("error to get geo shape")
                    }
                case _         => StringType

            }
            seq = seq :+ StructField(prop, dtype)
        }
        StructType(seq.toArray)
    }

    def createNestedArray(elementType: DataType, depth: Int): DataType = {
        var array = elementType
        for (_ <- 0 until depth) {
            array = DataTypes.createArrayType(array)
        }
        array
    }

    def sampleGeoField(index: String, mtype: String, field: String, geoType: String): Option[String] = {

        val data = sampleFieldData(index, mtype, field)  //query

        if(data.isDefined) {  //parse geo info
            val geoValue = data.get
            if (geoType == "geo_point") {
                geoValue match {
                    case l: java.util.List[_] =>
                        l.get(0) match{
                            case Double => Some("LON_LAT_ARRAY")
                            case _ => None
                        }
                    case s: String =>
                        if(s.contains(",")) {
                            Some("LAT_LON_STRING")
                        }else{
                            Some("GEOHASH")
                        }
                    case m: java.util.Map[_, _] => Some("LAT_LON_OBJECT")
                    case _ => None
                }
            }
            else if (geoType == "geo_shape") {
                geoValue match {
                    case m: java.util.Map[_, _] =>
                        val typ = m.get("type").toString
                        Some(typ.toUpperCase)
                    case _ => None
                }
            }else{
                None
            }
        }else{
            None
        }
    }


    def sampleFieldData(index: String, mtype: String, fields: String): Option[AnyRef] = {
        val request: String =
            s"""
              |{ "terminate_after":1, "size":1,
              |  "_source" : ["$fields"],
              |  "query": {
              |      "bool": {"must" : [
              |          {"exists": {"field": "$fields"} }
              |       ]}
              |   }
              |}
            """.stripMargin
        val entityReq: HttpEntity = new StringEntity(request, ContentType.APPLICATION_JSON)
        val response: Response = restClient.performRequest("GET", s"""/$index/$mtype/_search""", new util.Hashtable[String, String](), entityReq)
        val jsonStr = getContent(response)
        val jsonObject = new JSONObject(jsonStr)
        val map = scala.collection.mutable.Map.empty[String, AnyRef]

        if(isSucceeded(response)){
            val hits = getFieldAsArray(jsonObject, "hits/hits")
            val iter = hits.iterator

            if (iter.hasNext) {
                val hit = iter.next.asInstanceOf[JSONObject]

                if (hit.opt("_source") != null) { //for source,  select col from table
                    val data = hit.opt("_source")
                    val jsonObject = data.asInstanceOf[JSONObject]
                    getHitsValue(jsonObject, "", map)
                }

                if (hit.opt("fields") != null) { //for script_fields, select col as aaa from table
                    val data = hit.opt("fields")
                    val jsonObject = data.asInstanceOf[JSONObject]
                    getHitsValue(jsonObject, "", map)
                }
            }

        }
        val field: Seq[String] = fields.split('.').toSeq
        if(field.length == 1) {
            map.get(field.head)
        }else {
            var ret = map(field.head).asInstanceOf[java.util.Map[String, AnyRef]]
            for(i <- 1 until field.length) {
                ret = ret.get(field(i)).asInstanceOf[java.util.Map[String, AnyRef]]
            }
            Some(ret)
        }

    }

    def getMapping(result: JSONObject, index: String, mtype: String) = {
        if(result == null) null
        try {
            val a = result.getJSONObject(index).getJSONObject("mappings").getJSONObject(mtype)
            a
        }catch {
            case e: Exception => null
        }
    }


    def performScrollFirst(index: String, mtype: String, query: String, limit: Int): (ActionResponse, String, Long, Long, Boolean) = {
        val actionRsp: ActionResponse = new ActionResponse()

        val entityReq: HttpEntity = new StringEntity(query, ContentType.APPLICATION_JSON)   //default 2 min wait
        val response: Response = restClient.performRequest("POST", s"""/$index/$mtype/_search?scroll=2m""", new util.Hashtable[String, String](), entityReq)
        if(!isSucceeded(response)) {
            actionRsp.succeeded(false)
            throw new Exception("performScrollRequest to Server return ERROR " +response.getStatusLine.getStatusCode)
        }

        val content = getContent(response)
        val jsonObject = new JSONObject(content)
        val totalLines = getFieldAsLong(jsonObject, "hits/total")
        val scrollId :String = getFieldAsString(jsonObject, "_scroll_id")
        val fetchSize: Long = handleResponse(content, actionRsp)   //handle

        val finished = if(containsAggs(jsonObject)) {
            false //agg no scroll query
        }else {
            true
        }

        (actionRsp, scrollId, totalLines, fetchSize, finished)
    }

    def performScrollLast(scrollId: String) :(ActionResponse, String, Long) = {

        val actionRsp: ActionResponse = new ActionResponse()
        val leftQuery = s"""{"scroll":"2m","scroll_id":"$scrollId"}"""
        val leftScrollReq: HttpEntity = new StringEntity(leftQuery, ContentType.APPLICATION_JSON)

        val response: Response = restClient.performRequest("POST", s"""/_search/scroll""", new util.Hashtable[String, String](), leftScrollReq)

        if(!isSucceeded(response)) {
            actionRsp.succeeded(false)
            throw new Exception(s"performScrollLast to Server return ERROR ${response.getStatusLine.getStatusCode} line")
        }

        val content = getContent(response)
        val jsonObject = new JSONObject(content)
        val newScrollId = getFieldAsString(jsonObject, "_scroll_id")  //update scroll id
        val fetchSize = handleResponse(content, actionRsp)      //update fetch size

        (actionRsp, newScrollId, fetchSize)
    }

    def performScrollRequest(index: String, mtype: String, query: String, limit: Boolean=true) : ActionResponse = {
        val actionRsp: ActionResponse = new ActionResponse()

        //val entityReq: HttpEntity = new StringEntity(query, ContentType.APPLICATION_JSON)   //default 2 min wait
        //val response: Response = restClient.performRequest("POST", s"""/$index/$mtype/_search?scroll=2m""", new util.Hashtable[String, String](), entityReq)
        val response: Response = performRequestWithScroll(index, mtype, query, true)

        if(!isSucceeded(response)) {
            actionRsp.succeeded(false)
            throw new Exception("performScrollRequest to Server return ERROR " +response.getStatusLine.getStatusCode)
        }
        val jsonReqObject = new JSONObject(query)
        val requestLines = jsonReqObject.optLong("size", 10000l)  //default 10000

        val content = getContent(response)
        val jsonRspObject = new JSONObject(content)
        val responseLines = getFieldAsLong(jsonRspObject, "hits/total")

        val shouldProcessLines = if(limit) {
            math.min(requestLines, responseLines)
        }else{
            math.max(requestLines, responseLines)
        }
        //if has limit size, we should use the min size of send and receive size, if no limit size, we

        var scrollId = getFieldAsString(jsonRspObject, "_scroll_id")

        var proceedLines: Long = 0L
        var fetchSize: Long = handleResponse(content, actionRsp)   //handle
        proceedLines += fetchSize

        if(containsAggs(jsonRspObject)){
            actionRsp   //agg no scroll query
        }
        else {
            while (proceedLines < shouldProcessLines && proceedLines != 0 ) {
                val leftQuery = s"""{"scroll":"2m","scroll_id":"$scrollId"}"""
                //val leftScrollReq: HttpEntity = new StringEntity(leftQuery, ContentType.APPLICATION_JSON)
                //val response: Response = restClient.performRequest("POST", s"""/_search/scroll""", new util.Hashtable[String, String](), leftScrollReq)
                val response: Response = performRequestWithScroll(index, mtype, leftQuery, false)

                if(!isSucceeded(response)) {
                    actionRsp.succeeded(false)
                    throw new Exception(s"performScrollRequest to Server return ERROR ${response.getStatusLine.getStatusCode} line $proceedLines")
                }

                scrollId = getFieldAsString(jsonRspObject, "_scroll_id")  //update scroll id
                fetchSize = handleResponse(getContent(response), actionRsp)      //update fetch size
                proceedLines += fetchSize
            }
            actionRsp
        }
    }


    /*****************
     aggregation: {
        group1:{
            bucket1:[{
                key: xx
                group2:{
                    bucket2:[
                       { key: yy
                        name1: { value: $},
                        namex: { valuex: $}},
                       { key: yy,
                        name1: { value: $ },
                        namex: { valuex: $}}
                    ]
                }
            }
        }
     }
     ***************/

    def handleResponse(response: String, action: ActionResponse): Long = {
        import scala.collection.JavaConversions._

        var fetchSize: Long = 0
        val jsonObject = new JSONObject(response)

        val total = getFieldAsLong(jsonObject, "hits/total")
        action.totalHits(total)

        if(containsAggs(jsonObject)){
            var aggregationsMap:JSONObject =jsonObject.getJSONObject("aggregations")
            if(aggregationsMap == null) {
                aggregationsMap = jsonObject.getJSONObject("aggs")
            }

            val seq = scala.collection.mutable.ArrayBuffer.empty[Map[String, AnyRef]]
            val stack = scala.collection.mutable.Stack[(String, AnyRef)]()

            var aggIsSimple = true
            for(key <- aggregationsMap.keySet()){
                if(aggregationsMap.getJSONObject(key).has("buckets")){
                    aggIsSimple = false
                }
            }

            if(aggIsSimple) {  //no bucket in agg, no group by in agg, select in one line
                val map = scala.collection.mutable.Map.empty[String, AnyRef]
                for (key <- aggregationsMap.keySet()) {
                    val aggResult = aggregationsMap.getJSONObject(key)
                    val value: AnyRef = getAggValue(aggResult)
                    map +=(key -> value)
                }
                action.addAggregation( new AggWrapper(AggregationType.MULTI_BUCKETS, "", Map.empty[String, AnyRef] ++ map))

            }else {  // simple bucket or multi-bucket, receive get the content,
                for (key <- aggregationsMap.keySet()) {
                    getInColumn(key, aggregationsMap.getJSONObject(key), stack, seq)
                    fetchSize += seq.size
                    seq.foreach { elem =>
                        action.addAggregation(new AggWrapper(AggregationType.MULTI_BUCKETS, key, elem))
                    }
                }
            }
        }
        else {
            val hits = getFieldAsArray(jsonObject, "hits/hits")
            val iter = hits.iterator
            val map = scala.collection.mutable.Map.empty[String, AnyRef]

            while (iter.hasNext) {
                map.clear()
                val hit = iter.next.asInstanceOf[JSONObject]

                if (hit.opt("_source") != null) { //for source,  select col from table
                    val data = hit.opt("_source")
                    val jsonObject = data.asInstanceOf[JSONObject]
                    getHitsValue(jsonObject, "", map)
                }

                if (hit.opt("fields") != null) {  //for script_fields, select col as aaa from table
                    val data = hit.opt("fields")
                    val jsonObject = data.asInstanceOf[JSONObject]
                    getHitsValue(jsonObject, "", map)
                }

                if(map.nonEmpty) {  //es sort will return one empty line, do not add it
                    action.addHit(new HitWrapper(hit.getString("_index"),
                        hit.getString("_type"),
                        hit.getString("_id"),
                        "", //TODO: no use here
                        Map.empty[String, AnyRef] ++ map))
                }
                fetchSize += 1  //for not loop forever
            }
        }

        fetchSize
    }

    def performRequestWithScroll(index: String, mtype: String, query: String, isFirst: Boolean = true): Response = {
        val entityReq: HttpEntity = new StringEntity(query, ContentType.APPLICATION_JSON)
        val response: Response = if(isFirst) {
            restClient.performRequest("POST", s"""/$index/$mtype/_search?scroll=2m""", new util.Hashtable[String, String](), entityReq)
        }else{
            restClient.performRequest("POST", s"""/_search/scroll""", new util.Hashtable[String, String](), entityReq)
        }
        response
    }

    def performRequest(index: String, mtype: String, query: String): Response = {
        val entityReq: HttpEntity = new StringEntity(query, ContentType.APPLICATION_JSON)
        val response: Response = restClient.performRequest("POST", s"""/$index/$mtype/_search""", new util.Hashtable[String, String](), entityReq)
        response
    }

    def sendRequest(index: String, mtype: String, query: String): ActionResponse = {
        //val entityReq: HttpEntity = new StringEntity(query, ContentType.APPLICATION_JSON)
        //val response: Response = restClient.performRequest("POST", s"""/$index/$mtype/_search""", new util.Hashtable[String, String](), entityReq)
        val response = performRequest(index, mtype, query)

        val jsonStr = getContent(response)
        //val gson = new GsonBuilder().setPrettyPrinting().create
        //val jsonObject = gson.fromJson(jsonStr, classOf[JSONObject])
        val jsonObject = new JSONObject(jsonStr)

        val rsp: ActionResponse = new ActionResponse()
        if(isSucceeded(response)){
            import scala.collection.JavaConversions._
            val total = getFieldAsLong(jsonObject, "hits/total")
            rsp.totalHits(total)

            if(containsAggs(jsonObject)){
                var aggregationsMap:JSONObject =jsonObject.getJSONObject("aggregations")
                if(aggregationsMap == null) {
                    aggregationsMap = jsonObject.getJSONObject("aggs")
                }

                val seq = scala.collection.mutable.ArrayBuffer.empty[Map[String, AnyRef]]
                val stack = scala.collection.mutable.Stack[(String, AnyRef)]()

                var aggIsSimple = true
                for(key <- aggregationsMap.keySet()){
                    if(aggregationsMap.getJSONObject(key).has("buckets")){
                        aggIsSimple = false
                    }
                }

                if(aggIsSimple) {  //no bucket in agg, no group by in agg, select in one line
                    val map = scala.collection.mutable.Map.empty[String, AnyRef]
                    for (key <- aggregationsMap.keySet()) {
                        val aggResult = aggregationsMap.getJSONObject(key)
                        val value: AnyRef = getAggValue(aggResult)
                        map +=(key -> value)
                    }
                    rsp.addAggregation( new AggWrapper(AggregationType.MULTI_BUCKETS, "", Map.empty[String, AnyRef] ++ map))

                }else {  // simple bucket or multi-bucket, receive get the content,
                    for (key <- aggregationsMap.keySet()) {
                        getInColumn(key, aggregationsMap.getJSONObject(key), stack, seq)
                        seq.foreach { elem =>
                            rsp.addAggregation(new AggWrapper(AggregationType.MULTI_BUCKETS, key, elem))
                        }
                    }
                }

            }
            else {

                val hits = getFieldAsArray(jsonObject, "hits/hits")
                val iter = hits.iterator
                val map = scala.collection.mutable.Map.empty[String, AnyRef]

                while (iter.hasNext) {
                    val hit = iter.next.asInstanceOf[JSONObject]

                    if (hit.opt("_source") != null) { //for source,  select col from table
                        val data = hit.opt("_source")
                        val jsonObject = data.asInstanceOf[JSONObject]
                        getHitsValue(jsonObject, "", map)
                    }

                    if (hit.opt("fields") != null) {  //for script_fields, select col as aaa from table
                        val data = hit.opt("fields")
                        val jsonObject = data.asInstanceOf[JSONObject]
                        getHitsValue(jsonObject, "", map)
                    }

                    rsp.addHit(
                        new HitWrapper(hit.getString("_index"),
                                hit.getString("_type"),
                                hit.getString("_id"),
                                "",  //TODO: no use here
                                Map.empty[String, AnyRef] ++ map))
                }
            }
        }
        else {
            if(response.getStatusLine.getStatusCode == 404) {
                new ActionResponse().succeeded(false)
            }
            else {
                throw new Exception(s"${jsonObject.get("error").toString}")
            }
        }
        rsp
    }

    def close(): Unit = {
        if(restClient != null) {
            restClient.close()
        }
    }

    def getAggValue(result: JSONObject): AnyRef = {
        if(result.has("value")){
            result.get("value")
        }else {
          null
        }
    }

    def getHitsValue(input: AnyRef, pkey: String, map: scala.collection.mutable.Map[String, AnyRef]): AnyRef = {
        import scala.collection.JavaConversions._
        val localMap = new java.util.HashMap[String, AnyRef]()
        input match {
            case jsonObject: JSONObject =>
                for(key <- jsonObject.keySet()) {
                    val value = jsonObject.get(key)
                    value match {
                        case j: JSONObject =>
                            val retMap = getHitsValue(j, key, map)
                            localMap.put(key, retMap)
                        case a: JSONArray =>  //TODO: [ -73.983, 40.719 ] or "user" -> [{"last":"Smith","first":"John"},{"last":"White","first":"Alice"}]
                            val iter: Iterable[AnyRef] = a.map{elem => getHitsValue(elem, key, map)}
                            val localArray = new java.util.ArrayList[AnyRef]()
                            localArray ++= iter
                            localMap.put(key, localArray)
                        case _  =>
                            localMap.put(key, value)
                    }
                }
                if(pkey.isEmpty) {  //only highest outer
                    map ++= localMap
                }
                localMap
            case e: AnyRef => e
        }
    }

/************************
    sql: select avg(col_int_a), col_int_f, col_int_a  from test_mb_100 group by col_int_f, col_int_a
    {
        "aggregations": {
            "col_int_f": {
                "doc_count_error_upper_bound": 0,
                "sum_other_doc_count": 0,
                "buckets": [{
                    "key": 4,
                    "doc_count": 15,
                    "col_int_a": {
                        "doc_count_error_upper_bound": 0,
                        "sum_other_doc_count": 0,
                        "buckets": [{
                                "key": 50,
                                "doc_count": 1,
                                "avg(col_int_a)": {
                                    "value": 50
                                }
                            },
                            {
                                "key": 134,
                                "doc_count": 1,
                                "avg(col_int_a)": {
                                    "value": 134
                                }
                            }]
                    }
                }]
            }
         }
      }
************************/


    def getInBucket(highLevelKey: String, bucketObject: JSONObject, stack: scala.collection.mutable.Stack[(String, AnyRef)], seq: mutable.ArrayBuffer[Map[String, AnyRef]]): Unit = {
        import scala.collection.JavaConversions._
        //iterate all value, except doc count,
        //  if columnObject has bucket, do
        //    getInColumn(columnName, columnObject)
        //    stack .pop()
        //  else if columnObject has no bucket, do
        //    save (columnName, value) and stack value

        var shouldGetValue: Boolean = false
        val value: AnyRef = bucketObject.get("key")
        stack.push((highLevelKey, value))

        for (name <- bucketObject.keySet()) {
            //TODO: it may be has other exclude element name
            if (name != "key" && name != "doc_count" && name != "key_as_string") {
                val columnObject = bucketObject.getJSONObject(name)
                if (! columnObject.has("buckets")) { // no bucket, sould get value at once
                    shouldGetValue = true
                }
            }
        }

        if (shouldGetValue) {
            val map: Map[String, AnyRef] = bucketObject.keySet().filter(elem => elem != "key" && elem != "doc_count" && elem != "key_as_string").map { elem =>
                (elem, bucketObject.getJSONObject(elem).get("value"))
            }.toMap ++ stack.toMap
            seq.add(map)

        }else{  //child have buckets

            bucketObject.keySet().filter(elem => elem != "key" && elem != "doc_count" && elem != "key_as_string").foreach{ elem =>
                val columnObject = bucketObject.getJSONObject(elem)
                getInColumn(elem, columnObject, stack, seq)

            }
        }
        stack.pop //
    }

    def getInColumn(columnName: String, columnObject: JSONObject, stack: scala.collection.mutable.Stack[(String, AnyRef)], seq: mutable.ArrayBuffer[Map[String, AnyRef]]): Unit = {
        //if has "buckets", do
        //    get "buckets" value, iterate it, for every
        //    getBucket(key, bucketObject)
        if(columnObject.has("buckets")){
            val iter = columnObject.getJSONArray("buckets").iterator()
            while(iter.hasNext){
                val obj = iter.next().asInstanceOf[JSONObject]
                getInBucket(columnName, obj, stack, seq)
            }
        }
    }


    def isSucceeded(response: Response): Boolean = {
        response.getStatusLine.getStatusCode >= 200 && response.getStatusLine.getStatusCode < 300
    }

    def containsTerminal(result: JSONObject): Boolean = {
        if(result != null && result.has("terminated_early")){
            val terminal = result.opt("terminated_early").asInstanceOf[Boolean]
            if(terminal) {
                return true
            }
        }
        false
    }

    def containsAggs(result: JSONObject): Boolean = {
        result != null && (result.has("aggregations") || result.has("aggregation") || result.has("aggs") || result.has("agg"))
    }

    def getParentField(parent: JSONObject, fields: Seq[String]): JSONObject = {
        var obj = parent
        var i = 0
        fields.foreach { field =>
            if( i + 1 < fields.length) {
                obj = obj.getJSONObject(field)
                i = i + 1
            }
        }
        obj
    }

    private def getFieldAsArray(obj: JSONObject, field: String) = {
        val fields = field.split("/")
        val parent = getParentField(obj, fields)
        parent.getJSONArray(fields(fields.length - 1))
    }

    def getFieldAsString(jsonObject: JSONObject, field: String) = {
        jsonObject.get(field).toString
    }

    def getFieldAsLong(jsonObject: JSONObject, field: String) = {
        val fields = field.split("/")
        val obj = getParentField(jsonObject, fields)
        obj.getLong(fields(fields.length - 1))
    }

}


object EsRestClient{

}
