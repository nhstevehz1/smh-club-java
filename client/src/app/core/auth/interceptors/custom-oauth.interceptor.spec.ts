import {TestBed} from '@angular/core/testing';
import {HttpClient, HttpInterceptorFn, provideHttpClient, withInterceptors} from '@angular/common/http';
import {HttpTestingController, provideHttpClientTesting} from '@angular/common/http/testing';
import {Subject} from 'rxjs';

import {
  OAuthEvent,
  OAuthModuleConfig,
  OAuthResourceServerErrorHandler,
  OAuthService,
  OAuthStorage
} from 'angular-oauth2-oidc';

import {customOauthInterceptor} from './custom-oauth.interceptor';

describe('defaultOauthInterceptor', () => {
  const interceptor: HttpInterceptorFn = (req, next) =>
    TestBed.runInInjectionContext(() => customOauthInterceptor(req, next));

  let testController: HttpTestingController;
  let http: HttpClient;

  let authStorageMock: jasmine.SpyObj<OAuthStorage>;
  let oAuthSvcMock: jasmine.SpyObj<OAuthService>;
  let errorHandlerMock: jasmine.SpyObj<OAuthResourceServerErrorHandler>;
  let fakeModuleConfig: OAuthModuleConfig;

  const eventSubject$ = new Subject<OAuthEvent>();
  const eventObservable$ = eventSubject$.asObservable();

  const url = '/test'

  beforeEach(() => {
    authStorageMock = jasmine.createSpyObj('OAuthStorage', ['getItem']);
    oAuthSvcMock = jasmine.createSpyObj('OAuthService', ['events', 'waitForTokenInMsec']);
    errorHandlerMock =
        jasmine.createSpyObj('OAuthResourceServerErrorHandler', ['handleError']);

    fakeModuleConfig = {
      resourceServer: {
        allowedUrls: [],
        sendAccessToken: false
      }
    }

    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(withInterceptors([customOauthInterceptor])),
        provideHttpClientTesting(),
        {provide: OAuthStorage, useValue: authStorageMock},
        {provide: OAuthModuleConfig, useValue: fakeModuleConfig},
        {provide: OAuthService, useValue: oAuthSvcMock},
        {provide: OAuthResourceServerErrorHandler, useValue: errorHandlerMock}
      ]
    });

    testController = TestBed.inject(HttpTestingController);
    http = TestBed.inject(HttpClient);
    oAuthSvcMock.waitForTokenInMsec = 0;
    oAuthSvcMock.events = eventObservable$;
  });

  afterEach(() => {
    testController.verify();
  })

  it('should be created', () => {
    expect(interceptor).toBeTruthy();
  });

  it('should add auth header', async () => {
    fakeModuleConfig.resourceServer.allowedUrls?.push(url);
    fakeModuleConfig.resourceServer.sendAccessToken = true;

    const fakeToken = 'fake token';
    const spy = authStorageMock.getItem.and.returnValue(fakeToken);

    eventSubject$.next({type: 'token_received'});

    http.get(url).subscribe();

    const req = testController.expectOne(url);
    expect(spy).toHaveBeenCalledWith('access_token');
    expect(req.request.headers.get('Authorization')).toEqual(`Bearer ${fakeToken}`);
  });

  it('should NOT add header if url not in allowedUrls', async () => {
    fakeModuleConfig.resourceServer.sendAccessToken = true;
    fakeModuleConfig.resourceServer.allowedUrls?.push(url);
    const fakeUrl = '/fake';

    http.get(fakeUrl).subscribe();

    const req = testController.expectOne(fakeUrl);
    expect(req.request.headers.get('Authorization')).toBeNull();
  });

  it('should NOT add header if sendAccessToken is false', async () => {
    fakeModuleConfig.resourceServer.sendAccessToken = false;
    fakeModuleConfig.resourceServer.allowedUrls?.push(url);

    http.get(url).subscribe();

    const req = testController.expectOne(url);
    expect(req.request.headers.get('Authorization')).toBeNull();
  });
});
