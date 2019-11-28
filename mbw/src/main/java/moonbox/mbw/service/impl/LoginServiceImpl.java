package moonbox.mbw.service.impl;


import moonbox.mbw.security.LoginManager;
import moonbox.mbw.security.Session;
import moonbox.mbw.security.TokenEncoder;
import moonbox.mbw.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private LoginManager loginManager;

    @Autowired
    private TokenEncoder tokenEncoder;

    @Override
    public String login(String user, String password) {
        Session session = loginManager.login(user, password);
        String token = generateToken(session);
        return token;
    }

    @Override
    public String generateToken(Session session) {
        return tokenEncoder.encode(session);
    }


}
