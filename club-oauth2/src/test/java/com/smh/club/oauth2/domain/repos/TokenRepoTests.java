package com.smh.club.oauth2.domain.repos;

import com.smh.club.oauth2.domain.entities.AuthorizationEntity;
import com.smh.club.oauth2.domain.entities.TokenEntity;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import jakarta.transaction.Transactional;
import org.instancio.Instancio;
import org.instancio.junit.InstancioExtension;
import org.instancio.junit.WithSettings;
import org.instancio.settings.Keys;
import org.instancio.settings.Settings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY;
import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("tests")
@Transactional
@ExtendWith(InstancioExtension.class)
@DataJpaTest
@AutoConfigureEmbeddedDatabase(
    provider = ZONKY,
    type = AutoConfigureEmbeddedDatabase.DatabaseType.POSTGRES,
    refresh = AutoConfigureEmbeddedDatabase.RefreshMode.AFTER_EACH_TEST_METHOD)
public class TokenRepoTests {

  @Autowired
  private AuthorizationRepository authRepo;

  @Autowired
  private TokenRepository repo;

  private String authId;

  @WithSettings
  Settings settings =
      Settings.create()
          .set(Keys.SET_BACK_REFERENCES, true)
          .set(Keys.JPA_ENABLED, true);

  @BeforeEach
  public void setup() {
    var entity = Instancio.ofList(AuthorizationEntity.class)
        .size(5)
        .withUnique(field(AuthorizationEntity::getId))
        .generate(field(AuthorizationEntity::getTokens), g -> g.collection().size(0) )
        .create();

    authId = authRepo.saveAllAndFlush(entity).get(3).getId();
  }

  @Test
  public void find_by_token_type_and_token_value() {
    // setup
    var auth = authRepo.findById(authId).orElseThrow();

    var entities = Instancio.ofList(TokenEntity.class)
        .size(5)
        .withUnique(field(TokenEntity::getTokenType))
        .withUnique(field(TokenEntity::getTokenValue))
        .set(field(TokenEntity::getAuthorization), auth)
        .create();
    var expected = entities.get(3);

    // execute
    repo.saveAllAndFlush(entities);
    var optional = repo.findByTokenTypeAndTokenValue(expected.getTokenType(), expected.getTokenValue());

    assertTrue(optional.isPresent());
    assertEquals(expected, optional.get());
  }

  @Test
  public void find_by_token_value() {
    // setup
    var auth = authRepo.findById(authId).orElseThrow();
    var entities = Instancio.ofList(TokenEntity.class)
        .size(5)
        .withUnique(field(TokenEntity::getTokenType))
        .withUnique(field(TokenEntity::getTokenValue))
        .set(field(TokenEntity::getAuthorization), auth)
        .create();

    var expected = entities.get(3);

    // execute
    repo.saveAllAndFlush(entities);
    var optional = repo.findByTokenValue(expected.getTokenValue());
    assertTrue(optional.isPresent());
    assertEquals(expected, optional.get());
  }

}
