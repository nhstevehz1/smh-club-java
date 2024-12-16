package smh.club.oauth2.domain.entities;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "`authorization`", schema = "`auth`")
public class AuthorizationEntity {

  @Id
  @Column(nullable = false, length = 30)
  private String id;

  @Column(nullable = false, length = 30)
  private String registeredClientId;

  @Column(nullable = false, length = 30)
  private String principalName;

  private AuthorizationGrantType authorizationGrantType;

  @Column(length = 500)
  private String state;

  @Column(length = 4000)
  private String attributes;

  @Builder.Default
  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "authorization_scopes_set", schema = "auth",
      joinColumns = {@JoinColumn(name = "auth_id", nullable = false,
          referencedColumnName = "id")})
  @Column(name = "scope", nullable = false, length = 30)
  private Set<String> authorizedScopes = new HashSet<>();

  @Builder.Default
  @OneToMany(mappedBy = "authorization",
      orphanRemoval = true,
      cascade = {CascadeType.ALL},
      fetch = FetchType.EAGER)
  private Set<TokenEntity> tokens = new HashSet<>();

  public void addTokenEntity(TokenEntity tokenEntity) {
    tokenEntity.setAuthorization(this);
    this.tokens.add(tokenEntity);
  }
}
