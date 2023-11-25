package blackshoe.estheteuserservice.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
public class SwaggerConfig {
    private static final String API_NAME = "Esthete User Service API 명세서";
    private static final String API_VERSION = "1.0.0";
    private static final String API_DESCRIPTION = "Esthete";
    @Profile({"test || dev"})
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.OAS_30)
                //.ignoredParameterTypes(AuthenticationPrincipal.class)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

    public ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(API_NAME)
                .version(API_VERSION)
                .description(API_DESCRIPTION)
                .build();
    }

    @Bean
    @Profile({"!test && !dev"})
    public Docket disable() {
        return new Docket(DocumentationType.OAS_30).enable(false);
    }
}