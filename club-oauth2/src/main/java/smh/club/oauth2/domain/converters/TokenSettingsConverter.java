package smh.club.oauth2.domain.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;

@Converter(autoApply = true)
public class TokenSettingsConverter extends MapParserBase implements AttributeConverter<TokenSettings, String> {
  @Override
  public String convertToDatabaseColumn(TokenSettings tokenSettings) {
    return writeMap(tokenSettings.getSettings());
  }

  @Override
  public TokenSettings convertToEntityAttribute(String data) {
    return TokenSettings.withSettings(parseMap(data)).build();
  }
}
