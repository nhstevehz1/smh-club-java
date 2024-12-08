package smh.club.oauth2.config.dev;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import smh.club.oauth2.domain.repos.ClientRepository;
import smh.club.oauth2.domain.repos.UserRepository;
import smh.club.oauth2.security.JpaRegisteredClientRepository;
import smh.club.oauth2.security.JpaUserDetailsManager;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Profile("!default-config && !default-props")
@Configuration
public class AuthorizationConfig {

  private final ClientRepository clientRepository;
  private final UserRepository userRepository;


  /**
   * Persistent registered client storage.
   */
  @Bean
  public RegisteredClientRepository registeredClientRepository() {
    return new JpaRegisteredClientRepository(clientRepository);
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
