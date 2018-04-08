package moonbox.catalyst.adapter.elasticsearch5.rdd

import moonbox.catalyst.adapter.elasticsearch5.client.EsRestClient

class MbEsv5DataSys(props: Map[String, String]) {

    def connection: () => EsRestClient = {
        ((props: Map[String, String]) => {
            () => {
                //System.setProperty("es.set.netty.runtime.available.processors", "false")
                new EsRestClient(props)
            }
        })(props)
    }
}
