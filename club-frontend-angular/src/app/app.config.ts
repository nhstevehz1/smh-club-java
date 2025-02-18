import {ApplicationConfig, provideZoneChangeDetection} from '@angular/core';
import {provideRouter} from '@angular/router';

import {routes} from './app.routes';
import {provideHttpClient, withInterceptors} from "@angular/common/http";
import {loadingSpinnerInterceptor} from "./core/loading/loading-spinner.interceptor";
import {provideAnimations} from "@angular/platform-browser/animations";
import {provideOAuthClient} from "angular-oauth2-oidc";
import {DefaultOAuthInterceptor} from "./core/auth/interceptors/default-oauth.interceptor";

export let appConfig: ApplicationConfig;
appConfig = {
  providers: [
    provideAnimations(),
    provideZoneChangeDetection({eventCoalescing: true}),
    provideRouter(routes),
    provideHttpClient(
        withInterceptors([
          loadingSpinnerInterceptor,
          DefaultOAuthInterceptor
        ])
    ),
    provideOAuthClient({
      resourceServer: {
        allowedUrls: [
            '/api/v1', // used when running in dev mode
            'http://localhost:9001/api/v1',
            'https://localhost:9000/api/v1'
        ],
        sendAccessToken: false,
      }
    })
  ]
};
