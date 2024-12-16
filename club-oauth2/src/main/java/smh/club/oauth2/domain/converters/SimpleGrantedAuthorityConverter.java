package smh.club.oauth2.domain.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Converter(autoApply = true)
public class SimpleGrantedAuthorityConverter implements AttributeConverter<SimpleGrantedAuthority, String> {

  @Override
  public String convertToDatabaseColumn(SimpleGrantedAuthority grantedAuthority) {
    return grantedAuthority.getAuthority();
  }

  @Override
  public SimpleGrantedAuthority convertToEntityAttribute(String data) {
    return new SimpleGrantedAuthority(data);
  }
}
