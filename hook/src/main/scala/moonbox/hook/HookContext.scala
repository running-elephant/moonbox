package moonbox.hook

/**
  * Hook Context keeps all the necessary information for all the hooks.
  *
  * @param org
  * @param user
  * @param command
  * @param inputs input table set, table form like db.name
  * @param output output table form like db.name, when command isn't insert, the value is null
  */
class HookContext(org: String, user: String, command: String, inputs: java.util.Set[String], output: String) {

  def getOrg: String = this.org

  def getUser: String = this.user

  def getCommand: String = this.command

  def getInputs: java.util.Set[String] = this.inputs

  def getOutput: String = this.output

}

