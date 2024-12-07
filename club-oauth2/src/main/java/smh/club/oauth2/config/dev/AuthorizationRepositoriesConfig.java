package smh.club.oauth2.config.dev;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import smh.club.oauth2.domain.repos.ClientRepository;
import smh.club.oauth2.security.JpaRegisteredClientRepository;

@Profile("!default-config && !default-props")
@Configuration
public class AuthorizationRepositoriesConfig {

  /**
   * Persistent registered client storage.
   */
  @Bean
  public RegisteredClientRepository registeredClientRepository(ClientRepository clientRepository) {
    return new JpaRegisteredClientRepository(clientRepository);
  }

  /**
   * In-memory UserDetailsService.  TODO: replace with jdbc or jpa repo
   */
  @Bean
  public UserDetailsService userDetailsService() {
    UserDetails userDetails = User.builder()
        .username("user1")
        .password("{noop}password")
        .roles("USER")
        .build();

    return new InMemoryUserDetailsManager(userDetails);
  }
}
