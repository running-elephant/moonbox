package moonbox.mbw.service;

import moonbox.mbw.security.Session;
import org.springframework.stereotype.Service;

@Service
public interface LoginService {

    String login(String user, String password);

    String generateToken(Session session);

}
