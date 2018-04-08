package moonbox.catalyst.adapter.elasticsearch5.plan

import moonbox.catalyst.core.CatalystContext
import moonbox.catalyst.core.plan.TableScanExec
import org.apache.spark.sql.catalyst.InternalRow
import org.apache.spark.sql.catalyst.expressions.Attribute

import scala.collection.mutable

class EsTableScanExec(output: Seq[Attribute],
								  rows: Seq[InternalRow]) extends TableScanExec(output, rows) {

    override def translate(context: CatalystContext): Seq[String] = {
        Seq.empty[String]
    }

    override def toString(): String = {
        super.toString()
    }
}

object EsTableScanExec{
    def apply(output: Seq[Attribute],
              rows: Seq[InternalRow]): EsTableScanExec = new EsTableScanExec(output, rows)
}
