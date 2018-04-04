package moonbox.catalyst.adapter.elasticsearch5.client

import com.google.gson.{JsonObject, JsonParser}

class HitWrapper(index: String, mtype: String, id: String, source: String, map: Map[String, AnyRef]) {
    private val parser = new JsonParser

    def this(source: String) {
        this(null, null, null, source, Map.empty[String, AnyRef])
    }

    def getSourceAsString: String = source

    def getSourceAsJsonObject: JsonObject = {
        val element = parser.parse(source)
        element.getAsJsonObject
    }

    def getIndex: String = index

    def getType: String = mtype

    def getId: String = id

    def getMap: Map[String, AnyRef] = map

}
