package moonbox.grid.deploy.rest.entities


case class ResponseHeader(code: Int, msg: String, token: Option[String] = None)
case class Response[A](header: ResponseHeader, payload: Option[A] = None)
