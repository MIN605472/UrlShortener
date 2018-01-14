package liquidmountain.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Configuration of Swagger
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    /**
     *
     * @return Docket with Swagger info
     */
    @Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .apiInfo(metaData());
    }

    /**
     * Define LiquidMountain information
     * @return ApiInfo Object
     */
    private ApiInfo metaData() {
      return new ApiInfo(
                "Liquid Mountain's URL Shortener REST API",
                "Web application to shorten all kinds of URL's.\n" +
                        "You can shorten it in the form of QR. You can choose at what time the shortened link expires.\n" +
                        "Check the statistics collected by our service, number of clicks, countries from where they were collected ....",
                "2.0",
                "Terms of service",
                "Liquid-Mountain-G3",
                "",
                "");
    }

}