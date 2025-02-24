import {fakeAsync, TestBed, tick} from '@angular/core/testing';

import {AuthService} from './auth.service';
import {provideHttpClient} from "@angular/common/http";
import {provideHttpClientTesting} from "@angular/common/http/testing";
import {OAuthEvent, OAuthService, OAuthSuccessEvent} from "angular-oauth2-oidc";
import {Subject} from "rxjs";
import {authCodeFlowConfig} from "../../../auth.config";

describe('AuthService', () => {
  let service: AuthService;
  let oAuthMock: jasmine.SpyObj<OAuthService>;
  const methods = [
        'configure', 'loadDiscoveryDocumentAndTryLogin', 'setupAutomaticSilentRefresh',
        'loadUserProfile', 'events', 'getAccessToken', 'getIdentityClaims',
      'logOut', 'hasValidAccessToken', 'hasValidIdToken', 'initLoginFlow'
  ];

  const claimsMock = {
      preferred_username: 'username',
      given_name: 'given name',
      family_name: 'family name',
      name: 'name',
      email: 'email@email.com'
  }
  const tokenReceivedEvent = new OAuthSuccessEvent('token_received');

  let oauthSubject$ = new Subject<OAuthEvent>();
  let oauthEvent$ = oauthSubject$.asObservable();

  let windowMock: jasmine.SpyObj<Window>;

  beforeEach(() => {
    oAuthMock = jasmine.createSpyObj('OAuthService', methods);
    windowMock = jasmine.createSpyObj('Window', ['addEventListener', 'dispatchEvent']);
    TestBed.configureTestingModule({
      providers: [
          provideHttpClient(),
          provideHttpClientTesting(),
          {provide: OAuthService, useValue: oAuthMock},
          {provide: Window, useValue: windowMock}
      ]
    });
    oAuthMock.events = oauthEvent$;
  });

  it('should be created', () => {
      spyOn(oAuthMock.events, 'pipe').and.callThrough();
      spyOn(oAuthMock.events.pipe(), 'subscribe').and.stub();

    service = TestBed.inject(AuthService);
    expect(service).toBeTruthy();
  });

  describe('test startupLoginSequence', () => {
      it('should call loadDiscoveryDocumentAndTryLogin()', fakeAsync(() => {
          const spy =
              oAuthMock.loadDiscoveryDocumentAndTryLogin.and.returnValue(Promise.resolve(true));
          oAuthMock.hasValidAccessToken.and.returnValue(false);
          spyOn(oAuthMock.events, 'pipe').and.callThrough();
          spyOn(oAuthMock.events.pipe(), 'subscribe').and.stub();

          const svc = TestBed.inject(AuthService);

          // @ts-expect-error
          const svcSpy = spyOn(svc, 'navigateToLogin').and.stub();
          svc.startupLoginSequence();

          tick();

          expect(spy).toHaveBeenCalled();
          expect(svcSpy).toHaveBeenCalled()
      }));

  })

  describe('test initialization', () => {
    it('should call oAuthService.configure() on init', () => {
      const spy = oAuthMock.configure.and.callThrough();
        spyOn(oAuthMock.events, 'pipe').and.callThrough();
        spyOn(oAuthMock.events.pipe(), 'subscribe').and.stub();

      TestBed.inject(AuthService);
      expect(spy).toHaveBeenCalledOnceWith(authCodeFlowConfig);
    });

    it('should call oAuthService.setupAutomaticSilentRefresh() on init', fakeAsync(() => {
      const spy = oAuthMock.setupAutomaticSilentRefresh.and.callThrough();
      spyOn(oAuthMock.events, 'pipe').and.callThrough();
      spyOn(oAuthMock.events.pipe(), 'subscribe').and.stub();

      TestBed.inject(AuthService);
      tick();

      expect(spy).toHaveBeenCalled();
    }));

    it('should call oAuthService.events', fakeAsync(() => {
        const pipeSpy = spyOn(oAuthMock.events, 'pipe').and.callThrough();
        const subSpy = spyOn(oAuthMock.events.pipe(), 'subscribe').and.stub();

        TestBed.inject(AuthService);
        tick();

        expect(pipeSpy).toHaveBeenCalledTimes(3);
        expect(subSpy).toHaveBeenCalledTimes(3)
    }));

    it('should call oAuthService.loadUserProfile on token_received event', fakeAsync(() => {
        spyOn(oAuthMock.events, 'pipe').and.callThrough();
        spyOn(oAuthMock.events.pipe(), 'subscribe').and.callThrough();
        const spy = oAuthMock.loadUserProfile.and.returnValue(Promise.resolve(claimsMock));

        TestBed.inject(AuthService);

        oauthSubject$.next(tokenReceivedEvent);
        tick();

        expect(spy).toHaveBeenCalled();
    }));

    it('should call oAuthService.hasValidAccessToken()', fakeAsync(() => {
        spyOn(oAuthMock.events, 'pipe').and.callThrough();
        spyOn(oAuthMock.events.pipe(), 'subscribe').and.stub();

        const spy = oAuthMock.hasValidAccessToken.and.stub();

        TestBed.inject(AuthService);
        tick()

        expect(spy).toHaveBeenCalled();
    }));

    it('should call oAuthService.setupAutomaticClientRefresh()', fakeAsync(() => {
        spyOn(oAuthMock.events, 'pipe').and.callThrough();
        spyOn(oAuthMock.events.pipe(), 'subscribe').and.stub();

        const spy = oAuthMock.setupAutomaticSilentRefresh.and.stub();

        TestBed.inject(AuthService);
        tick();

        expect(spy).toHaveBeenCalled();
    }));

    it('should call window.addEventListener', fakeAsync(() => {
        const spy = windowMock.addEventListener.and.stub();
        spyOn(oAuthMock.events, 'pipe').and.callThrough();
        spyOn(oAuthMock.events.pipe(), 'subscribe').and.stub();

        TestBed.inject(AuthService);
        tick();

        expect(spy).toHaveBeenCalled();
    }));
  });

  describe('test service methods', () => {
     beforeEach(() => {
       service = TestBed.inject(AuthService);
         spyOn(oAuthMock.events, 'pipe').and.callThrough();
         spyOn(oAuthMock.events.pipe(), 'subscribe').and.stub();
     });

      it('should call oAuthService.initCodeFlow on login', () => {
          const spy = oAuthMock.initLoginFlow.and.stub();

          service.login();

          expect(spy).toHaveBeenCalled();
      });

     it('should call oAuthService.logOut on logout', () => {
         const spy = oAuthMock.logOut.and.stub();

         service.logOut();

         expect(spy).toHaveBeenCalled();
     });


     it('isLoggedIn should return true', () => {
        const spy = oAuthMock.hasValidIdToken.and.returnValue(true);

        let result = service.isLoggedIn();

        expect(spy).toHaveBeenCalled();
        expect(result).toEqual(true);
     });

    it('isLoggedIn should return false', () => {
      const spy = oAuthMock.hasValidIdToken.and.returnValue(false);

      let result = service.isLoggedIn();

      expect(spy).toHaveBeenCalled();
      expect(result).toEqual(false);
    });
  });
});
