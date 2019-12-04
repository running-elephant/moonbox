package moonbox.grid.deploy.rest.entities


case class Response[A](code: Int, msg: String, payload: Option[A] = None)
