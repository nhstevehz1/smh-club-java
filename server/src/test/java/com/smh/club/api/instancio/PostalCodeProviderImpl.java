package com.smh.club.api.instancio;

import java.lang.reflect.Field;

import com.smh.club.api.validation.constraints.PostalCode;
import org.instancio.Node;
import org.instancio.generator.GeneratorSpec;
import org.instancio.generators.Generators;
import org.instancio.spi.InstancioServiceProvider;

/**
 * Used by Instancio.  Generates valid postal codes
 */
public class PostalCodeProviderImpl implements InstancioServiceProvider {

  @Override
  public GeneratorProvider getGeneratorProvider() {
    return new PostalCodeGeneratorImpl();
  }

  public static class PostalCodeGeneratorImpl implements GeneratorProvider {

    @Override
    public GeneratorSpec<?> getGenerator(Node node, Generators gen) {
      Field field = node.getField();
      Class<?> targetClass = node.getTargetClass();

      if (targetClass == String.class && field != null) {
        PostalCode postalCode = field.getDeclaredAnnotation(PostalCode.class);
        if (postalCode != null) {
          return gen.text().pattern("#d#d#d#d#d-#d#d#d#d");
        }
      }
      return null;
    }
  }
}
