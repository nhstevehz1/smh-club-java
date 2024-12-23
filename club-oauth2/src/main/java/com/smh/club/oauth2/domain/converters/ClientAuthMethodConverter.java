package com.smh.club.oauth2.domain.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;

@Converter(autoApply = true)
public class ClientAuthMethodConverter implements AttributeConverter<ClientAuthenticationMethod, String> {

  @Override
  public String convertToDatabaseColumn(ClientAuthenticationMethod clientAuthenticationMethod) {
    return clientAuthenticationMethod.getValue();
  }

  @Override
  public ClientAuthenticationMethod convertToEntityAttribute(String data) {
    return new ClientAuthenticationMethod(data);
  }
}
