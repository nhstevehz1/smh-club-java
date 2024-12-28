package com.smh.club.oauth2.config.mappers;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module;

@Configuration
public class ObjectMapperConfig {

  @Bean
  public Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder() {
    var classLoader = ObjectMapperConfig.class.getClassLoader();

    var modules = SecurityJackson2Modules.getModules(classLoader);
    modules.add(new OAuth2AuthorizationServerJackson2Module());

    return  new Jackson2ObjectMapperBuilder().modules(modules);
  }
}
