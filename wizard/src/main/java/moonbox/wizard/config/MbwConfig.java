package moonbox.wizard.config;


public class MbwConfig {

    public static String LOGIN_IMPLEMENTATION = "moonbox.deploy.login.implementation";

    public static String DEFAULT_LOGIN_IMPLEMENTATION = "built-in";

    public static String JWT_ALGORITHM = "moonbox.jwt.algorithm";

    public static String DEFAULT_JWT_ALGORITHM = "HS256";

    public static String JWT_SECRET = "moonbox.jwt.secret";

    public static String DEFAULT_JWT_SECRET = "moonbox_secret";

    public static String LOGIN_TIMEOUT = "moonbox.deploy.login.timeout";

    public static Long DEFAULT_LOGIN_TIMEOUT = 3600000l;

    public static String LOGIN_SINGLE_ENABLE = "moonbox.deploy.login.single.enable";

    public static boolean DEFAULT_LOGIN_SINGLE_ENABLE = false;

}
