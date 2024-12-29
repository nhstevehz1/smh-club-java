package com.smh.club.oauth2.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("dev")
@Configuration
public class SwaggerConfig {

  //@Bean
  public GroupedOpenApi publicApi() {
    return GroupedOpenApi.builder()
        .group("public")
        .pathsToMatch("/**")
        .build();
  }
}
