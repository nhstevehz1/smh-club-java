package smh.club.oauth2.domain.converters;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;

public abstract class MapParserBase {

  private final ObjectMapper mapper;

  public MapParserBase() {
    this.mapper = new ObjectMapper();
  }

  protected Map<String, Object> parseMap(final String data) {
    try {
      return this.mapper.readValue(data, new TypeReference<>() {
      });
    } catch (Exception ex) {
      throw new IllegalArgumentException(ex.getMessage(), ex);
    }
  }

  protected String writeMap(final Map<String, Object> data) {
    try {
      return this.mapper.writeValueAsString(data);
    } catch (Exception ex) {
      throw new IllegalArgumentException(ex.getMessage(), ex);
    }
  }

}
