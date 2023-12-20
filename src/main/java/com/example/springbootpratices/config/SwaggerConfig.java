package com.example.springbootpratices.config;

import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Your Custom Title")
                        .version("1.0")
                        .description("Your custom description here")
                        .license(new License().name("Your License").url("http://your-license-url.com")));
    }
}
