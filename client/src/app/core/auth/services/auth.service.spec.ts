import {fakeAsync, TestBed, tick} from '@angular/core/testing';

import {AuthService} from './auth.service';
import {provideHttpClient} from "@angular/common/http";
import {provideHttpClientTesting} from "@angular/common/http/testing";
import {OAuthEvent, OAuthService} from "angular-oauth2-oidc";
import {Observable, Subject} from "rxjs";
import {authCodeFlowConfig} from "../../../auth.config";

describe('AuthService', () => {
  let service: AuthService;
  let oAuthMock: jasmine.SpyObj<OAuthService>;
  const methods = [
        'configure', 'loadDiscoveryDocumentAndTryLogin', 'setupAutomaticSilentRefresh',
        'loadUserProfile', 'events', 'getAccessToken', 'getIdentityClaims',
      'logOut', 'hasValidAccessToken', 'hasValidIdToken', 'initLoginFlow'
  ];

  let oauthSubject$: Subject<OAuthEvent>
  let oauthEvent$: Observable<OAuthEvent>;

  let windowMock: jasmine.SpyObj<Window>;

  beforeEach(() => {
    oAuthMock = jasmine.createSpyObj('OAuthService', methods);
    windowMock = jasmine.createSpyObj('Window', ['addEventListener', 'dispatchEvent']);

    oauthSubject$ = new Subject<OAuthEvent>();
    oauthEvent$  = oauthSubject$.asObservable()

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

          // @ts-expect-error bug in testing framework
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

        expect(pipeSpy).toHaveBeenCalledTimes(2);
        expect(subSpy).toHaveBeenCalledTimes(2)
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

        const result = service.isLoggedIn();

        expect(spy).toHaveBeenCalled();
        expect(result).toEqual(true);
     });

    it('isLoggedIn should return false', () => {
      const spy = oAuthMock.hasValidIdToken.and.returnValue(false);

      const result = service.isLoggedIn();

      expect(spy).toHaveBeenCalled();
      expect(result).toEqual(false);
    });
  });
});
