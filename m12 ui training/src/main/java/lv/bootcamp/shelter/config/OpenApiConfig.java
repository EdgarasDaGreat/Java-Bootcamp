package lv.bootcamp.shelter.config;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI basicUiOpenApi(){
        return new OpenAPI()
                .info(new Info()
                        .title("Basic UI demo app")
                        .version("v1")
                        .description("Rest Endpoints used in the basic UI module"));
    }
}
