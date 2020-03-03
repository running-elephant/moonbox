package moonbox.grid.deploy.security


object RoleType extends Enumeration {

  type RoleType = Value

  val ROOT = Value("1")
  val SA = Value("2")
  val NORMAL = Value("3")

  def roleType(code: String) = RoleType.withName(code)
}
