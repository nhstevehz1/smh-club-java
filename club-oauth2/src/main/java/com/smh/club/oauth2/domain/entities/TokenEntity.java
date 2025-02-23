package com.smh.club.oauth2.domain.entities;

import com.smh.club.oauth2.domain.models.TokenType;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "authorization_token", schema = "auth")
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

  @Column(nullable = false, length = 1000)
  private String metadata;

  @Column(length = 1000)
  private String claims;

  @Builder.Default
  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "token_scopes_set", schema = "auth",
      joinColumns = {@JoinColumn(name = "auth_id", referencedColumnName = "id")})
  @Column(name = "scope", nullable = false, length = 30)
  private Set<String> scopes = new HashSet<>();

  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "auth_id", referencedColumnName ="id", nullable = false)
  private AuthorizationEntity authorization;
}
