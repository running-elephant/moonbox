package moonbox.catalyst.adapter.elasticsearch5.client

import org.apache.spark.sql.types.DataType

import scala.collection.mutable

class ActionRequest {

    var hasLimit: Boolean = false
    var element: scala.collection.mutable.ArrayBuffer[String] = scala.collection.mutable.ArrayBuffer.empty[String]
    //val projectSchemaMap: mutable.LinkedHashMap[String, DataType] = mutable.LinkedHashMap.empty[String, DataType]
    val aggSchemaMap: mutable.LinkedHashMap[String, DataType] = mutable.LinkedHashMap.empty[String, DataType]

    def buildRequest(): String = {
        if(!hasLimit) {  //if no limit, add default size here
            element.insert(0, """ "from":0, "size":200 """)
        }
        "{ " + element.mkString(",") + " }"
    }

}
