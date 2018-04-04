package moonbox.catalyst.core.plan

import org.apache.spark.sql.catalyst.InternalRow
import org.apache.spark.sql.catalyst.expressions.Attribute

abstract case class TableScanExec(output: Seq[Attribute],
								  rows: Seq[InternalRow]) extends LeafExecNode {

}
