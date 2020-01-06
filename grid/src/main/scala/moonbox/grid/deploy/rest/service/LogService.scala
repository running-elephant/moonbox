package moonbox.grid.deploy.rest.service

import java.io.{BufferedReader, InputStreamReader}

import com.jcraft.jsch.{ChannelExec, JSch, JSchException, Session}
import moonbox.catalog.AbstractCatalog.User
import moonbox.common.{MbConf, MbLogging}
import moonbox.grid.deploy.rest.entities.LogView
import moonbox.grid.deploy.rest.routes.SessionConverter

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class LogService(conf: MbConf) extends SessionConverter with MbLogging {

  private lazy val sshUser = Option(System.getenv("MOONBOX_SSH_USER")).getOrElse(System.getenv("USER"))
  private lazy val sshPwd = Option(System.getenv("MOONBOX_SSH_PASSWORD"))
  private lazy val sshPort = Option(System.getenv("MOONBOX_SSH_OPTS")).map(_.substring(2).toInt)

  private val FILE_PLACEHOLDER = "LOG_FILE"
  private val supportedCmd = Seq("tail", "head")
  private val defaultCmd = s"tail -n2000"

  import LogUtils._

  def viewLog(log: LogView)(implicit user: User): Future[Either[String, Throwable]] = {
    try {
      val host = getHost(log.address)
      val cmd = buildViewLogCmd(host, log.file, log.command)
      val content = execCmd(sshUser, host, sshPort, sshPwd, cmd)
      Future(Left(content))
    } catch {
      case ex: Throwable => Future(Right(throw ex))
    }
  }

  private def buildViewLogCmd(host: String, file: String, cmd: Option[String]): String = {
    val finalCmd = cmd.getOrElse(s"$defaultCmd $file").replace(FILE_PLACEHOLDER, file)
    checkCmd(finalCmd)
    finalCmd
  }

  private def checkCmd(cmd: String): Unit = {
    val cmdArray = cmd.trim.split("\\s+")
    val headCmd = cmdArray.head.toLowerCase
    if (supportedCmd.contains(headCmd)) {
      if (!cmdArray(1).matches("-n\\d+"))
        throw new Exception(s"$cmd not supported, just support tail -n, head -n command")
    } else {
      throw new Exception(s"$cmd not supported, just support tail -n, head -n command")
    }
  }

  private def getHost(address: String): String = {
    address.split(":").head.trim
  }

}

object LogUtils {

  private val timeout = 10 // seconds

  def execCmd(user: String, host: String, port: Option[Int], password: Option[String], cmd: String): String = {
    try {
      val session = getJSchSession(user, host, port, password)
      val channelExec = session.openChannel("exec").asInstanceOf[ChannelExec]
      channelExec.setCommand(cmd)
      channelExec.setInputStream(null)
      channelExec.connect()
      val reader = new BufferedReader(new InputStreamReader(channelExec.getInputStream, "UTF8"))
      val content = reader.lines().toArray.mkString("\n")
      reader.close()
      channelExec.disconnect()
      content
    } catch {
      case ex: JSchException =>
        throw new Exception(s"exec shell command $cmd failed caused by ${ex.getMessage}", ex)
    }
  }

  private def getJSchSession(user: String, host: String, port: Option[Int], password: Option[String]): Session = {
    val session = new JSch().getSession(user, host, port.getOrElse(22))
    password.foreach(password => session.setPassword(password))
    session.setTimeout(timeout * 1000)
    session.setConfig("StrictHostKeyChecking", "no")
    session.connect()
    session
  }
}