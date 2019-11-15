package moonbox.application.interactive.spark

import akka.actor.Address
import moonbox.grid.deploy.app._
import moonbox.grid.deploy.master.WorkerInfo

import scala.collection.mutable

class SparkAppMaster extends AppMaster {
	// for interactive application

	override def typeName: String = "SPARK"

	override def createDriverDesc: DriverDesc = ???
}
