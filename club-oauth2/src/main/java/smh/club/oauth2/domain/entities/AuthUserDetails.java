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
@Table(name = "`user`", schema = "`auth`")
public class AuthUserDetails implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(nullable = false, unique = true, length = 20)
  private String username;

  @Column(nullable = false, length = 20)
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
  @OneToMany(mappedBy = "authUserDetails",
      orphanRemoval = true,
      cascade = {CascadeType.ALL},
      fetch = FetchType.EAGER)
  private Set<AuthGrantedAuthority> authorities = new HashSet<>();

  public void addAuthority(AuthGrantedAuthority authority) {
    this.authorities.add(authority);
    authority.setAuthUserDetails(this);
  }

  public void removeAuthority(AuthGrantedAuthority authority) {
    authority.setAuthUserDetails(this);
    this.authorities.remove(authority);
  }
}
