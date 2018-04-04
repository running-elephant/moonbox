package moonbox.catalyst.core

import java.util.Properties

import moonbox.catalyst.adapter.jdbc.JdbcRow
import org.apache.spark.sql.UDFRegistration
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan
import org.apache.spark.sql.types.StructType

trait CatalystQueryExecutor {

  val planner = new CatalystPlanner(getPlannerRule)

  val provider: String

  def getPlannerRule(): Seq[Strategy]

  def execute4Jdbc(plan: LogicalPlan): (Iterator[JdbcRow], Map[Int, Int], Map[String, Int])

  def getTableSchema: StructType

  def execute[T](plan: LogicalPlan, convert: (Option[StructType], Seq[Any]) => T): Iterator[T]

  def adaptorFunctionRegister(udf: UDFRegistration)
}
