/*-
 * <<
 * Moonbox
 * ==
 * Copyright (C) 2016 - 2019 EDP
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

package moonbox.core

import moonbox.common.MbConf
import moonbox.common.util.Utils
import org.apache.spark.sql.SparkEngine
import org.scalatest.{BeforeAndAfterAll, FunSuite}

class MoonboxSessionSuite extends FunSuite with BeforeAndAfterAll {
	test("mbSession") {
		val session: MoonboxSession = new MoonboxSession(new MbConf(), "common", "sally")
	}

	override protected def beforeAll(): Unit = {
		val userDir = System.getProperty("user.dir")
		Utils.setEnv("MOONBOX_HOME", userDir.substring(0, userDir.lastIndexOf('/')))
		SparkEngine.start(new MbConf().set("spark.master", "local").set("spark.app.name", "test"))
	}
}