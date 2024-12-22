package smh.club.oauth2.domain.converters;

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
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import smh.club.oauth2.helpers.TestUtils;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(InstancioExtension.class)
public class StringObjectMapConverterTests {

  private StringObjectMapConverter converter;

  @WithSettings
  private final Settings settings =
      Settings.create().set(Keys.JPA_ENABLED, true);

  @BeforeEach
  public void setUp() {

    converter = new StringObjectMapConverter(TestUtils.getObjectMapper());
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
}
