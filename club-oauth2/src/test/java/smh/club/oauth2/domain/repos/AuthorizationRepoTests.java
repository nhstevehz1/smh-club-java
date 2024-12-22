package smh.club.oauth2.domain.repos;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import jakarta.transaction.Transactional;
import org.instancio.Instancio;
import org.instancio.junit.InstancioExtension;
import org.instancio.junit.WithSettings;
import org.instancio.settings.Keys;
import org.instancio.settings.Settings;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import smh.club.oauth2.domain.entities.AuthorizationEntity;
import smh.club.oauth2.domain.entities.TokenEntity;

import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY;
import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("tests")
@Transactional
@ExtendWith(InstancioExtension.class)
// Can't use @DataJpaTest due to some converters need an injected ObjectMapper
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureEmbeddedDatabase(
    provider = ZONKY,
    type = AutoConfigureEmbeddedDatabase.DatabaseType.POSTGRES,
    refresh = AutoConfigureEmbeddedDatabase.RefreshMode.AFTER_EACH_TEST_METHOD)
public class AuthorizationRepoTests {

  @Autowired
  private AuthorizationRepository repo;

  @WithSettings
  Settings settings =
      Settings.create()
          .set(Keys.SET_BACK_REFERENCES, true)
          .set(Keys.JPA_ENABLED, true);

  @Test
  public void find_by_state() {
    // setup
    var list = Instancio.ofList(AuthorizationEntity.class)
        .size(5)
        .generate(field(AuthorizationEntity::getId), g -> g.string().upperCase().length(10))
        .generate(field(AuthorizationEntity::getTokens), g -> g.collection().size(0))
        .create();

    list.forEach(a -> {
      var tokens = Instancio.ofList(TokenEntity.class)
          .withUnique(field(TokenEntity::getId))
          .ignore(field(TokenEntity::getAuthorization))
          .withUnique(field(TokenEntity::getTokenType))
          .withUnique(field(TokenEntity::getTokenValue))
          .create();
      tokens.forEach(a::addTokenEntity);
    });

    var expected = list.get(3);
    repo.saveAllAndFlush(list);

    // execute
    var entity =  repo.findByState(expected.getState());

    assertTrue(entity.isPresent());
    var actual = entity.get();
    assertEquals(expected.getId(), actual.getId());
    assertEquals(expected.getState(), actual.getState());
    assertEquals(expected.getRegisteredClientId(), actual.getRegisteredClientId());
    assertEquals(expected.getAuthorizationGrantType(), actual.getAuthorizationGrantType());
    assertEquals(expected.getPrincipalName(), actual.getPrincipalName());
    assertEquals(expected.getAuthorizedScopes(), actual.getAuthorizedScopes());
    assertEquals(expected.getAttributes(), actual.getAttributes());
    assertEquals(expected.getTokens(), actual.getTokens());
  }

  @Test
  public void find_by_token_type_and_token_value() {
    // setup
    var list = Instancio.ofList(AuthorizationEntity.class)
        .size(5)
        .generate(field(AuthorizationEntity::getId), g -> g.string().upperCase().length(10))
        .generate(field(AuthorizationEntity::getTokens), g -> g.collection().size(0))
        .create();

    list.forEach(a -> {
      var tokens = Instancio.ofList(TokenEntity.class)
          .withUnique(field(TokenEntity::getId))
          .ignore(field(TokenEntity::getAuthorization))
          .withUnique(field(TokenEntity::getTokenType))
          .withUnique(field(TokenEntity::getTokenValue))
          .create();
      tokens.forEach(a::addTokenEntity);
    });

    var expected = list.get(3);
    repo.saveAllAndFlush(list);

    // execute
    for (TokenEntity token : expected.getTokens()) {
      var auth = repo.findByTokensTokenTypeAndTokensTokenValue(token.getTokenType(), token.getTokenValue());

      assertTrue(auth.isPresent());
      assertEquals(expected, auth.get());
    }

  }

  @Test
  public void find_by_token_value() {
    // setup
    var list = Instancio.ofList(AuthorizationEntity.class)
        .size(5)
        .generate(field(AuthorizationEntity::getId), g -> g.string().upperCase().length(10))
        .generate(field(AuthorizationEntity::getTokens), g -> g.collection().size(0))
        .create();

    list.forEach(a -> {
      var tokens = Instancio.ofList(TokenEntity.class)
          .withUnique(field(TokenEntity::getId))
          .ignore(field(TokenEntity::getAuthorization))
          .withUnique(field(TokenEntity::getTokenType))
          .withUnique(field(TokenEntity::getTokenValue))
          .create();
      tokens.forEach(a::addTokenEntity);
    });

    var expected = list.get(3);
    repo.saveAllAndFlush(list);

    // execute
    for (TokenEntity token : expected.getTokens()) {
      var auth = repo.findByTokensTokenValue(token.getTokenValue());

      assertTrue(auth.isPresent());
      assertEquals(expected, auth.get());
    }
  }

}
