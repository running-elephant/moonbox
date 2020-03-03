package moonbox.tools

import java.security.MessageDigest

/**
  * Created by swallow on 2019/9/12.
  */
object MoonboxLdapUserTool {

  private val KEY_SHA = "SHA"
  private val hexDigits = (0 to 9).++('a' to 'f').map(_.toString)

  def main(args: Array[String]): Unit = {
    assert(args.length == 1 && args.head.endsWith("@creditease.cn"), "please input one ldap user email")
    val pwd = encryptSHA(args.head.stripSuffix("@creditease.cn"))
    println(s"user ${args.head} password: $pwd")
  }

  def encryptSHA(data: String): String = {
    if (data == null || data.equals("")) {
      ""
    } else {
      val sha = MessageDigest.getInstance(KEY_SHA)
      sha.update(data.getBytes)
      byteArrayToHexString(sha.digest()).toUpperCase
    }
  }

  private def byteArrayToHexString(bytes: scala.Array[Byte]): String = {
    bytes.map(byteToHexString).reduce(_ + _)
  }

  private def byteToHexString(byte: Byte): String = {
    val res = if (byte < 0) byte + 256
    else byte
    hexDigits(res / 16) + hexDigits(res % 16)
  }
}
