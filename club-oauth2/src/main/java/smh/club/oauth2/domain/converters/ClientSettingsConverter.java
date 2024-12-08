package smh.club.oauth2.domain.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;

@Converter(autoApply = true)
public class ClientSettingsConverter extends MapParserBase implements AttributeConverter<ClientSettings, String> {

  @Override
  public String convertToDatabaseColumn(ClientSettings clientSettings) {
    return writeMap(clientSettings.getSettings());
  }

  @Override
  public ClientSettings convertToEntityAttribute(String data) {
    return ClientSettings.withSettings(parseMap(data)).build();
  }
}
