package moonbox.application.interactive.spark

import com.fasterxml.jackson.databind.ObjectMapper



case class Prop(prop: String, value: String)

case class TableColumn(col: String, `type`: String, nullable: Boolean)

case class DagNode(id: Int,
                   database: String,
                   tableId: Option[Int] = None,
                   table: String,
                   `type`: String,
                   col: Option[String] = None,
                   desc: String,
                   cols: Option[Seq[TableColumn]] = None,
                   props: Seq[Prop] = Nil)

case class SourceNode(id: Int)

case class TargetNode(id: Int)

case class DagEdge(source: SourceNode, target: TargetNode, props: Seq[Prop] = Nil)

case class DagEntity(nodes: Seq[DagNode], edges: Seq[DagEdge], props: Seq[Prop] = Nil) {
  def json: String = {
    new ObjectMapper().writeValueAsString(this)
  }
}

case class SqlDag(dag_table: DagEntity, dag_col: DagEntity)

