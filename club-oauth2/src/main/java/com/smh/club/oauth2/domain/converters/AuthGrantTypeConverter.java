package com.smh.club.oauth2.domain.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

@Converter(autoApply = true)
public class AuthGrantTypeConverter implements AttributeConverter<AuthorizationGrantType, String> {

  @Override
  public String convertToDatabaseColumn(AuthorizationGrantType authorizationGrantType) {
    return authorizationGrantType.getValue();
  }

  @Override
  public AuthorizationGrantType convertToEntityAttribute(String data) {
    return new AuthorizationGrantType(data);
  }
}
