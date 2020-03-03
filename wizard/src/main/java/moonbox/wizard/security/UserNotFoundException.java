package moonbox.wizard.security;

public class UserNotFoundException extends LoginFailedException {

    public UserNotFoundException(String user, String org) {
        super("User " + user + "is not found in org " + org);
    }
}
