package smh.club.oauth2.domain.entities;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import smh.club.oauth2.domain.converters.StringObjectMapConverter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name ="client", schema = "auth")
public class ClientEntity {

  @Id
  @Column(nullable = false, length = 50)
  private String id;

  @Column(nullable = false, length = 50)
  private String clientId;

  private Instant clientIdIssuedAt;

  @Column(length = 100)
  private String clientSecret;

  private Instant clientSecretExpiresAt;

  @Column(nullable = false, length = 50)
  private String clientName;

  @Builder.Default
  @Convert(converter = StringObjectMapConverter.class)
  @Column(nullable = false)
  private Map<String, Object> clientSettings = new HashMap<>();

  @Builder.Default
  @Convert(converter = StringObjectMapConverter.class)
  @Column(nullable = false)
  private Map<String, Object> tokenSettings  = new HashMap<>();

  @Builder.Default
  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "client_auth_methods_set", schema = "auth",
      joinColumns = {@JoinColumn(name = "client_id", referencedColumnName = "id")})
  @Column(name = "auth_method", length = 30)
  private Set<ClientAuthenticationMethod> clientAuthenticationMethods = new HashSet<>();

  @Builder.Default
  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "client_grant_types_set", schema = "auth",
      joinColumns = {@JoinColumn(name = "client_id", referencedColumnName = "id")})
  @Column(name = "grant_type", length = 30)
  private Set<AuthorizationGrantType> authorizationGrantTypes = new HashSet<>();

  @Builder.Default
  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "client_redirect_uri_set", schema = "auth",
      joinColumns = {@JoinColumn(name = "client_id", referencedColumnName = "id")})
  @Column(name = "redirect_uri", length = 100)
  private Set<String> redirectUris = new HashSet<>();

  @Builder.Default
  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "client_logout_redirect_uri_set", schema = "auth",
      joinColumns = {@JoinColumn(name = "client_id", referencedColumnName = "id")})
  @Column(name = "redirect_uri", length = 100)
  private Set<String> postLogoutRedirectUris = new HashSet<>();

  @Builder.Default
  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "client_scopes_set", schema = "auth",
      joinColumns = {@JoinColumn(name = "client_id", referencedColumnName = "id")})
  @Column(name = "`scope`", length = 30)
  private Set<String> scopes = new HashSet<>();

}
