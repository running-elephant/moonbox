package moonbox.catalyst.adapter.elasticsearch5.jdbc;

import java.sql.DriverManager;

public class Driver {
    static volatile boolean registered;
    static {
        try{
            if (!registered) {
                registered = true;
                DriverManager.registerDriver(new EsCatalystDriver());
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

    }
}
