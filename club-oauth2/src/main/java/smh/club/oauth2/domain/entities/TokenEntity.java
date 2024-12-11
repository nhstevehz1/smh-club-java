package smh.club.oauth2.domain.entities;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.*;
import lombok.*;
import smh.club.oauth2.domain.models.TokenType;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "authorization_token", schema = "`auth`")
@IdClass(TokenEntity.TokenTypeId.class)
public class TokenEntity {

  @Id
  @Column(nullable = false, length = 30)
  private UUID id;

  @Id
  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 30)
  private TokenType tokenType;

  @Column(nullable = false, length = 200)
  private String tokenValue;

  @Column(nullable = false)
  private Instant issuedAt;

  private Instant expiresAt;

  @Builder.Default
  @ElementCollection
  @CollectionTable(name = "token_metadata_map", schema = "auth",
      joinColumns = {@JoinColumn(name = "token_id", referencedColumnName = "id")})
  @MapKeyColumn(name = "metadata_name", length = 30)
  @Column(name = "metadata", nullable = false, length = 100)
  private Map<String, String> metadata = new HashMap<>();

  @Builder.Default
  @ElementCollection
  @CollectionTable(name = "token_scopes_set", schema = "auth",
      joinColumns = {@JoinColumn(name = "token_id", referencedColumnName = "id")})
  @Column(name = "`scope`", nullable = false, length = 30)
  private Set<String> scopes = new HashSet<>();

  @Builder.Default
  @ElementCollection
  @CollectionTable(name = "token_claims_map", schema = "auth",
      joinColumns = {@JoinColumn(name = "token_id", referencedColumnName = "id")})
  @MapKeyColumn(name = "claim_name", length = 30)
  @Column(name = "claim", nullable = false, length = 100)
  private Map<String, String> claims = new HashMap<>();

  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "auth_id", referencedColumnName = "id", nullable = false)
  private AuthorizationEntity authorization;

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class TokenTypeId {
    private UUID id;
    private TokenType tokenType;
  }
}
