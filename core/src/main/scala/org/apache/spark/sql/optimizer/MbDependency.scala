package org.apache.spark.sql.optimizer

abstract class MbDependency extends Serializable {
	def node: MbTreeNode
}

case class SameDependency(node: MbTreeNode) extends MbDependency

case class CrossDependency(node: MbTreeNode) extends MbDependency