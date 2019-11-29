package moonbox.wizard.security;


public interface Login {

    Session doLogin(String org, String user, String password);

}
