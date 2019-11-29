package moonbox.wizard.security;


import moonbox.catalog.JdbcCatalog;
import moonbox.common.MbConf;

public class LdapLogin implements Login {

    private MbConf conf;

    private JdbcCatalog catalog;

    public LdapLogin(MbConf conf, JdbcCatalog catalog) {
        this.conf = conf;
        this.catalog = catalog;
    }

    @Override
    public Session doLogin(String org, String user, String password) {
        return null;
    }
}
