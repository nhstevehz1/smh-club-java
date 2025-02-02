import {ComponentFixture, TestBed} from '@angular/core/testing';

import {UserComponent} from './user.component';
import {OAuthService} from "angular-oauth2-oidc";
import {AuthService} from "../../services/auth.service";

describe('UserComponent', () => {
  let component: UserComponent;
  let fixture: ComponentFixture<UserComponent>;
  let authServiceMock: jasmine.SpyObj<AuthService>;
  let methods = [
    'getGivenName', 'getEmail', 'getRoles', 'getIdToken',
    'getAccessToken', 'getRefreshToken'
  ];

  beforeEach(async () => {
    authServiceMock = jasmine.createSpyObj('AuthService', methods);
    await TestBed.configureTestingModule({
      imports: [UserComponent],
      providers: [
        {provide: OAuthService, useValue: {}}
      ]
    }).overrideComponent(UserComponent,
        {set: {providers: [{provide: AuthService, useValue: authServiceMock}]}       }
    ).compileComponents();

    fixture = TestBed.createComponent(UserComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
