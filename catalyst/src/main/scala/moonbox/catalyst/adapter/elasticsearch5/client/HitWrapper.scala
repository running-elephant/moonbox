package moonbox.catalyst.adapter.elasticsearch5.client

class HitWrapper(index: String, mtype: String, id: String, source: String, map: Map[String, AnyRef]) {

    def this(source: String) {
        this(null, null, null, source, Map.empty[String, AnyRef])
    }

    def getSourceAsString: String = source

    def getIndex: String = index

    def getType: String = mtype

    def getId: String = id

    def getMap: Map[String, AnyRef] = map

}
