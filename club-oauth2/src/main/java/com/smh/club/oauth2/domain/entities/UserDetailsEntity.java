package com.smh.club.oauth2.domain.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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

  @Column(nullable = false, length = 30)
  private String firstName;

  @Column(nullable = false, length = 25)
  private String middleName;

  @Column(nullable = false, length = 30)
  private String lastName;

  @Column(nullable = false, length = 40)
  private String email;

  @Builder.Default
  private boolean accountNonExpired = true;

  @Builder.Default
  private boolean accountNonLocked = true;

  @Builder.Default
  private boolean credentialsNonExpired = true;

  @Builder.Default
  private boolean enabled = false;

  @JsonManagedReference
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

  public void removeGrantedAuthorityById(long grantedAuthorityId) {
    var auth = authorities.stream()
        .filter(ga -> ga.getId() == grantedAuthorityId)
        .findFirst();

    auth.ifPresent(ga -> {
      authorities.remove(ga);
      ga.setUserDetails(null);
    });
  }

  public void removeGrantedAuthority(GrantedAuthorityEntity grantedAuthority) {
    authorities.remove(grantedAuthority);
    grantedAuthority.setUserDetails(null);
  }
}
