package smh.club.oauth2.services;

import org.instancio.junit.InstancioExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import smh.club.oauth2.contracts.AuthorizationMapper;
import smh.club.oauth2.domain.repos.AuthorizationConsentRepository;
import smh.club.oauth2.domain.repos.TokenRepository;

@ExtendWith(MockitoExtension.class)
@ExtendWith(InstancioExtension.class)
public class AuthorizationServiceTests {

  @Mock
  private AuthorizationConsentRepository authRepo;

  @Mock
  private RegisteredClientRepository clientRepo;

  @Mock
  private TokenRepository tokenRepo;

  @Mock
  private AuthorizationMapper mapper;

  @InjectMocks
  private JpaOAuth2AuthorizationService service;

  @BeforeEach
  void setUp() {

  }
}
