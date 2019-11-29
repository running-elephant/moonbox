package moonbox.wizard;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * springBoot starter
 */
@SpringBootApplication
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
//@EnableDiscoveryClient
//@EnableEurekaClient
@ComponentScan(basePackages = "moonbox.wizard")
@EnableSwagger2
public class MoonboxWizardServer extends WebMvcConfigurationSupport {
    private static final Logger logger = LoggerFactory.getLogger(MoonboxWizardServer.class);

    public static void main(String[] args) {

        new SpringApplicationBuilder(MoonboxWizardServer.class)
                .web(true)
                .run(args);

        logger.info("MoonboxWizard Server started success !");
    }

}
