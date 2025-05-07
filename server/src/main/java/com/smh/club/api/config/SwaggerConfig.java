package com.smh.club.api.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.*;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
public class SwaggerConfig {

    String SCHEME_NAME = "OPENID_CONNECT";
    String OPEN_ID_CONNECT_URL = "http://localhost:8080/realms/smh-club/.well-known/openid-configuration";

    @Bean
    public OpenAPI caseOpenAPI() {
        return new OpenAPI()
            .components(new Components()
                .addSecuritySchemes(SCHEME_NAME, createOAuthScheme()))
            .addSecurityItem(new SecurityRequirement().addList(SCHEME_NAME))
            .info(new Info()
                .title("SMH Club API")
                .description("Club member information")
                .version("1.0")
            );
    }

    @Bean
    public GroupedOpenApi api() {
        String[] paths = {"/api/**"};
        return GroupedOpenApi.builder()
            .group("public")
            .pathsToMatch(paths)
            .build();
    }

    private SecurityScheme createOAuthScheme() {
        return new SecurityScheme()
            .type(SecurityScheme.Type.OPENIDCONNECT)
            .openIdConnectUrl(OPEN_ID_CONNECT_URL);
    }
}
