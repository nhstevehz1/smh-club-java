package com.smh.club.api.hateoas.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@NoArgsConstructor
@Getter
@Configuration
@ConfigurationProperties(prefix = "user.roles")
public class UserRoles {
  private String admin = "admin";
  private String user = "user";

  public void setAdmin(String admin) {
    if (admin == null || admin.isEmpty()) {
      throw new IllegalArgumentException("admin cannot be null or empty");
    }
    this.admin = admin;
  }

  public void setUser(String user) {
    if (user == null || user.isEmpty()) {
      throw new IllegalArgumentException("user cannot be null or empty");
    }
    this.user = user;
  }
}
