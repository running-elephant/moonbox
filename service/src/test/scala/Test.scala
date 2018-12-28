import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan
import org.apache.spark.sql.catalyst.rules.Rule
import org.apache.spark.sql.types.{StringType, StructField, StructType}

object Test {
	def main(args: Array[String]) {
		import org.apache.spark.sql.catalyst.TableIdentifier
		import org.apache.spark.sql.catalyst.analysis.{Analyzer, FunctionRegistry}
		import org.apache.spark.sql.catalyst.catalog._
		import org.apache.spark.sql.catalyst.optimizer.Optimizer
		import org.apache.spark.sql.catalyst.parser.CatalystSqlParser
		import org.apache.spark.sql.internal.SQLConf

		val sqlConf = new SQLConf()
		val parser = new CatalystSqlParser(sqlConf)
		val sessionCatalog = new SessionCatalog(new InMemoryCatalog(), FunctionRegistry.builtin, sqlConf)
		val analyzer = new Analyzer(sessionCatalog, sqlConf, 100) {
			override val postHocResolutionRules: Seq[Rule[LogicalPlan]] = {
				Seq(
					new Rule[LogicalPlan]() {
						override def apply(plan: LogicalPlan): LogicalPlan = {
							throw new Exception("columns")
						}
					}
				)
			}
		}
		val optimizer = new Optimizer(sessionCatalog, sqlConf) {}

		sessionCatalog.createDatabase(new CatalogDatabase(
			"default",
			"",
			sessionCatalog.getDefaultDBPath("default"),
			Map()
		), ignoreIfExists = true)

		val parameters = Map[String, String](
			"url" -> "jdbc:mysql://10.143.131.38:3306/test",
			"dbtable" -> "book_list",
			"user" -> "root",
			"password" -> "123456",
			"driver" -> "com.mysql.jdbc.Driver"
		)

		// val schema = JDBCRelation(null, new JDBCOptions(parameters))(null).schema

		sessionCatalog.createTable(new CatalogTable(
			identifier = TableIdentifier("test", Some("default")),
			tableType = CatalogTableType.EXTERNAL,
			storage = CatalogStorageFormat.empty,
			schema = StructType.apply(Array(StructField("name", StringType))),
			provider = None
		), ignoreIfExists = true)


		val parsedPlan = parser.parsePlan("select default.test.name from test")
		val analyzedPlan = analyzer.execute(parsedPlan)
		println()
	}
}
