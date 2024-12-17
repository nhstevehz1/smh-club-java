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
@Table(name = "authorization_token", schema = "auth",
  uniqueConstraints = {
      @UniqueConstraint(columnNames = {"token_value"}),
      @UniqueConstraint(columnNames = {"auth_id", "token_type"})})
public class TokenEntity {

  @Builder.Default
  @Id
  @Column(nullable = false, length = 50)
  private String id = UUID.randomUUID().toString();

  @Enumerated(EnumType.STRING)
  @Column(name = "token_type", nullable = false)
  private TokenType tokenType;

  @Column(name = "token_value", nullable = false, length = 200)
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
      joinColumns = {@JoinColumn(name = "auth_id", referencedColumnName = "auth_id"),
                      @JoinColumn(name = "token_type", referencedColumnName = "token_type")})
  @Column(name = "scope", nullable = false, length = 30)
  private Set<String> scopes = new HashSet<>();

  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "auth_id", referencedColumnName ="id", nullable = false)
  private AuthorizationEntity authorization;
}
