package moonbox.client

import io.netty.channel.nio.NioEventLoopGroup
import io.netty.util.concurrent.DefaultThreadFactory

object SingleEventLoopGroup{
  val daemonNioEventLoopGroup = new NioEventLoopGroup(0, new DefaultThreadFactory(this.getClass, true))
  val nioEventLoopGroup = new NioEventLoopGroup()
}

