package moonbox.wizard.security;

import moonbox.catalog.JdbcCatalog;
import moonbox.common.MbConf;
import moonbox.wizard.config.MbwConfig;
import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LoginManager {

    @Autowired
    private MbConf conf;

    @Autowired
    private JdbcCatalog catalog;

    public Login getLoginImpl() {
        return createLogin(conf.get(MbwConfig.LOGIN_IMPLEMENTATION, MbwConfig.DEFAULT_LOGIN_IMPLEMENTATION));
    }

    public Session login(String username, String password) {
        Pair<String, String> orgUser = parseUsername(username);
        return getLoginImpl().doLogin(orgUser.getValue0(), orgUser.getValue1(), password);
    }

    private Login createLogin(String loginType) {
        if (loginType.equalsIgnoreCase("LDAP"))
            return new LdapLogin(conf, catalog);
        else
            return new CatalogLogin(conf, catalog);
    }

    private Pair<String, String> parseUsername(String username) {
        String[] orgUser = username.split("@");
        if (orgUser.length != 2) {
            throw new UsernameFormatException(username);
        } else {
            return new Pair<>(orgUser[0], orgUser[1]);
        }
    }
}
