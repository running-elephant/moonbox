package moonbox.core

import java.util.ServiceLoader

import moonbox.common.util.Utils
import moonbox.hook.{PostExecuteWithHookContext, _}

import scala.collection.JavaConverters._
import scala.collection.mutable.ListBuffer

/**
  * Handles hook executions for MoonboxSession
  */
object HookRunner {

  private val preExecHooks = ListBuffer.empty[PreExecuteWithHookContext]
  private val postExecHooks = ListBuffer.empty[PostExecuteWithHookContext]
  private val onFailureHooks = ListBuffer.empty[OnFailureExecuteWithHookContext]

  loadHooks()

  private def loadHooks(): Unit = {
    preExecHooks.appendAll(ServiceLoader.load(classOf[PreExecuteWithHookContext]).iterator().asScala)
    postExecHooks.appendAll(ServiceLoader.load(classOf[PostExecuteWithHookContext]).iterator().asScala)
    onFailureHooks.appendAll(ServiceLoader.load(classOf[OnFailureExecuteWithHookContext]).iterator().asScala)
  }

  def runPreExecHooks(hookContext: HookContext): Unit = {
    preExecHooks.foreach(preHook => Utils.tryLogNonFatalError(preHook.run(hookContext)))
  }

  def runPostExecHooks(hookContext: HookContext): Unit = {
    postExecHooks.foreach(postHook => Utils.tryLogNonFatalError(postHook.run(hookContext)))
  }

  def runOnFailureExecHooks(hookContext: HookContext): Unit = {
    onFailureHooks.foreach(onFailureHook => Utils.tryLogNonFatalError(onFailureHook.run(hookContext)))
  }

}
