package moonbox.application.batch

import org.apache.spark.SparkContext


object Main {
	def main(args: Array[String]) {
		val sparkContext: SparkContext = new SparkContext()
		sparkContext.parallelize(Seq(1,2,3)).count()
	}
}
