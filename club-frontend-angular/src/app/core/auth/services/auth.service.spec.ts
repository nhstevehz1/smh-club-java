import {fakeAsync, TestBed, tick} from '@angular/core/testing';

import {AuthService} from './auth.service';
import {provideHttpClient} from "@angular/common/http";
import {provideHttpClientTesting} from "@angular/common/http/testing";
import {OAuthEvent, OAuthService, OAuthSuccessEvent, EventType} from "angular-oauth2-oidc";
import {of, Subject} from "rxjs";
import {authCodeFlowConfig} from "../../../auth.config";
import {jwtDecode} from "jwt-decode";
import {RealmAccess} from "../models/realm-access";

describe('AuthService', () => {
  let service: AuthService;
  let oAuthServiceMock: jasmine.SpyObj<OAuthService>;
  const methods = [
        'configure', 'loadDiscoveryDocumentAndLogin', 'setupAutomaticSilentRefresh',
        'loadUserProfile', 'events', 'getAccessToken', 'getAccessToken', 'getIdentityClaims',
      'logOut', 'hasValidIdToken'
  ];
  const tokenReceivedEvent = new OAuthSuccessEvent('token_received')
  let oauthSubject = new Subject<OAuthEvent>();
  let $oauthEvent = oauthSubject.asObservable();

  beforeEach(() => {
    oAuthServiceMock = jasmine.createSpyObj('OAuthService', methods);
    TestBed.configureTestingModule({
      providers: [
          provideHttpClient(),
          provideHttpClientTesting(),
          {provide: OAuthService, useValue: oAuthServiceMock},
      ]
    });
    oAuthServiceMock.events = $oauthEvent;
  });

  it('should be created', () => {
    service = TestBed.inject(AuthService);
    expect(service).toBeTruthy();
  });

  describe('test initialization', () => {
    it('should call oAuthService.configure() on init', () => {
      const spy = oAuthServiceMock.configure.and.callThrough();
      TestBed.inject(AuthService);
      expect(spy).toHaveBeenCalledOnceWith(authCodeFlowConfig);
    });

    it('should call oAuthService.loadDiscoveryDocumentAndLogin() on init', () => {
      const spy =
          oAuthServiceMock.loadDiscoveryDocumentAndLogin.and.returnValue(Promise.resolve(true));
      TestBed.inject(AuthService);
      expect(spy).toHaveBeenCalledTimes(1);
    });

    it('should call oAuthService.setupAutomaticSilentRefresh() on init', fakeAsync(() => {
      oAuthServiceMock.loadDiscoveryDocumentAndLogin.and.returnValue(Promise.resolve(true));
      const spy = oAuthServiceMock.setupAutomaticSilentRefresh.and.callThrough();

      TestBed.inject(AuthService);
      tick();

      expect(spy).toHaveBeenCalled();
    }));

    it('should call oAuthService.loadUserProfile()', fakeAsync(() => {
        oAuthServiceMock.loadDiscoveryDocumentAndLogin.and.returnValue(Promise.resolve(true));
        const spy = oAuthServiceMock.loadUserProfile.and.callThrough();

        TestBed.inject(AuthService);
        tick();

        oauthSubject.next(tokenReceivedEvent);

        expect(spy).toHaveBeenCalled();
    }));
  });

  describe('test service methods', () => {
     beforeEach(() => {
       service = TestBed.inject(AuthService);
     });

     it('should return email', () => {
       const record: Record<string, string> = {'email': 'test@test.com'} ;
       const spy = oAuthServiceMock.getIdentityClaims.and.returnValue(record);

       let result = service.getEmail();

       expect(spy).toHaveBeenCalled();
       expect(result).toEqual('test@test.com');
     });

     it('should call oAuthService.logOut on logout', () => {
         const spy = oAuthServiceMock.logOut.and.callThrough();

         service.logOut();

         expect(spy).toHaveBeenCalled();
     });

     it('should return user name', () => {
        const record: Record<string, string> = {'given_name': 'username'} ;
        const spy = oAuthServiceMock.getIdentityClaims.and.returnValue(record);

        let result = service.getGivenName();

        expect(spy).toHaveBeenCalled();
        expect(result).toEqual('username');
     });

     it('isLoggedIn should return true', () => {
        const spy = oAuthServiceMock.hasValidIdToken.and.returnValue(true);

        let result = service.isLoggedIn();

        expect(spy).toHaveBeenCalled();
        expect(result).toEqual(true);
     });

    it('isLoggedIn should return false', () => {
      const spy = oAuthServiceMock.hasValidIdToken.and.returnValue(false);

      let result = service.isLoggedIn();

      expect(spy).toHaveBeenCalled();
      expect(result).toEqual(false);
    });
  });
});
