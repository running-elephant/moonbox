package moonbox.wizard.service;

import moonbox.wizard.security.Session;
import org.springframework.stereotype.Service;

@Service
public interface LoginService {

    String login(String user, String password);

    String generateToken(Session session);

}
