package com.smh.club.oauth2.services;

import com.smh.club.oauth2.domain.repos.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@RequiredArgsConstructor
@Service
public class JpaUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Assert.hasText(username, "username cannot be null or empty");

    return userRepository.findByUsername(username)
        .orElseThrow(
            () -> new UsernameNotFoundException("No user found with username = " + username));
  }
}
