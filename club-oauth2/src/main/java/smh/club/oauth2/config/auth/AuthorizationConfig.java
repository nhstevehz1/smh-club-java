package smh.club.oauth2.config.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import smh.club.oauth2.contracts.RegisteredClientMapper;
import smh.club.oauth2.domain.repos.ClientRepository;
import smh.club.oauth2.domain.repos.UserRepository;
import smh.club.oauth2.services.JpaRegisteredClientService;
import smh.club.oauth2.services.JpaUserDetailsManager;

@RequiredArgsConstructor
@Profile("prod")
@Configuration
public class AuthorizationConfig {

  private final ClientRepository clientRepository;
  private final UserRepository userRepository;
  private final RegisteredClientMapper clientMapper;


  /**
   * Persistent registered client storage.
   */
  @Bean
  public RegisteredClientRepository registeredClientRepository() {
    return new JpaRegisteredClientService(clientRepository, clientMapper);
  }

  /**
   * Persistent user details storage
   */
  @Bean
  public UserDetailsService jpaUserDetailsService() {
    return new JpaUserDetailsManager(userRepository);
  }

  @Bean
  public DaoAuthenticationProvider jpaDaoAuthenticationProvider() {
    DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
    daoAuthenticationProvider.setUserDetailsService(jpaUserDetailsService());
    daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
    return daoAuthenticationProvider;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }
}
