package moonbox.grid.deploy.cluster.master

object RecoveryState extends Enumeration {
	type MasterState = Value

	val STANDBY, ACTIVE, RECOVERING, COMPLETING_RECOVERY = Value
}
