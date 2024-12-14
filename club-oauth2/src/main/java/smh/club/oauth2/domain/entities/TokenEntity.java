package smh.club.oauth2.domain.entities;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.*;
import lombok.*;
import smh.club.oauth2.domain.converters.StringObjectMapConverter;
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
  private String id;

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
  @Convert(converter = StringObjectMapConverter.class)
  @Column(length = 1000)
  private Map<String, Object> metadata = new HashMap<>();

  @Builder.Default
  @Convert(converter = StringObjectMapConverter.class)
  @Column(length = 1000)
  private Map<String, Object> claims = new HashMap<>();

  @Builder.Default
  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "token_scopes_set", schema = "auth",
      joinColumns = {@JoinColumn(name = "token_id", referencedColumnName = "id")})
  @Column(name = "`scope`", nullable = false, length = 30)
  private Set<String> scopes = new HashSet<>();

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
