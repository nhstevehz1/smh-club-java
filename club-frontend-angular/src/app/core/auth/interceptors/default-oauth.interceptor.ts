import {HttpInterceptorFn} from '@angular/common/http';
import {inject} from "@angular/core";
import {OAuthModuleConfig, OAuthStorage} from "angular-oauth2-oidc";

export const DefaultOAuthInterceptor: HttpInterceptorFn = (req, next) => {
  const authStorage = inject(OAuthStorage);
  const moduleConfig = inject(OAuthModuleConfig);

  const url = req.url.toLowerCase();
  console.log(url);

  if (!moduleConfig) return next(req);
  if (!moduleConfig.resourceServer) return next(req);
  if (!moduleConfig.resourceServer.allowedUrls) return next(req);

  const allowedUrls = moduleConfig.resourceServer.allowedUrls;
  if (!checkUrl(url, allowedUrls)) return next(req);

  // url is in the allowed list.  Set authorization header with bearer token
  const sendAccessToken = moduleConfig.resourceServer.sendAccessToken
  const token = authStorage.getItem('access_token');

  if (sendAccessToken && token) {
    let header = 'Bearer ' + token;
    let headers = req.headers.set('Authorization', header);
    req = req.clone({headers: headers});
  }

  return next(req);
};

export function checkUrl(url: string, allowedUrls: string[]): boolean {
  let found = allowedUrls.find(u => url.startsWith(u));
  return !!found;
}
