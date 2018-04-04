package moonbox.catalyst.adapter.elasticsearch5.plan

import moonbox.catalyst.core.CatalystContext
import moonbox.catalyst.core.plan.{RowDataSourceScanExec}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.catalyst.expressions.Attribute
import org.apache.spark.sql.catalyst.plans.physical.Partitioning
import org.apache.spark.sql.catalyst.{InternalRow, TableIdentifier}
import org.apache.spark.sql.sources.BaseRelation

import scala.collection.mutable

class EsRowDataSourceScanExec(output: Seq[Attribute],
                              rdd: RDD[InternalRow],
                              relation: BaseRelation,
                              outputPartitioning: Partitioning,
                              metadata: Map[String, String],
                              metastoreTableIdentifier: Option[TableIdentifier])  extends RowDataSourceScanExec(output,
                                                                                            rdd,
                                                                                            relation,
                                                                                            outputPartitioning,
                                                                                            metadata,
                                                                                            metastoreTableIdentifier ){
    override def translate(context: CatalystContext): Seq[String] = {
        Seq.empty[String]
    }

}


object EsRowDataSourceScanExec{
    def apply(output: Seq[Attribute],
              rdd: RDD[InternalRow],
              relation: BaseRelation,
              outputPartitioning: Partitioning,
              metadata: Map[String, String],
              metastoreTableIdentifier: Option[TableIdentifier]): EsRowDataSourceScanExec = {
        new EsRowDataSourceScanExec(output, rdd, relation, outputPartitioning, metadata, metastoreTableIdentifier)
    }

}