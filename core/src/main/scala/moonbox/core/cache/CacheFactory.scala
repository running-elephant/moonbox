package moonbox.core.cache

trait CacheFactory {

	def create(conf: CacheConfig): Cache
}
