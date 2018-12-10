package moonbox.grid.deploy.master

import akka.actor.ActorRef
import akka.actor.Address

private[deploy] class ApplicationInfo(
	val startTime: Long,
	val id: String,
	val host: String,
	val port: Int,
	val address: Address,
	val dataPort: Int,
	val endpoint: ActorRef,
	val appType: ApplicationType
) extends Serializable {

	@transient var state: ApplicationState.Value = _

	init()

	private def init(): Unit = {
		state = ApplicationState.RUNNING
	}

	private def readObject(in: java.io.ObjectInputStream): Unit = {
		in.defaultReadObject()
		init()
	}

	override def toString: String = {
		s"""startTime: $startTime
		   |id: $id
		   |host: $host
		   |port: $port
		   |address: $address
		   |dataPort: $dataPort
		   |endpoint: $endpoint
		   |type: ${appType.getClass.getSimpleName.stripSuffix("$")}
		   |state: $state
		 """.stripMargin
	}
}

trait ApplicationType

object ApplicationType {

	def apply(`type`: String): ApplicationType = {
		if (`type`.equalsIgnoreCase("CENTRALIZED")) {
			CENTRALIZED
		} else {
			DISTRIBUTED
		}
	}

	case object CENTRALIZED extends ApplicationType

	case object DISTRIBUTED extends ApplicationType
}




