package moonbox.core.cache

object RedisCacheFactory extends CacheFactory {
	override def create(conf: CacheConfig): Cache = {
		new RedisCache(conf.servers)
	}
}
