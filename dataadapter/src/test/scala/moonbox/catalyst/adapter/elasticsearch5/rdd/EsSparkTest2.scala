package org.apache.spark.sql

import moonbox.catalyst.core.parser.udf.{ArrayExists, ArrayFilter, ArrayMap}
import org.apache.spark.sql.catalyst.analysis.FunctionRegistry.FunctionBuilder
import org.apache.spark.sql.catalyst.analysis.{FunctionRegistry, SimpleFunctionRegistry}
import org.apache.spark.sql.catalyst.expressions.{Expression, ExpressionDescription, ExpressionInfo, RuntimeReplaceable}

import scala.reflect.ClassTag
import scala.util.{Failure, Success, Try}

object EsSparkTest2 {

    def selfFunctionRegister(): FunctionRegistry = {
        val registry: SimpleFunctionRegistry = FunctionRegistry.builtin
        val expressions: Map[String, (ExpressionInfo, FunctionBuilder)] = Map(
            expression[ArrayExists]("array_exists"),
            //expression[ArrayExists2]("array_exists2"),
            expression[ArrayMap]("array_map"),
            expression[ArrayFilter]("array_filter")
        )
        expressions.foreach { case (name, (info, builder)) => registry.registerFunction(name, info, builder) }

        registry
    }

    def main(args: Array[String]): Unit = {
        selfFunctionRegister()
        val spark: SparkSession = SparkSession.builder().master("local[*]").appName("estest").getOrCreate()

        val df = spark.read.format("org.elasticsearch.spark.sql")
                .option("es.resource", "nest_test_table")
                .option("es.read.field.as.array.include", "user")
                .option("es.nodes", "testserver1:9200").load()
        df.createOrReplaceTempView("nest_table")
        df.printSchema()

        val strings = spark.sessionState.functionRegistry.listFunction()
        //spark.sql("""select * from nest_table where array_contains1(user.age, "x > 5")""").show()
//        val parsed = spark.sessionState.sqlParser.parsePlan("""select array_map(user.score, "x + 100"), user.age from nest_table""")
//        val analyzed = spark.sessionState.analyzer.execute(parsed)
//        val optimized = spark.sessionState.optimizer.execute(analyzed)
//        val plan = spark.sessionState.planner.plan(optimized)

        //spark.sql("""select * from nest_table where array_exists(user.first, "x='John'")""").show(false)
        //spark.sql("""select * from nest_table where array_exists(user.last, "x='White'")""").show(false)
        //spark.sql("""select * from nest_table where array_exists(user.age, "x=30")""").show(false)
        //spark.sql("""select * from nest_table where array_exists(user.score, "x>=300")""").show(false)
        //spark.sql("""select * from nest_table where array_exists2(user.score, "x>=300")""").show(false)
        //spark.sql("""select group, user.age, array_map(user.age, "x+30"), array_filter(array_map(user.age, "x+30"), "x<60") from nest_table where array_exists(array_filter(array_map(user.age, "x+30"), "x<60"), "x<60")""").show(false)
        //spark.sql("""select * from nest_table where array_exists(user.age, "x>50 and x<70")""").show(false)
        //spark.sql("""select * from nest_table where array_exists(user.score, "x>600")""").show(false)
        //spark.sql("""select * from nest_table where array_exists(user.age, "x>=45 and x<=60")""").show(false)
        //spark.sql("""select * from nest_table where array_exists(user.age, "x>=45 or x<=25")""").show(false)
        //spark.sql("""select user.last, user.age from nest_table where array_exists(user.age, "x>=45 or x<=25")""").show(false)
        spark.sql("""select user.last, user.age from nest_table where array_exists(user.age, "x>19")""").show(false)
        //spark.sql("""SELECT comments as ccc, comments.name as aaa, comments.age as bbb from  nest_table where array_exists(comments.age, "x>19")""").show(false)
        //spark.sql("""select map_values(map(1, 'a', 2, 'b')) """).show(false)
        //spark.sql("""select array_map(user.score, "x + 100"), user.age from nest_table""").show(false)
        //spark.sql("""select array_map(user.score, "x + 100"), user.score from nest_table""").show(false)

        //spark.sql("""select array_map(user.first, "x+\"aaa\""), user.first from nest_table""").show(false)
        //spark.sql("""select array_map(array_map(array_map(user.age, "x + 100"), "x * 2"), "x + 1000"), user.age from nest_table""").show(false)
        //spark.sql("SELECT sort_array(user.last, true), user.last, user.age from nest_table").show(false)
        //spark.sql("""SELECT array_filter(user.first, "x='John'"), user.first, user.age from nest_table""").show(false)
        //spark.sql("""SELECT array_filter(user.age, "x>30"), user.first, user.age from nest_table""").show(false)
        //spark.sql("""SELECT array_filter(array_map(array_filter(user.age, "x>30"), "x*2+1000"), "x>1100"), user.first, user.age from nest_table""").show(false)
        //spark.sql("""SELECT max(size(user.age)) from nest_table""").show(false)

        // val plan: LogicalPlan = sqlParser.parsePlan("select * from basic_info_mix_index where array_exists( member.name = 'aaa' )" )
        //val plan: LogicalPlan = sqlParser.parsePlan("select * from nest_table where array_contains(user.first, 'John')" )
       /* val plan: LogicalPlan = sqlParser.parsePlan("select * from nest_table where array_exists(user.first = 'John')" )

        val plan1: LogicalPlan = analyzer.execute(plan)
        val plan2: LogicalPlan = optimizer.execute(plan1)
        val a = plan2.schema
        val schema: StructType = StructType.fromAttributes(plan2.output)

        val df2: DataFrame = Dataset.ofRows(spark, plan2)
        df2.show()*/

    }

    def expressionInfo[T <: Expression : ClassTag](name: String): ExpressionInfo = {
        val clazz = scala.reflect.classTag[T].runtimeClass
        val df = clazz.getAnnotation(classOf[ExpressionDescription])
        if (df != null) {
            new ExpressionInfo(clazz.getCanonicalName, null, name, df.usage(), df.extended())
        } else {
            new ExpressionInfo(clazz.getCanonicalName, name)
        }
    }

    def expression[T <: Expression](name: String)
                                   (implicit tag: ClassTag[T]): (String, (ExpressionInfo, FunctionBuilder)) = {

        // For `RuntimeReplaceable`, skip the constructor with most arguments, which is the main
        // constructor and contains non-parameter `child` and should not be used as function builder.
        val constructors = if (classOf[RuntimeReplaceable].isAssignableFrom(tag.runtimeClass)) {
            val all = tag.runtimeClass.getConstructors
            val maxNumArgs = all.map(_.getParameterCount).max
            all.filterNot(_.getParameterCount == maxNumArgs)
        } else {
            tag.runtimeClass.getConstructors
        }
        // See if we can find a constructor that accepts Seq[Expression]
        val varargCtor = constructors.find(_.getParameterTypes.toSeq == Seq(classOf[Seq[_]]))
        val builder = (expressions: Seq[Expression]) => {
            if (varargCtor.isDefined) {
                // If there is an apply method that accepts Seq[Expression], use that one.
                Try(varargCtor.get.newInstance(expressions).asInstanceOf[Expression]) match {
                    case Success(e) => e
                    case Failure(e) =>
                        // the exception is an invocation exception. To get a meaningful message, we need the
                        // cause.
                        throw new AnalysisException(e.getCause.getMessage)
                }
            } else {
                // Otherwise, find a constructor method that matches the number of arguments, and use that.
                val params = Seq.fill(expressions.size)(classOf[Expression])
                val tmp = constructors.find(_.getParameterTypes.toSeq == params)

                val f = constructors.find(_.getParameterTypes.toSeq == params).getOrElse {
                    throw new AnalysisException(s"Invalid number of arguments for function $name")
                }
                Try(f.newInstance(expressions : _*).asInstanceOf[Expression]) match {
                    case Success(e) => e
                    case Failure(e) =>
                        // the exception is an invocation exception. To get a meaningful message, we need the
                        // cause.
                        throw new AnalysisException(e.getCause.getMessage)
                }
            }
        }

        (name, (expressionInfo[T](name), builder))
    }


}
