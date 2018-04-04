package moonbox.catalyst.adapter.elasticsearch5.jdbc

import java.sql.{Connection, DriverManager}
import java.util.Properties

import moonbox.catalyst.jdbc.CatalystDriver

class EsCatalystDriver extends CatalystDriver{

    val CONNECT_STRING_PREFIX = "jdbc:es:"
    override def connect(url: String, info: Properties): Connection = {
        new EsCatalystConnection(url, info)
    }

    //DriverManager - Connection getConnection(String url, java.util.Properties info)
    override def acceptsURL(url: String): Boolean = {
        if(url.startsWith(CONNECT_STRING_PREFIX)) {
            true
        }
        else false
    }

}

object EsCatalystDriver {

    //val driver = new EsCatalystDriver
    //DriverManager.registerDriver(driver)
    println("----")

}
