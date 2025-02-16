package com.smh.club.api.rest.instancio;

import com.smh.club.api.rest.validation.constraints.BirthDate;
import java.lang.reflect.Field;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import org.instancio.Node;
import org.instancio.generator.GeneratorSpec;
import org.instancio.generators.Generators;
import org.instancio.spi.InstancioServiceProvider;

/**
 * Used by Instancio.  Generates valid birth dates
 */
public class BirthDateProviderImpl implements InstancioServiceProvider {

  @Override
  public GeneratorProvider getGeneratorProvider() {
    return new BirthDateGeneratorImpl();
  }

  public static class BirthDateGeneratorImpl implements GeneratorProvider {

    @Override
    public GeneratorSpec<?> getGenerator(Node node, Generators gen) {
      Field field = node.getField();
      Class<?> targetClass = node.getTargetClass();

      if (targetClass == Instant.class && field != null) {
        BirthDate birthdate = field.getDeclaredAnnotation(BirthDate.class);
        if (birthdate != null) {
          return gen.temporal().instant().range(
              ZonedDateTime.now().minusYears(100).toInstant(),
              ZonedDateTime.now().minusYears(birthdate.minAge()).toInstant());
        }
      }
      return null;
    }
  }
}
