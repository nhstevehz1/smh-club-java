package com.smh.club.oauth2.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PasswordChangeResponse {

  private boolean passwordMatches;
  private boolean userFound;

  public static PasswordChangeResponse noUserMatch(){
    return new PasswordChangeResponse(false, false);
  }

  public static PasswordChangeResponse success() {
    return new PasswordChangeResponse(true, true);
  }

  public static PasswordChangeResponse noPasswordMatch() {
    return new PasswordChangeResponse(false, true);
  }
}
