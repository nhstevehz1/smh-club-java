import {ApplicationConfig, inject, provideAppInitializer, provideZoneChangeDetection} from '@angular/core';
import {provideRouter} from '@angular/router';
import {provideHttpClient, withInterceptors} from '@angular/common/http';
import {DOCUMENT} from '@angular/common';
import {provideAnimations} from '@angular/platform-browser/animations';
import {provideOAuthClient} from 'angular-oauth2-oidc';


import {customOauthInterceptor, authAppInitFactory, AuthService} from '@app/core/auth';
import {provideNgxTranslate} from '@app/core/i18n';
import {routes} from '@app/app.routes';
import {loadingSpinnerInterceptor} from '@app/core/loading';

export const appConfig: ApplicationConfig = {
  providers: [
    provideAnimations(),
    provideZoneChangeDetection({eventCoalescing: true}),
    provideRouter(routes),
    provideHttpClient(
        withInterceptors([
          loadingSpinnerInterceptor,
          customOauthInterceptor
        ])
    ),
    provideOAuthClient({
      resourceServer: {
        allowedUrls: [
            '/api/v1', // used when running in dev mode
            'http://localhost:9001/api/v1',
            'https://localhost:9000/api/v1'
        ],
        sendAccessToken: true,
      }
    }),
      provideNgxTranslate(), // custom wrapper around ngx-translate
      provideAppInitializer(() => {
          const initFn = (authAppInitFactory)(inject(AuthService));
          return initFn();
      }),
      {
          provide: Window,
          useFactory: (): Window => inject(DOCUMENT)?.defaultView as Window
      }

  ]
};
