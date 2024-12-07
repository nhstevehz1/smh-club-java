package smh.club.oauth2.domain.entities;

import jakarta.persistence.*;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "`authorization_consent`", schema = "`auth`")
@IdClass(AuthorizationConsent.AuthorizationConsentId.class)
public class AuthorizationConsent {
  @Id
  private String registeredClientId;
  @Id
  private String principalName;
  @Column(length = 1000)
  private String authorities;

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class AuthorizationConsentId implements Serializable {
    private String registeredClientId;
    private String principalName;
  }
}
