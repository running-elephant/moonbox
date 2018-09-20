package moonbox.grid.deploy2.node

object RoleState extends Enumeration {
	type RoleState = Value

	val MASTER, SLAVE, RECOVERING, COMPLETING_RECOVERY = Value
}
