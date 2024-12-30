package com.smh.club.oauth2.config.mappers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration
public class ObjectMapperConfig {

  @Bean
  public ObjectMapper withoutOath2() {
    return new Jackson2ObjectMapperBuilder().build();
  }
}
