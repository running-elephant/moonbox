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

package moonbox.catalyst.adapter.elasticsearch5.plan

import moonbox.catalyst.core.CatalystContext
import moonbox.catalyst.core.plan.TableScanExec
import org.apache.spark.sql.catalyst.InternalRow
import org.apache.spark.sql.catalyst.expressions.Attribute

import scala.collection.mutable

class EsTableScanExec(output: Seq[Attribute],
								  rows: Seq[InternalRow]) extends TableScanExec(output, rows) {

    override def translate(context: CatalystContext): Seq[String] = {
        if(rows != null && rows.isEmpty) {
            context.limitSize = 0
            Seq(s""" "from": 0, "size": 0 """)  // where 1=0 after sql parser, there is only LocalRelation, we use limit 0 simulate this case
        }else {
            Seq.empty[String]
        }
    }

    override def toString(): String = {
        super.toString()
    }
}

object EsTableScanExec{
    def apply(output: Seq[Attribute],
              rows: Seq[InternalRow]): EsTableScanExec = new EsTableScanExec(output, rows)
}
