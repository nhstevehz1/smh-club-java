package smh.club.oauth2.domain.entities;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import lombok.*;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name ="`client`", schema = "`auth`")
public class Client {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false, length = 30)
  private String clientId;

  @Builder.Default
  private Instant clientIdIssuedAt = Instant.now();

  @Column(length = 30)
  private String clientSecret;

  private Instant clientSecretExpiresAt;

  @Column(nullable = false, length = 30)
  private String clientName;


  @Singular("clientAuthenticationMethod")
  @ElementCollection
  @Column(nullable = false)
  private Set<ClientAuthenticationMethod> clientAuthenticationMethods;

  @Singular("authorizationGrantType")
  @ElementCollection
  @Column(nullable = false)
  private Set<AuthorizationGrantType> authorizationGrantTypes;

  @Singular("redirectUri")
  @ElementCollection
  private Set<String> redirectUris;

  @Singular("postLogoutRedirectUri")
  @ElementCollection
  private Set<String> postLogoutRedirectUris;

  @Singular("scope")
  @Column(nullable = false)
  @ElementCollection
  private Set<String> scopes;

  @Column(nullable = false)
  private ClientSettings clientSettings;

  @Column(nullable = false)
  private TokenSettings tokenSettings;

}
