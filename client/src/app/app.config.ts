import {ApplicationConfig, inject, provideAppInitializer, provideZoneChangeDetection} from '@angular/core';
import {provideRouter} from '@angular/router';

import {routes} from './app.routes';
import {provideHttpClient, withInterceptors} from "@angular/common/http";
import {loadingSpinnerInterceptor} from "./core/loading/loading-spinner.interceptor";
import {provideAnimations} from "@angular/platform-browser/animations";
import {provideOAuthClient} from "angular-oauth2-oidc";
import {customOauthInterceptor} from "./core/auth/interceptors/custom-oauth.interceptor";
import {authAppInitFactory} from "./core/auth/factories/auth-app-init-factory";
import {AuthService} from "./core/auth/services/auth.service";
import {DOCUMENT} from "@angular/common";
import {provideNgxTranslate} from "./core/i18n/providers/ngx-translate-provider";

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
