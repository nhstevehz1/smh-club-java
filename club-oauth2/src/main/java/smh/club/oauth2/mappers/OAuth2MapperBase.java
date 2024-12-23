package smh.club.oauth2.mappers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;

public abstract class OAuth2MapperBase {
  private final ObjectMapper mapper;

  public OAuth2MapperBase(ObjectMapper mapper) {
    this.mapper = mapper;
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
