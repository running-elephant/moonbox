package moonbox.wizard.security;

public class UsernameFormatException extends LoginFailedException {

    public UsernameFormatException(String user) {
        super("Username format is invalid. Expect format is orgName@userName, but it is " + user);
    }
}
