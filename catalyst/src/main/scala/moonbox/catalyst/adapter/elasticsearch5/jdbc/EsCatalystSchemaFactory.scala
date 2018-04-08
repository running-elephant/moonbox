package moonbox.catalyst.adapter.elasticsearch5.jdbc

import moonbox.catalyst.adapter.jdbc.JdbcSchemaFactory

class EsCatalystSchemaFactory extends JdbcSchemaFactory{
    override def create(props :Map[String, String]) = {
        new EsCatalystSchema(props)
    }
}


object EsCatalystSchemaFactory {
    def main(args :Array[String]) :Unit = {
        val c = Class.forName("moonbox.catalyst.adapter.elasticsearch5.jdbc.EsCatalystSchemaFactory")
        val o :Any = c.newInstance()
    }
}