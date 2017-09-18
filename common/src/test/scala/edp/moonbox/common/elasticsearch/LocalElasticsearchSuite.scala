package edp.moonbox.common.elasticsearch

import org.scalatest.FunSuite

class LocalElasticsearchSuite extends FunSuite {

	test("Local Elasticsearch Server") {
		val esServer = new LocalElasticsearch("es-test", "/tmp", "/tmp", "9200", "9300", false)
		esServer.start()
		assert(esServer.isRunning)
		esServer.stop()
		assert(esServer.isStop)
	}

}
