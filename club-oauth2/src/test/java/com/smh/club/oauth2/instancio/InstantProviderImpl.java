package com.smh.club.oauth2.instancio;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import org.instancio.Node;
import org.instancio.generator.GeneratorSpec;
import org.instancio.generators.Generators;
import org.instancio.spi.InstancioServiceProvider;

public class InstantProviderImpl implements InstancioServiceProvider {/**/

  @Override
  public GeneratorProvider getGeneratorProvider() {
    return new InstantGeneratorImpl();
  }

  public static class InstantGeneratorImpl implements GeneratorProvider {
    @Override
    public GeneratorSpec<?> getGenerator(Node node, Generators gen) {

      var targetClass = node.getTargetClass();
      if (targetClass == Instant.class) {
        return gen.temporal().instant().truncatedTo(ChronoUnit.MICROS);
      }
      return null;
    }
  }
}
