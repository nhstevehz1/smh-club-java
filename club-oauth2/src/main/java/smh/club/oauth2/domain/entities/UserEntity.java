package smh.club.oauth2.domain.entities;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users", schema = "auth",
  indexes =
    {@Index(name = "idx_users_username", columnList = "username", unique = true)})
public class UserEntity implements UserDetails {

  @Id
  @Column(name = "username", nullable = false, unique = true, length = 20)
  private String username;

  @Column(name = "password", nullable = false, unique = true, length = 20)
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
  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "user_authorities", schema = "auth",
    joinColumns =
        {@JoinColumn(name = "username", referencedColumnName = "username")},
    indexes =
        {@Index(name = "idx_users_username", columnList = "username, authority", unique = true)})
  @Column(name = "authority", nullable = false, length = 20)
  private Set<SimpleGrantedAuthority> authorities = new HashSet<>();

}
