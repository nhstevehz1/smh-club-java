package smh.club.oauth2.config.mapper;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import smh.club.oauth2.domain.entities.UserDetailsEntity;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public abstract class UserEntityMixin {

  @JsonCreator
  public UserEntityMixin(UserDetailsEntity userDetailsEntity) {

  }
}
