package moonbox.wizard.config;

import com.google.common.collect.Lists;
import moonbox.wizard.consts.Constants;
import moonbox.wizard.enums.HttpCodeEnum;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * Swagger Config
 */
@Configuration
@EnableSwagger2
public class Swagger2Configuration {

    @Bean
    public Docket createRestApi() {

        List<ResponseMessage> responseMessageList = new ArrayList<>();
        responseMessageList.add(new ResponseMessageBuilder().code(HttpCodeEnum.OK.getCode()).message(HttpCodeEnum.OK.getMessage()).build());
        responseMessageList.add(new ResponseMessageBuilder().code(HttpCodeEnum.BAD_REQUEST.getCode()).message(HttpCodeEnum.BAD_REQUEST.getMessage()).build());
        responseMessageList.add(new ResponseMessageBuilder().code(HttpCodeEnum.UNAUTHORIZED.getCode()).message(HttpCodeEnum.UNAUTHORIZED.getMessage()).build());
        responseMessageList.add(new ResponseMessageBuilder().code(HttpCodeEnum.FORBIDDEN.getCode()).message(HttpCodeEnum.FORBIDDEN.getMessage()).build());
        responseMessageList.add(new ResponseMessageBuilder().code(HttpCodeEnum.NOT_FOUND.getCode()).message(HttpCodeEnum.NOT_FOUND.getMessage()).build());
        responseMessageList.add(new ResponseMessageBuilder().code(HttpCodeEnum.SERVER_ERROR.getCode()).message(HttpCodeEnum.SERVER_ERROR.getMessage()).build());
        responseMessageList.add(new ResponseMessageBuilder().code(HttpCodeEnum.DUPLICATED_RECORD.getCode()).message(HttpCodeEnum.DUPLICATED_RECORD.getMessage()).build());
        responseMessageList.add(new ResponseMessageBuilder().code(HttpCodeEnum.OPERATE_FAIL.getCode()).message(HttpCodeEnum.OPERATE_FAIL.getMessage()).build());


        return new Docket(DocumentationType.SWAGGER_2)
                .globalResponseMessage(RequestMethod.GET, responseMessageList)
                .globalResponseMessage(RequestMethod.POST, responseMessageList)
                .globalResponseMessage(RequestMethod.PUT, responseMessageList)
                .globalResponseMessage(RequestMethod.DELETE, responseMessageList)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("moonbox.mbw.controller"))
                .paths(PathSelectors.any())
                .build()
                .securitySchemes(Lists.newArrayList(apiKey()));

    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("moonbox wizard api")
                .version("0.3.0-beta")
                .build();
    }

    private ApiKey apiKey() {
        return new ApiKey(Constants.TOKEN_HEADER_STRING, Constants.TOKEN_HEADER_STRING, "header");
    }

}
