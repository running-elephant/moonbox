package moonbox.catalyst.adapter.jdbc

import moonbox.catalyst.core.{Schema, SchemaFactory}

class JdbcSchemaFactory extends SchemaFactory {
	override def create(props: Map[String, String]): Schema = new JdbcSchema(props)
}
