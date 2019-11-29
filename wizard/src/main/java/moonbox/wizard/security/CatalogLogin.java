package moonbox.wizard.security;


import moonbox.catalog.CatalogUser;
import moonbox.catalog.JdbcCatalog;
import moonbox.catalog.PasswordEncryptor;
import moonbox.common.MbConf;
import scala.Option;

public class CatalogLogin implements Login {

    private MbConf conf;

    private JdbcCatalog catalog;

    public CatalogLogin(MbConf conf, JdbcCatalog catalog) {
        this.conf = conf;
        this.catalog = catalog;
    }

    @Override
    public Session doLogin(String org, String user, String password) {
        Option<CatalogUser> userOption = catalog.getUserOption(org, user);
        if (userOption.nonEmpty()) {
            CatalogUser catalogUser = userOption.get();
            if (catalogUser.password().equals(PasswordEncryptor.encryptSHA(password))) {
                Long orgId = catalog.organizationId(org);
                Long userId = catalog.userId(orgId, user);
                return new Session(userId, catalogUser.name(), orgId, org);
            } else {
                throw new PasswordNotMatchException();
            }
        } else {
            throw new UserNotFoundException(user, org);
        }
    }
}
