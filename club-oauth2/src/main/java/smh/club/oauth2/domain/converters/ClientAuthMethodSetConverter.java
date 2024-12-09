package smh.club.oauth2.domain.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.util.StringUtils;

@Converter(autoApply = true)
public class ClientAuthMethodSetConverter implements AttributeConverter<Set<ClientAuthenticationMethod>, String> {

  @Override
  public String convertToDatabaseColumn(Set<ClientAuthenticationMethod> clientAuthenticationMethods) {

    var stringSet = clientAuthenticationMethods
        .stream().map(ClientAuthenticationMethod::getValue)
        .collect(Collectors.toSet());

    return StringUtils.collectionToCommaDelimitedString(stringSet);
  }

  @Override
  public Set<ClientAuthenticationMethod> convertToEntityAttribute(String str) {

    var strSet = StringUtils.commaDelimitedListToSet(str);

    return strSet.stream()
        .map(ClientAuthenticationMethod::new)
        .collect(Collectors.toSet());
  }
}
