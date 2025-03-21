import {HttpInterceptorFn} from '@angular/common/http';
import {inject} from "@angular/core";
import {OAuthModuleConfig, OAuthResourceServerErrorHandler, OAuthService, OAuthStorage} from "angular-oauth2-oidc";
import {catchError, map, mergeMap, take, timeout} from "rxjs/operators";
import {filter, merge, of} from "rxjs";

export const customOauthInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(OAuthService);
  const authStorage = inject(OAuthStorage);
  const moduleConfig = inject(OAuthModuleConfig);
  const errorHandler = inject(OAuthResourceServerErrorHandler);

  const url = req.url.toLowerCase();

  if (!moduleConfig.resourceServer.allowedUrls ||
      !checkUrl(url, moduleConfig.resourceServer.allowedUrls!)) {
    return next(req);
  }

  const sendAccessToken = moduleConfig.resourceServer.sendAccessToken;

  if (!sendAccessToken) {
    return next(req).pipe(catchError(error => errorHandler.handleError(error)));
  }

  return merge(
      of(authStorage.getItem('access_token')).pipe(filter((token) => !!token)),
      authService.events.pipe(
          filter((e) => e.type === 'token_received'),
          timeout(authService.waitForTokenInMsec || 0),
          catchError(() => of(null)),
          map(() => authStorage.getItem('access_token'))
      )
  ).pipe(
      take(1),
      mergeMap((token) => {
        if (token) {
          const header = 'Bearer ' + token;
          const headers = req.headers.set('Authorization', header);
          req = req.clone({ headers });
        }

        return next(req).pipe(catchError(error => errorHandler.handleError(error)));
      })
  )
};

export function checkUrl(url: string, allowedUrls: string[]): boolean {
  const found = allowedUrls.find(u => url.startsWith(u));
  return !!found;
}
