package smh.club.oauth2.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import smh.club.oauth2.domain.entities.UserEntity;
import smh.club.oauth2.domain.repos.UserRepository;

@RequiredArgsConstructor
@Profile("prod")
@Transactional
@Service
public class JpaUserDetailsManager implements UserDetailsManager {

  private final UserRepository userRepository;

  @Override
  public void createUser(UserDetails user) {
    userRepository.save((UserEntity) user);
  }

  @Override
  public void updateUser(UserDetails user) {
    userRepository.save((UserEntity) user);
  }

  @Override
  public void deleteUser(String username) {
    userRepository.deleteByUsername(username);
  }

  @Override
  public void changePassword(String oldPassword, String newPassword) {
    UserEntity userDetails = userRepository.findByPassword(oldPassword)
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
