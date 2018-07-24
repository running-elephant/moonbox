/*-
 * <<
 * Moonbox
 * ==
 * Copyright (C) 2016 - 2018 EDP
 * ==
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * >>
 */

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
