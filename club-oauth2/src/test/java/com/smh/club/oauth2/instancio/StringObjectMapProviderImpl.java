package com.smh.club.oauth2.instancio;

import java.util.Map;
import org.instancio.Node;
import org.instancio.generator.Generator;
import org.instancio.generator.GeneratorSpec;
import org.instancio.generators.Generators;
import org.instancio.spi.InstancioServiceProvider;

public class StringObjectMapProviderImpl implements InstancioServiceProvider {

  @Override
  public GeneratorProvider getGeneratorProvider() {
    return new StringObjectMapGeneratorImpl();
  }

  public static class StringObjectMapGeneratorImpl implements GeneratorProvider {

    @Override
    public GeneratorSpec<?> getGenerator(Node node, Generators gen) {
      Class<?> targetClass = node.getTargetClass();
      Class<?> parentClass = node.getParent() != null ? node.getParent().getTargetClass() : null;

      if (targetClass == Object.class  && parentClass == Map.class ) {
        return randomObject();
      }

      return null;
    }
  }

  private static Generator<Object> randomObject() {
    return random -> {
      var idx = random.intRange(0, 2);

      return switch (idx) {
        case 0 -> random.trueOrFalse();
        case 1 -> random.upperCaseAlphabetic(5);
        case 2 -> random.intRange(1, 100);
        default -> null;
      };
    };
  }

}
