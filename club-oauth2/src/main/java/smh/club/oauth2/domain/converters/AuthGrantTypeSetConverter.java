package smh.club.oauth2.domain.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.util.StringUtils;

@Converter(autoApply = true)
public class AuthGrantTypeSetConverter implements AttributeConverter<Set<AuthorizationGrantType>, String> {
  @Override
  public String convertToDatabaseColumn(Set<AuthorizationGrantType> authorizationGrantTypes) {

    var stringSet = authorizationGrantTypes
        .stream().map(AuthorizationGrantType::getValue)
        .collect(Collectors.toSet());

    return StringUtils.collectionToCommaDelimitedString(stringSet);
  }

  @Override
  public Set<AuthorizationGrantType> convertToEntityAttribute(String str) {

    var strSet = StringUtils.commaDelimitedListToStringArray(str);

    return Arrays.stream(strSet)
        .map(AuthorizationGrantType::new)
        .collect(Collectors.toSet());
  }
}
