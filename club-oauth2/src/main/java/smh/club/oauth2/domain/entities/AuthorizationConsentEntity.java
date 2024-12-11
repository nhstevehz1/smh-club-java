package smh.club.oauth2.domain.entities;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "`authorization_consent`", schema = "`auth`")
@IdClass(AuthorizationConsentEntity.AuthorizationConsentId.class)
public class AuthorizationConsentEntity {

  @Id
  private String registeredClientId;

  @Id
  private String principalName;

  @Builder.Default
  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "authorization_consent_authorities_set", schema = "auth",
      joinColumns = {
        @JoinColumn(name = "registered_client_id", referencedColumnName = "registered_client_id"),
        @JoinColumn(name = "principal_name", referencedColumnName = "principal_name")})
  @Column(name = "authority", length = 100)
  private Set<SimpleGrantedAuthority> authorities = new HashSet<>();

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class AuthorizationConsentId {
    private String registeredClientId;
    private String principalName;
  }
}
