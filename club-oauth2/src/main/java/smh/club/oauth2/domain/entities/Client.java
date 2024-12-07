package smh.club.oauth2.domain.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "`client`", schema = "`auth`")
public class Client {
  @Id
  @Column(nullable = false, unique = true)
  private String id;

  @Column(nullable = false)
  private String clientId;

  @Builder.Default
  @Column(nullable = false)
  private Instant clientIdIssuedAt = Instant.now();

  private String clientSecret;

  private Instant clientSecretExpiresAt;

  @Column(nullable = false)
  private String clientName;

  @Column(nullable = false, length = 1000)
  private String clientAuthenticationMethods;

  @Column(nullable = false, length = 1000)
  private String authorizationGrantTypes;

  @Column(length = 1000)
  private String redirectUris;

  @Column(length = 1000)
  private String postLogoutRedirectUris;

  @Column(nullable = false, length = 1000)
  private String scopes;

  @Column(nullable = false, length = 2000)
  private String clientSettings;

  @Column(nullable = false, length = 2000)
  private String tokenSettings;

}
