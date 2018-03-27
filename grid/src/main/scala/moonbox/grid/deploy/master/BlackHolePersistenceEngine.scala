package moonbox.grid.deploy.master

import scala.reflect.ClassTag

class BlackHolePersistenceEngine extends PersistenceEngine {
	override def persist(name: String, obj: Object): Unit = {}

	override def unpersist(name: String): Unit = {}

	override def read[T: ClassTag](prefix: String): Seq[T] = Nil
}
