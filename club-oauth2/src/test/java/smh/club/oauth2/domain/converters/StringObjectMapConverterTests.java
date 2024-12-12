package smh.club.oauth2.domain.converters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.Duration;
import java.util.HashMap;
import org.instancio.junit.InstancioExtension;
import org.instancio.junit.WithSettings;
import org.instancio.settings.Keys;
import org.instancio.settings.Settings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.web.jackson2.WebJackson2Module;
import org.springframework.security.web.jackson2.WebServletJackson2Module;
import org.springframework.security.web.server.jackson2.WebServerJackson2Module;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(InstancioExtension.class)
public class StringObjectMapConverterTests {

  private ObjectMapper mapper;
  private StringObjectMapConverter converter;

  @WithSettings
  private final Settings settings =
      Settings.create().set(Keys.JPA_ENABLED, true);

  @BeforeEach
  public void setUp() {
    mapper = new ObjectMapper();
    mapper.registerModule(new WebJackson2Module());
    mapper.registerModule(new WebServletJackson2Module());
    mapper.registerModule(new WebServerJackson2Module());
    mapper.registerModule(new OAuth2AuthorizationServerJackson2Module());
    mapper.registerModule(new JavaTimeModule());

    converter = new StringObjectMapConverter(mapper);
  }

  @Test
  public void clientSettingsTest() {
    // setup
    var map =
        ClientSettings.builder()
        .requireAuthorizationConsent(true)
        .requireProofKey(true)
        .jwkSetUrl("https://example.com")
        .x509CertificateSubjectDN("DN=blah")
        .tokenEndpointAuthenticationSigningAlgorithm(SignatureAlgorithm.RS256)
        .build().getSettings();

    // execute and verify
    var str = converter.convertToDatabaseColumn(map);
    assertNotNull(str);

    var settings = converter.convertToEntityAttribute(str);

    assertNotNull(settings);
    assertTrue(settings.entrySet().containsAll(map.entrySet()));
  }

  @Test
  public void stringObjectMapTest() {
    // setup
    var map = new HashMap<String, Object>();
    map.put("string", "value1");
    map.put("int", 5);
    map.put("boolean", true);

    // execute and verify
    var str = converter.convertToDatabaseColumn(map);
    assertNotNull(str);

    var settings = converter.convertToEntityAttribute(str);

    assertNotNull(settings);
    assertTrue(settings.entrySet().containsAll(map.entrySet()));
  }

  @Test
  public void tokeSettingsTest() {
    // setup
    var map =
        TokenSettings.builder()
            .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
            .accessTokenTimeToLive(Duration.ofDays(1))
            .deviceCodeTimeToLive(Duration.ofDays(1))
            .authorizationCodeTimeToLive(Duration.ofDays(1))
            .idTokenSignatureAlgorithm(SignatureAlgorithm.RS256)
            .reuseRefreshTokens(true)
            .x509CertificateBoundAccessTokens(true)
            .build().getSettings();

    // execute and verify
    var str = converter.convertToDatabaseColumn(map);
    assertNotNull(str);

    var settings = converter.convertToEntityAttribute(str);

    assertNotNull(settings);
    assertTrue(settings.entrySet().containsAll(map.entrySet()));
  }

  @Test
  public void authorizationMetadataTest() {
    // setup
    var rc = RegisteredClient.withId("id").build();
    OAuth2Authorization.withRegisteredClient(rc)
        .attribute("test", true)
        .build();

    var map =
        TokenSettings.builder()
            .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
            .accessTokenTimeToLive(Duration.ofDays(1))
            .deviceCodeTimeToLive(Duration.ofDays(1))
            .authorizationCodeTimeToLive(Duration.ofDays(1))
            .idTokenSignatureAlgorithm(SignatureAlgorithm.RS256)
            .reuseRefreshTokens(true)
            .x509CertificateBoundAccessTokens(true)
            .build().getSettings();

    // execute and verify
    var str = converter.convertToDatabaseColumn(map);
    assertNotNull(str);

    var settings = converter.convertToEntityAttribute(str);

    assertNotNull(settings);
    assertTrue(settings.entrySet().containsAll(map.entrySet()));
  }

}
