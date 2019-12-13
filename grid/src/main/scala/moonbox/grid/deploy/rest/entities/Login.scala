package moonbox.grid.deploy.rest.entities

case class Login(user: String, password: String)

case class LoginResult(token: String, roleType: Int)
