package com.smh.club.oauth2.helpers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;

public class OAuth2MapperTestBase {
  private final ObjectMapper mapper;

  public OAuth2MapperTestBase() {
    mapper = TestUtils.getObjectMapper();
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
