package com.smh.club.oauth2.domain.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "user_authorities", schema = "auth",
indexes = {@Index(name = "idx_user_authorities__user_id",
                  columnList = "auth_id,authority")})
public class GrantedAuthorityEntity implements GrantedAuthority {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(nullable = false, length = 20)
  private String authority;

  @JsonBackReference
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "user_id", referencedColumnName ="id", nullable = false)
  private UserDetailsEntity userDetails;

}
