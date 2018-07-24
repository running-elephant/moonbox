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

package moonbox.common

import org.scalatest.FunSuite

class MbConfSuite extends FunSuite {
	test("moonbox conf") {
		val conf: MbConf = new MbConf()
		conf.set("string", "string")
		    .set("string.string", "string.string")
		    .set("boolean", true)
		    .set("int", 10)
		    .set("double", 3.5d)
		    .set("float", 3.14f)

		assert(conf.get("string").contains("string"))
		assert(conf.get("string.string").contains("string.string"))
		assert(conf.get("boolean").contains("true"))
		assert(conf.get("boolean", false))
		assert(conf.get("int").contains("10"))
		assert(conf.get("int", 11) == 10)
		assert(conf.get("double").contains("3.5"))
		assert(conf.get("double", 3.6d) == 3.5d)
		assert(conf.get("float").contains("3.14"))
		assert(conf.get("float", 3.15f) == 3.14f)
		assert(conf.get("x.x.x", 100) == 100)
	}
}
