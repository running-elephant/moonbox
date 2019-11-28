package moonbox.mbw.security;


public interface Login {

    Session doLogin(String org, String user, String password);

}
