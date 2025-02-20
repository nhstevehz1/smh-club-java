import { TestBed } from '@angular/core/testing';
import { HttpInterceptorFn } from '@angular/common/http';

import { DefaultOAuthInterceptor } from './defaultOAuthInterceptor';
import {OAuthModuleConfig, OAuthStorage} from "angular-oauth2-oidc";

describe('defaultOauthInterceptor', () => {
  const interceptor: HttpInterceptorFn = (req, next) => 
    TestBed.runInInjectionContext(() => DefaultOAuthInterceptor(req, next));

  let authStorageMock: jasmine.SpyObj<OAuthStorage>;
  let autConfigMock: jasmine.SpyObj<OAuthModuleConfig>;

  beforeEach(() => {
    authStorageMock = jasmine.createSpyObj('OAuthStorage', ['getItem']);
    autConfigMock = jasmine.createSpyObj('OAuthModuleConfig', ['resourceServer']);

    TestBed.configureTestingModule({
      providers: [
        {provide: OAuthStorage, useValue: authStorageMock},
        {provide: OAuthModuleConfig, useValue: autConfigMock},
      ]
    });
  });

  it('should be created', () => {
    expect(interceptor).toBeTruthy();
  });
});
