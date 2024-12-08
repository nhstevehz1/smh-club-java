package smh.club.oauth2.dev;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.htmlunit.WebClient;
import org.htmlunit.WebResponse;
import org.htmlunit.html.DomElement;
import org.htmlunit.html.HtmlCheckBoxInput;
import org.htmlunit.html.HtmlPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.util.UriComponentsBuilder;

import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ActiveProfiles("tests")
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureEmbeddedDatabase(
    provider = ZONKY,
    type = AutoConfigureEmbeddedDatabase.DatabaseType.POSTGRES,
    refresh = AutoConfigureEmbeddedDatabase.RefreshMode.AFTER_EACH_TEST_METHOD)
public class DevAuthorizationServerConsentTests {
  @Autowired
  private WebClient webClient;

  @Autowired
  private RegisteredClientRepository registeredClientRepository;

  @MockitoBean
  private OAuth2AuthorizationConsentService authorizationConsentService;

  private final String redirectUri = "http://127.0.0.1/login/oauth2/code/messaging-client-oidc";

  private final String authorizationRequestUri = UriComponentsBuilder
      .fromPath("/oauth2/authorize")
      .queryParam("response_type", "code")
      .queryParam("client_id", "messaging-client")
      .queryParam("scope", "openid message.read message.write")
      .queryParam("state", "state")
      .queryParam("redirect_uri", this.redirectUri)
      .toUriString();

  @BeforeEach
  public void setUp() {
    this.webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
    this.webClient.getOptions().setRedirectEnabled(true);
    this.webClient.getCookieManager().clearCookies();
    when(this.authorizationConsentService.findById(any(), any())).thenReturn(null);

    addClientToDb();
  }

  @Test
  @WithMockUser("user1")
  public void whenUserConsentsToAllScopesThenReturnAuthorizationCode() throws IOException {
    final HtmlPage consentPage = this.webClient.getPage(this.authorizationRequestUri);
    assertThat(consentPage.getTitleText()).isEqualTo("Consent required");

    List<HtmlCheckBoxInput> scopes = new ArrayList<>();
    consentPage.querySelectorAll("input[name='scope']").forEach(scope ->
        scopes.add((HtmlCheckBoxInput) scope));
    for (HtmlCheckBoxInput scope : scopes) {
      scope.click();
    }

    List<String> scopeIds = new ArrayList<>();
    scopes.forEach(scope -> {
      assertThat(scope.isChecked()).isTrue();
      scopeIds.add(scope.getId());
    });
    assertThat(scopeIds).containsExactlyInAnyOrder("message.read", "message.write");

    DomElement submitConsentButton = consentPage.querySelector("button[id='submit-consent']");
    this.webClient.getOptions().setRedirectEnabled(false);

    WebResponse approveConsentResponse = submitConsentButton.click().getWebResponse();
    assertThat(approveConsentResponse.getStatusCode()).isEqualTo(HttpStatus.MOVED_PERMANENTLY.value());
    String location = approveConsentResponse.getResponseHeaderValue("location");
    assertThat(location).startsWith(this.redirectUri);
    assertThat(location).contains("code=");
  }

  @Test
  @WithMockUser("user1")
  public void whenUserCancelsConsentThenReturnAccessDeniedError() throws IOException {
    final HtmlPage consentPage = this.webClient.getPage(this.authorizationRequestUri);
    assertThat(consentPage.getTitleText()).isEqualTo("Consent required");

    DomElement cancelConsentButton = consentPage.querySelector("button[id='cancel-consent']");
    this.webClient.getOptions().setRedirectEnabled(false);

    WebResponse cancelConsentResponse = cancelConsentButton.click().getWebResponse();
    assertThat(cancelConsentResponse.getStatusCode()).isEqualTo(HttpStatus.MOVED_PERMANENTLY.value());
    String location = cancelConsentResponse.getResponseHeaderValue("location");
    assertThat(location).startsWith(this.redirectUri);
    assertThat(location).contains("error=access_denied");
  }

  private void addClientToDb() {
    RegisteredClient oidcClient = RegisteredClient.withId(UUID.randomUUID().toString())
        .clientId("messaging-client")
        .clientSecret("{noop}secret")
        .clientIdIssuedAt(Instant.now())
        .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
        .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
        .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
        .redirectUri("http://127.0.0.1:8080/login/oauth2/code/messaging-client-oidc")
        .postLogoutRedirectUri("http://127.0.0.1:8080/logged-out")
        .scope(OidcScopes.OPENID)
        .scope(OidcScopes.PROFILE)
        .scope("message.read")
        .scope("message.write")
        .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
        .build();

    registeredClientRepository.save(oidcClient);
  }
}
