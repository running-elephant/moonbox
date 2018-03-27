package moonbox.core.cache

trait Cache {

	def put[K, E](key: K, value: TraversableOnce[E]): Unit

	def bulkPut[K, E, C <: TraversableOnce[E]](key: K, iter: TraversableOnce[C]): Unit

	def get[K, E, C <: TraversableOnce[E]](key: K, start: Long, end: Long): TraversableOnce[C]

	def getAsIterator[K, E, C <: TraversableOnce[E]](key: K, fetchSize: Int, total: Long = Long.MaxValue): Iterator[C]

	def size[K](key: K): Long

}
