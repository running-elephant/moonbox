package moonbox.catalyst.core.plan

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.catalyst.{InternalRow, TableIdentifier}
import org.apache.spark.sql.catalyst.expressions.Attribute
import org.apache.spark.sql.catalyst.plans.physical.Partitioning
import org.apache.spark.sql.sources.BaseRelation

abstract case class RowDataSourceScanExec(output: Seq[Attribute],
                                          rdd: RDD[InternalRow],
                                          relation: BaseRelation,
                                          outputPartitioning: Partitioning,
                                          metadata: Map[String, String],
                                          metastoreTableIdentifier: Option[TableIdentifier]) extends LeafExecNode{

}
