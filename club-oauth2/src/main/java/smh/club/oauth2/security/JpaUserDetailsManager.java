package smh.club.oauth2.security;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import smh.club.oauth2.domain.entities.AuthUserDetails;
import smh.club.oauth2.domain.repos.UserRepository;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Transactional
@Service
public class JpaUserDetailsManager implements UserDetailsManager {

  private final UserRepository userRepository;

  @Override
  public void createUser(UserDetails user) {
    userRepository.save((AuthUserDetails) user);
  }

  @Override
  public void updateUser(UserDetails user) {
    userRepository.save((AuthUserDetails) user);
  }

  @Override
  public void deleteUser(String username) {
    userRepository.deleteByUsername(username);
  }

  @Override
  public void changePassword(String oldPassword, String newPassword) {
    AuthUserDetails userDetails = userRepository.findByPassword(oldPassword)
        .orElseThrow(() -> new UsernameNotFoundException("Invalid password"));
    userDetails.setPassword(newPassword);
    userRepository.save(userDetails);
  }

  @Override
  public boolean userExists(String username) {
    return userRepository.existsByUsername(username);
  }

  @Override
  public UserDetails loadUserByUsername(String username) {
    return userRepository.findByUsername(username)
        .orElseThrow(
          () -> new UsernameNotFoundException("No user found with username = " + username));
  }
}
