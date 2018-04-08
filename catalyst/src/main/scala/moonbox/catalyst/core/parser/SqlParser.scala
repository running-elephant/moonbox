package moonbox.catalyst.core.parser

import java.util.Properties

import moonbox.catalyst.core.parser.udf.udfParser.ParserUtil
import moonbox.catalyst.core.{CatalystAnalyzer, CatalystOptimizer}
import org.apache.spark.sql.UDFRegistration
import org.apache.spark.sql.catalyst.TableIdentifier
import org.apache.spark.sql.catalyst.analysis.FunctionRegistry
import org.apache.spark.sql.catalyst.catalog._
import org.apache.spark.sql.catalyst.parser.CatalystSqlParser
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan
import org.apache.spark.sql.internal.SQLConf
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.udf.UdfUtil

class SqlParser {
  var registeredTables: Seq[String] = Seq.empty[String]
  val conf = new SQLConf().copy(SQLConf.CASE_SENSITIVE -> true)
  val catalog = new SessionCatalog(new InMemoryCatalog, FunctionRegistry.builtin, conf)
  val parser = new CatalystSqlParser(conf)
  val analyzer = new CatalystAnalyzer(catalog, conf, 10)
  val optimizer = new CatalystOptimizer(catalog, conf)

  catalog.createDatabase(
    CatalogDatabase(
      "default", "",
      catalog.getDefaultDBPath("default"),
      Map()),
    ignoreIfExists = true
  )

  def getRegister: UDFRegistration = {
    val register = UdfUtil.selfFunctionRegister() //call common udf
    UdfUtil.buildUdfRegister(register)
  }

  def registerTable(tableName: String, schema: StructType, provider: String): Unit = {
    val catalogTable = CatalogTable(
      identifier = TableIdentifier(tableName, Some("default")),
      tableType = CatalogTableType.MANAGED,
      storage = CatalogStorageFormat.empty,
      schema = schema,
      provider = Some(provider)
    )
    catalog.createTable(catalogTable, ignoreIfExists = false)
  }

  def parse(sql: String): LogicalPlan = {
    val transformedSql = ParserUtil.sqlTransform(sql)
    optimizer.execute(analyzer.execute(parser.parsePlan(transformedSql)))
  }
}

