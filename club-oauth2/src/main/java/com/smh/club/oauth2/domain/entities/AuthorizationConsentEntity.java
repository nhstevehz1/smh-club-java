package com.smh.club.oauth2.domain.entities;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "authorization_consent", schema = "auth")
@IdClass(AuthorizationConsentEntity.AuthorizationConsentId.class)
public class AuthorizationConsentEntity {

  @Id
  @Column(name = "registered_client_id",nullable = false, length = 50)
  private String registeredClientId;

  @Id
  @Column(name = "principal_name", nullable = false, length = 50)
  private String principalName;

  @Builder.Default
  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "authorization_consent_authorities_set", schema = "auth",
      joinColumns = {
        @JoinColumn(name = "registered_client_id", referencedColumnName = "registered_client_id"),
        @JoinColumn(name = "principal_name", referencedColumnName = "principal_name")})
  @Column(name = "authority", length = 100)
  private Set<String> authorities = new HashSet<>();

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class AuthorizationConsentId implements Serializable {
    private String registeredClientId;
    private String principalName;
  }
}
