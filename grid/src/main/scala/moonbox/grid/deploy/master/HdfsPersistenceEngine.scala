package moonbox.grid.deploy.master

import java.net.URI

import moonbox.common.MbConf
import moonbox.grid.config._
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.FileSystem

import scala.reflect.ClassTag

class HdfsPersistenceEngine(conf: MbConf) extends PersistenceEngine {
	private val WORKING_DIR = conf.get("moonbox.persist.hdfs.dir", "/grid")
	private val hdfs = FileSystem.get(new URI(conf.get(PERSIST_SERVERS)), new Configuration())

	override def persist(name: String, obj: Object): Unit = ???

	override def unpersist(name: String): Unit = ???

	override def read[T: ClassTag](prefix: String): Seq[T] = ???

	override def exist(path: String) = ???
}
