package com.smh.club.oauth2.helpers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module;
import org.springframework.security.web.jackson2.WebJackson2Module;
import org.springframework.security.web.jackson2.WebServletJackson2Module;
import org.springframework.security.web.server.jackson2.WebServerJackson2Module;

public class TestUtils {

  private TestUtils() {}

  public static ObjectMapper getObjectMapper() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new WebJackson2Module());
    mapper.registerModule(new WebServletJackson2Module());
    mapper.registerModule(new WebServerJackson2Module());
    mapper.registerModule(new OAuth2AuthorizationServerJackson2Module());
    mapper.registerModule(new JavaTimeModule());
    return mapper;
  }

}
