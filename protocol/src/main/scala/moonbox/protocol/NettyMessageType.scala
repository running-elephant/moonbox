package moonbox.protocol

import java.util.Locale

object NettyMessageType extends Enumeration {
  type NettyMessageType = Value
  val PROTOBUF_MESSAGE = Value("protobuf")
  val JAVA_MESSAGE = Value("java")

  def getMessageType(name: String): NettyMessageType = {
    name.toLowerCase(Locale.ROOT) match {
      case "protobuf" => PROTOBUF_MESSAGE
      case "java" => JAVA_MESSAGE
      case _ => null
    }
  }
}
