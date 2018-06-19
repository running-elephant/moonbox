package moonbox.catalyst.adapter.mongo

import moonbox.catalyst.core.CatalystContext
import moonbox.catalyst.core.plan.TableScanExec
import org.apache.spark.sql.catalyst.InternalRow
import org.apache.spark.sql.catalyst.expressions.Attribute

class MongoTableScanExec(output: Seq[Attribute],
                         rows: Seq[InternalRow]) extends TableScanExec(output, rows) with MongoTranslateSupport {
  override def translate(context: CatalystContext) = {
    Nil
  }
}
