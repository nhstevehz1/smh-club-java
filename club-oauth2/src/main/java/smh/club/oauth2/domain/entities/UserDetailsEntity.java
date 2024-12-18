package smh.club.oauth2.domain.entities;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users", schema = "auth",
  indexes =
    {@Index(name = "idx_users_username", columnList = "username", unique = true)})
public class UserDetailsEntity implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(nullable = false, unique = true, length = 20)
  private String username;

  @Column(nullable = false, unique = true, length = 20)
  private String password;

  @Builder.Default
  private boolean accountNonExpired = true;

  @Builder.Default
  private boolean accountNonLocked = true;

  @Builder.Default
  private boolean credentialsNonExpired = true;

  @Builder.Default
  private boolean enabled = false;

  @Builder.Default
  @OneToMany(
      mappedBy = "userDetails",
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER,
      orphanRemoval = true)
  private Set<GrantedAuthorityEntity> authorities = new HashSet<>();

  public void addGrantedAuthority(GrantedAuthorityEntity grantedAuthority) {
    grantedAuthority.setUserDetails(this);
    authorities.add(grantedAuthority);
  }

  public void removeGrantedAuthority(GrantedAuthorityEntity grantedAuthority) {
    authorities.remove(grantedAuthority);
    grantedAuthority.setUserDetails(null);
  }
}
