package moonbox.core.udf

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.catalyst.expressions.{Expression, ScalaUDF}
import org.apache.spark.sql.execution.aggregate.ScalaUDAF
import org.apache.spark.sql.types.DoubleType
import org.scalatest.{BeforeAndAfterAll, FunSuite}

import scala.reflect.api.JavaUniverse

class MbUdfSuite extends FunSuite with BeforeAndAfterAll{

  var session: SparkSession = _
  var jarPath: String = _

  override def beforeAll: scala.Unit = {
    session = SparkSession.builder().master("local[2]").appName("udf").getOrCreate()
    //session.sparkContext.addJar(jarPath)
    val rootDir = System.getProperty("user.dir")

    val df = session.read.json(s"$rootDir/core/src/test/resources/temperatures.json")
    df.createOrReplaceTempView("citytemps")

    val df1 = session.read.json(s"$rootDir/core/src/test/resources/inventory.json")
    df1.createOrReplaceTempView("inventory")

    val df2 = session.read.json(s"$rootDir/core/src/test/resources/cities.json")
    df2.createOrReplaceTempView("cities")

    jarPath = s"file:$rootDir/core/src/test/resources/udfTest.jar"  //update jar path

  }

  test("scala class") {
    val src =
      """
        |class A { val value = 100}
        |class PersonData()  {
        |  val field = 42
        |  val obj = new A()
        |
        |  def multiply(a: AnyRef): Double = {
        |    val d: Double = a.asInstanceOf[Double]
        |    ((d * 9.0 / 5.0) + 32.0) + obj.value
        |  }
        |}
      """.stripMargin

	  val (func, returnType) = ScalaSourceUDF(src, "PersonData", Some("multiply"))
	  session.sessionState.functionRegistry.registerFunction("multiply", (e: Seq[Expression]) => ScalaUDF(func, returnType, e))
	  session.sql("SELECT city, multiply(avgLow) AS avgLowF, multiply(avgHigh) AS avgHighF FROM citytemps").show()
  }


  test("scala class extends Function trait") {
    val src =
		"""
		  |class PersonData extends Function1[Double, Double] {
		  |  val field: Double = 42d
		  |  override def apply(v1: Double): Double = {
		  |    ((v1 * 9.0 / 5.0) + field)
		  |  }
		  |}
		""".stripMargin
	  val (func, returnType) = ScalaSourceUDF(src, "PersonData", None)
	  session.sessionState.functionRegistry.registerFunction("multiply", (e: Seq[Expression]) => ScalaUDF(func, returnType, e))
    session.sql("SELECT city, multiply(avgLow) AS avgLowF, multiply(avgHigh) AS avgHighF FROM citytemps").show()
  }


  test("java class") {
    val src =
      """package mytest;
        |import java.io.Serializable;
        |public class Panda  {
        |    public Double multiply(Double d) {
        |        return d * 2;
        |    }
        |    public Double add(Double d) {
        |        return d * 200;
        |    }
        |};
      """.stripMargin
	  val (func, returnType) = JavaSourceUDF(src, "mytest.Panda", Some("multiply"))
    session.sessionState.functionRegistry.registerFunction("multiply", (e: Seq[Expression]) => ScalaUDF(func, returnType, e))
    session.sql("SELECT city, multiply(avgLow) AS avgLowF, multiply(avgHigh) AS avgHighF FROM citytemps").show()
  }
	test("java class implements UDF interface") {
		val src =
			"""package mytest;
			  |import java.io.Serializable;
			  |import org.apache.spark.sql.api.java.UDF1;
			  |public class Panda implements UDF1<Double, Double>{
			  |    @Override public Double call(Double d) {
			  |        return d * 2;
			  |    }
			  |};
			""".stripMargin
		val (func, returnType) = JavaSourceUDF(src, "mytest.Panda", None)
		session.sessionState.functionRegistry.registerFunction("multiply", (e: Seq[Expression]) => ScalaUDF(func, returnType, e))
		session.sql("SELECT city, multiply(avgLow) AS avgLowF, multiply(avgHigh) AS avgHighF FROM citytemps").show()
	}

  /*test("jar java udf file") {
    session.sessionState.functionRegistry.registerFunction("multiply", (e: Seq[Expression]) =>
      ScalaUDF(UdfHelper.generateFunction(1, "multiply", "", jarPath, "zoo.Panda", "jar"), DoubleType, e))
    session.sql("SELECT city, multiply(avgLow) AS avgLowF, multiply(avgHigh) AS avgHighF FROM citytemps").show()
  }

  test("jar scala udf file") {
    session.sessionState.functionRegistry.registerFunction("multiply", (e: Seq[Expression]) => ScalaUDF(UdfHelper.generateFunction(1, "multiply", "", jarPath, "zoo.Tiger$", "jar"), DoubleType, e))
    session.sql("SELECT city, multiply(avgLow) AS avgLowF, multiply(avgHigh) AS avgHighF FROM citytemps").show()
  }

  test("jar scala udaf file") {
    session.sessionState.functionRegistry.registerFunction("multiply", (e: Seq[Expression]) =>
      ScalaUDAF(e, UdfHelper.generateAggFunction(path=jarPath, className="zoo.Monkey", tpe="jar")))
    session.sql("SELECT Make, multiply(RetailValue, Stock) AS InventoryValuePerMake FROM inventory GROUP BY Make").show()
  }
  test("jar java udaf file") {
    session.sessionState.functionRegistry.registerFunction("fun", (e: Seq[Expression]) =>
      ScalaUDAF(e, UdfHelper.generateAggFunction(path=jarPath, className="zoo.Bear", tpe="jar")))
    session.sql("SELECT city , count['dominant'] as Dominant, count['Total'] as Total from(select city, fun(Female, Male) as count from cities group by (city)) temp").show()
  }*/


  test("udaf scala") {
    val src =
      """
        |import org.apache.spark.sql.expressions.{MutableAggregationBuffer, UserDefinedAggregateFunction}
        |import org.apache.spark.sql.types._
        |import org.apache.spark.sql.Row
        |class SumAggregation extends UserDefinedAggregateFunction with Serializable{
        |    // Define the UDAF input and result schema's  ,  Input  = (Double price, Long quantity)
        |    def inputSchema: StructType = new StructType().add("price", DoubleType).add("quantity", LongType)
        |    def bufferSchema: StructType =  new StructType().add("total", DoubleType)  // Output = (Double total)
        |    def dataType: DataType = DoubleType
        |    def deterministic: Boolean = true // true: our UDAF's output given an input is deterministic
        |    def initialize(buffer: MutableAggregationBuffer): Unit = {
        |      buffer.update(0, 0.0)           // Initialize the result to 0.0
        |    }
        |    def update(buffer: MutableAggregationBuffer, input: Row): Unit = {
        |      val sum   = buffer.getDouble(0) // Intermediate result to be updated
        |      val price = input.getDouble(0)  // First input parameter
        |      val qty   = input.getLong(1)    // Second input parameter
        |      buffer.update(0, sum + (price * qty))   // Update the intermediate result
        |    }
        |    def merge(buffer1: MutableAggregationBuffer, buffer2: Row): Unit = {
        |      // Merge intermediate result sums by adding them
        |      buffer1.update(0, buffer1.getDouble(0) + buffer2.getDouble(0))
        |    }
        |    def evaluate(buffer: Row): Any = {  // THe final result will be contained in 'buffer'
        |      buffer.getDouble(0)
        |    }
        |}
      """.stripMargin

    session.sessionState.functionRegistry.registerFunction("multiply", (e: Seq[Expression]) =>
      ScalaUDAF(e, ScalaSourceUDAF(src, "SumAggregation")))  //UserDefinedAggregateFunction

    session.sql("SELECT Make, multiply(RetailValue, Stock) AS InventoryValuePerMake FROM inventory GROUP BY Make").show()

  }

  test("udaf java") {
    val src =
      """
        |package zoo;
        |import java.util.ArrayList;
        |import java.util.HashMap;
        |import java.util.List;
        |import java.util.Map;
        |import org.apache.spark.sql.Row;
        |import org.apache.spark.sql.expressions.MutableAggregationBuffer;
        |import org.apache.spark.sql.expressions.UserDefinedAggregateFunction;
        |import org.apache.spark.sql.types.DataType;
        |import org.apache.spark.sql.types.DataTypes;
        |import org.apache.spark.sql.types.StructField;
        |import org.apache.spark.sql.types.StructType;
        |public class Bear extends UserDefinedAggregateFunction{
        |    private StructType inputSchema;
        |    private StructType bufferSchema;
        |    private DataType returnDataType =   DataTypes.createMapType(DataTypes.StringType, DataTypes.StringType);
        |    MutableAggregationBuffer mutableBuffer;
        |    public Bear(){
        |        List<StructField> inputFields = new ArrayList<StructField>();  //inputSchema : This UDAF can accept 2 inputs which are of type Integer
        |        StructField inputStructField1 = DataTypes.createStructField("femaleCount",DataTypes.IntegerType, true);
        |        inputFields.add(inputStructField1);
        |        StructField inputStructField2 = DataTypes.createStructField("maleCount",DataTypes.IntegerType, true);
        |        inputFields.add(inputStructField2);
        |        inputSchema = DataTypes.createStructType(inputFields);
        |        List<StructField> bufferFields = new ArrayList<StructField>();  //BufferSchema : This UDAF can hold calculated data in below mentioned buffers
        |        StructField bufferStructField1 = DataTypes.createStructField("totalCount",DataTypes.IntegerType, true);
        |        bufferFields.add(bufferStructField1);
        |        StructField bufferStructField2 = DataTypes.createStructField("femaleCount",DataTypes.IntegerType, true);
        |        bufferFields.add(bufferStructField2);
        |        StructField bufferStructField3 = DataTypes.createStructField("maleCount",DataTypes.IntegerType, true);
        |        bufferFields.add(bufferStructField3);
        |        StructField bufferStructField4 = DataTypes.createStructField("outputMap",DataTypes.createMapType(DataTypes.StringType, DataTypes.StringType), true);
        |        bufferFields.add(bufferStructField4);
        |        bufferSchema = DataTypes.createStructType(bufferFields);
        |    }
        |    @Override  //This method determines which bufferSchema will be used
        |    public StructType bufferSchema() {        return bufferSchema;    }
        |    @Override  //This method determines the return type of this UDA
        |    public DataType dataType() {        return returnDataType;    }
        |    @Override   //Returns true iff this function is deterministic, i.e. given the same input, always return the same output.
        |    public boolean deterministic() {        return true;    }
        |    @Override  //This method will re-initialize the variables to 0 on change of city name
        |    public void initialize(MutableAggregationBuffer buffer) {
        |        buffer.update(0, 0);
        |        buffer.update(1, 0);
        |        buffer.update(2, 0);
        |        mutableBuffer = buffer;
        |    }
        |    @Override //This method is used to increment the count for each city
        |    public void update(MutableAggregationBuffer buffer, Row input) {
        |        buffer.update(0, buffer.getInt(0) + input.getInt(0) + input.getInt(1));
        |        buffer.update(1, input.getInt(0));
        |        buffer.update(2, input.getInt(1));
        |    }
        |    @Override  //This method will be used to merge data of two buffers
        |    public void merge(MutableAggregationBuffer buffer, Row input) {
        |        buffer.update(0, buffer.getInt(0) + input.getInt(0));
        |        buffer.update(1, buffer.getInt(1) + input.getInt(1));
        |        buffer.update(2, buffer.getInt(2) + input.getInt(2));
        |    }
        |    @Override // This method calculates the final value by referring the aggregation buffer
        |    public Object evaluate(Row buffer) {//In this method we are preparing a final map that will be returned as output
        |        Map<String,String> op = new HashMap<String,String>();
        |        op.put("Total", "" + mutableBuffer.getInt(0));
        |        op.put("dominant", "Male");
        |        if(buffer.getInt(1) > mutableBuffer.getInt(2)) {
        |            op.put("dominant", "Female");
        |        }
        |        mutableBuffer.update(3,op);
        |        return buffer.getMap(3);
        |    }
        |    @Override
        |    public StructType inputSchema() {        return inputSchema;    }
        |};
      """.stripMargin

    session.sessionState.functionRegistry.registerFunction("fun", (e: Seq[Expression]) =>
      ScalaUDAF(e, JavaSourceUDAF(src, "zoo.Bear")))  //UserDefinedAggregateFunction

    session.sql("SELECT city , count['dominant'] as Dominant, count['Total'] as Total from(select city, fun(Female, Male) as count from cities group by (city)) temp").show()

  }

}



