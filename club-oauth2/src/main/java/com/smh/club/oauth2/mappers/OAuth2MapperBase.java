package com.smh.club.oauth2.mappers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smh.club.oauth2.config.ObjectMapperConfig;
import java.util.Map;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module;

public abstract class OAuth2MapperBase {
  private final ObjectMapper mapper;

  protected OAuth2MapperBase(ObjectMapper mapper) {
    this.mapper = mapper;
  }

  public OAuth2MapperBase() {
    var classLoader = ObjectMapperConfig.class.getClassLoader();

    var modules = SecurityJackson2Modules.getModules(classLoader);
    modules.add(new OAuth2AuthorizationServerJackson2Module());
    mapper = new Jackson2ObjectMapperBuilder().modules(modules).build();
  }

  protected Map<String, Object> parseMap(String data) {
    try {
      return this.mapper.readValue(data, new TypeReference<>() {
      });
    } catch (Exception ex) {
      throw new IllegalArgumentException(ex.getMessage(), ex);
    }
  }

  protected String writeMap(Map<String, Object> data) {
    try {
      return this.mapper.writeValueAsString(data);
    } catch (Exception ex) {
      throw new IllegalArgumentException(ex.getMessage(), ex);
    }
  }


}
