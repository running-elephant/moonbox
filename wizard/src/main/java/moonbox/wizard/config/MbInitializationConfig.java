package moonbox.wizard.config;

import moonbox.catalog.JdbcCatalog;
import moonbox.common.MbConf;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MbInitializationConfig {

    @Bean
    public MbConf getMbConf() {
        return new MbConf();
    }

    @Bean
    public JdbcCatalog getMbCatalog() {
        return new JdbcCatalog(getMbConf());
    }
}
