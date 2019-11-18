package moonbox.grid.deploy.security

class LoginFailedException(message: String) extends Exception(message)

class UsernameFormatException(username: String) extends LoginFailedException(
	s"Username format is invalid. Expect format is orgName@userName, but it is $username."
)

class UserNotFoundException(user: String) extends LoginFailedException(s"User $user is not found")

class PasswordNotMatchException extends LoginFailedException("Username or password is incorrect")
