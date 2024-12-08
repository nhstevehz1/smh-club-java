package smh.club.oauth2.domain.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Set;
import org.springframework.util.StringUtils;

@Converter(autoApply = true)
public class StringSetConverter implements AttributeConverter<Set<String>, String> {
  @Override
  public String convertToDatabaseColumn(Set<String> strings) {
    return StringUtils.collectionToCommaDelimitedString(strings);
  }

  @Override
  public Set<String> convertToEntityAttribute(String str) {
    return StringUtils.commaDelimitedListToSet(str);
  }
}
