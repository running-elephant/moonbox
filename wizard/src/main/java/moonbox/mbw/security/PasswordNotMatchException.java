package moonbox.mbw.security;


public class PasswordNotMatchException extends LoginFailedException {

    public PasswordNotMatchException() {
        super("Username or password is incorrect");
    }
}
