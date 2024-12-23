package com.smh.club.oauth2.domain.converters;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Converter
@Component
public class StringObjectMapConverter implements AttributeConverter<Map<String, Object>, String> {

  private final ObjectMapper mapper;

  @Autowired
  public StringObjectMapConverter(ObjectMapper mapper) {
    this.mapper = mapper;
  }

  @Override
  public String convertToDatabaseColumn(Map<String, Object> map) {

    try {
      return mapper.writeValueAsString(map);
    } catch (Exception ex) {
        throw new RuntimeException(ex);
    }
  }

  @Override
  public Map<String, Object> convertToEntityAttribute(String data) {
    try {
      return mapper.readValue(data, new TypeReference<>() {
      });
    } catch (Exception ex){
      throw new RuntimeException(ex);
    }
  }


}
