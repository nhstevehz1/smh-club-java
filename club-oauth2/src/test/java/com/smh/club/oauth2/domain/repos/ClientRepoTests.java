package com.smh.club.oauth2.domain.repos;

import com.smh.club.oauth2.domain.entities.ClientEntity;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import jakarta.transaction.Transactional;
import java.util.Set;
import org.instancio.Instancio;
import org.instancio.junit.InstancioExtension;
import org.instancio.junit.WithSettings;
import org.instancio.settings.Keys;
import org.instancio.settings.Settings;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.test.context.ActiveProfiles;

import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY;
import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("tests, prod")
@Transactional
@ExtendWith(InstancioExtension.class)
@DataJpaTest
@AutoConfigureEmbeddedDatabase(
    provider = ZONKY,
    type = AutoConfigureEmbeddedDatabase.DatabaseType.POSTGRES,
    refresh = AutoConfigureEmbeddedDatabase.RefreshMode.AFTER_EACH_TEST_METHOD)
public class ClientRepoTests {

  @Autowired
  private ClientRepository repo;

  @WithSettings
  Settings settings =
      Settings.create()
          .set(Keys.SET_BACK_REFERENCES, true)
          .set(Keys.JPA_ENABLED, true);

  @Test
  public void find_by_client_id() {
    // setup
    var grantTypes = Set.of(AuthorizationGrantType.AUTHORIZATION_CODE, AuthorizationGrantType.CLIENT_CREDENTIALS);
    var authMethods = Set.of(ClientAuthenticationMethod.CLIENT_SECRET_BASIC, ClientAuthenticationMethod.CLIENT_SECRET_POST);

    var list = Instancio.ofList(ClientEntity.class)
        .size(5)
        .set(field(ClientEntity::getAuthorizationGrantTypes), grantTypes)
        .set(field(ClientEntity::getClientAuthenticationMethods), authMethods)
        .create();

    var expected = list.get(3);

    // execute
    repo.saveAllAndFlush(list);
    var optional = repo.findByClientId(expected.getClientId());

    // assert
    assertTrue(optional.isPresent());
    var actual = optional.get();
    assertEquals(expected.getId(), actual.getId());
    assertEquals(expected.getClientId(), actual.getClientId());
    assertEquals(expected.getClientIdIssuedAt(), actual.getClientIdIssuedAt());
    assertEquals(expected.getClientSecret(), actual.getClientSecret());
    assertEquals(expected.getClientSecretExpiresAt(), actual.getClientSecretExpiresAt());
    assertEquals(actual.getClientSettings(), expected.getClientSettings());
    assertEquals(actual.getTokenSettings(), expected.getTokenSettings());
    assertEquals(actual.getClientAuthenticationMethods(), expected.getClientAuthenticationMethods());
    assertEquals(actual.getAuthorizationGrantTypes(), expected.getAuthorizationGrantTypes());
    assertEquals(actual.getClientAuthenticationMethods(), expected.getClientAuthenticationMethods());
    assertEquals(actual.getRedirectUris(), expected.getRedirectUris());
    assertEquals(actual.getPostLogoutRedirectUris(), expected.getPostLogoutRedirectUris());
    assertEquals(actual.getScopes(), expected.getScopes());
  }
}
