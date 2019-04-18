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

package moonbox.catalyst.adapter.mongo

import moonbox.catalyst.core.CatalystContext
import moonbox.catalyst.core.plan.TableScanExec
import org.apache.spark.sql.catalyst.InternalRow
import org.apache.spark.sql.catalyst.expressions.Attribute

class MongoTableScanExec(output: Seq[Attribute],
                         rows: Seq[InternalRow]) extends TableScanExec(output, rows) with MongoTranslateSupport {
  override def translate(context: CatalystContext) = {
    if (rows == null) {
      Nil
    } else {
      /* This code is used for 'limit 0' and 'where 1=0' (OptimizedPlan is LocalRelation(_, Nil)) */
      "{$match: {$and: [{\"_id\": {$eq: null}}, {\"_id\": {$ne: null}}]}}" :: Nil
      // TODO: to handle LocalRelation with non-Nil data
    }
  }
}
