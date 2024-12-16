package smh.club.oauth2.config;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import java.io.IOException;
import java.time.Instant;
import java.util.UUID;
import org.htmlunit.Page;
import org.htmlunit.WebClient;
import org.htmlunit.WebResponse;
import org.htmlunit.html.HtmlButton;
import org.htmlunit.html.HtmlElement;
import org.htmlunit.html.HtmlInput;
import org.htmlunit.html.HtmlPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.util.UriComponentsBuilder;
import smh.club.oauth2.domain.entities.UserEntity;
import smh.club.oauth2.domain.repos.UserRepository;

import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles({"tests", "prod"})
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureEmbeddedDatabase(
    provider = ZONKY,
    type = AutoConfigureEmbeddedDatabase.DatabaseType.POSTGRES,
    refresh = AutoConfigureEmbeddedDatabase.RefreshMode.AFTER_EACH_TEST_METHOD)
public class DevAuthorizationServerApplicationTests {

  @Autowired
  private RegisteredClientRepository registeredClientRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private WebClient webClient;

  private static final String REDIRECT_URI = "http://127.0.0.1:8080/login/oauth2/code/messaging-client-oidc";

  private static final String AUTHORIZATION_REQUEST = UriComponentsBuilder
      .fromPath("/oauth2/authorize")
      .queryParam("response_type", "code")
      .queryParam("client_id", "messaging-client")
      .queryParam("scope", "openid")
      .queryParam("state", "some-state")
      .queryParam("redirect_uri", REDIRECT_URI)
      .toUriString();


  @BeforeEach
  public void setUp() {
    this.webClient.getOptions().setThrowExceptionOnFailingStatusCode(true);
    this.webClient.getOptions().setRedirectEnabled(true);
    this.webClient.getCookieManager().clearCookies();// log out

    setupDatabase();
  }

  @Test
  public void whenLoginSuccessfulThenDisplayNotFoundError() throws IOException {
    HtmlPage page = this.webClient.getPage("/");

    assertLoginPage(page);

    this.webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
    WebResponse signInResponse = signIn(page, "user1", "password").getWebResponse();
    assertThat(signInResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());	// there is no "default" index page
  }

  @Test
  public void whenLoginFailsThenDisplayBadCredentials() throws IOException {
    HtmlPage page = this.webClient.getPage("/");

    HtmlPage loginErrorPage = signIn(page, "user1", "wrong-password");

    HtmlElement alert = loginErrorPage.querySelector("div[role=\"alert\"]");
    assertThat(alert).isNotNull();
    assertThat(alert.getTextContent()).isEqualTo("Bad credentials");
  }

  @Test
  public void whenNotLoggedInAndRequestingTokenThenRedirectsToLogin() throws IOException {
    HtmlPage page = this.webClient.getPage(AUTHORIZATION_REQUEST);

    assertLoginPage(page);
  }

  @Test
  public void whenLoggingInAndRequestingTokenThenRedirectsToClientApplication() throws IOException {
    // Log in
    this.webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
    this.webClient.getOptions().setRedirectEnabled(false);
    signIn(this.webClient.getPage("/login"), "user1", "password");

    // Request token
    WebResponse response = this.webClient.getPage(AUTHORIZATION_REQUEST).getWebResponse();

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.MOVED_PERMANENTLY.value());
    String location = response.getResponseHeaderValue("location");
    assertThat(location).startsWith(REDIRECT_URI);
    assertThat(location).contains("code=");
  }

  private static <P extends Page> P signIn(HtmlPage page, String username, String password) throws IOException {
    HtmlInput usernameInput = page.querySelector("input[name=\"username\"]");
    HtmlInput passwordInput = page.querySelector("input[name=\"password\"]");
    HtmlButton signInButton = page.querySelector("button");

    usernameInput.type(username);
    passwordInput.type(password);
    return signInButton.click();
  }

  private static void assertLoginPage(HtmlPage page) {
    assertThat(page.getUrl().toString()).endsWith("/login");

    HtmlInput usernameInput = page.querySelector("input[name=\"username\"]");
    HtmlInput passwordInput = page.querySelector("input[name=\"password\"]");
    HtmlButton signInButton = page.querySelector("button");

    assertThat(usernameInput).isNotNull();
    assertThat(passwordInput).isNotNull();
    assertThat(signInButton.getTextContent()).isEqualTo("Sign in");
  }

  private void setupDatabase() {
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

    var user = UserEntity.builder()
        .username("user1")
        .password("{noop}password")
        .enabled(true)
        .build();

    user.getAuthorities().add(new SimpleGrantedAuthority("USER"));
    userRepository.save(user);

  }
}
