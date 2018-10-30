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

package moonbox.grid.timer

import moonbox.grid.api.JobSubmit
import moonbox.grid.deploy2.node.Moonbox

case class EventCall(definer: String, sqls: Seq[String], config: String) extends Function0[Unit] {
	override def apply(): Unit = {
		Moonbox.master ! JobSubmit(definer, sqls, config, async = true)
	}
}
