package moonbox.catalyst.adapter.elasticsearch5.client

import moonbox.catalyst.adapter.elasticsearch5.client.AggWrapper.AggregationType.AggregationType


class AggWrapper(mtype: AggregationType, result: String, map: Map[String, AnyRef]= Map.empty[String, AnyRef]) {

    def getType: AggregationType = mtype
    def getResult: String = result

    def getMap: Map[String, AnyRef] = map

}

object AggWrapper {
    /** Type of an aggregation (to know if there are buckets or not) */
    object AggregationType extends Enumeration {
        type AggregationType = Value
        val SIMPLE, MULTI_BUCKETS = Value
    }
}