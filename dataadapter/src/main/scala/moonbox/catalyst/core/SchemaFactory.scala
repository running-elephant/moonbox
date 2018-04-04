package moonbox.catalyst.core

trait SchemaFactory {
	def create(props: Map[String, String]): Schema
}
