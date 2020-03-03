package moonbox.hook

/**
  * ExecuteWithHookContext is a interface that the Execute Hook can run with the HookContext.
  */
trait ExecuteWithHookContext extends Hook {

  /**
    * @param hookContext The hook context passed to each hooks.
    * @throws Exception
    */
  @throws[Exception]
  def run(hookContext: HookContext)

}
