import {HttpInterceptorFn} from '@angular/common/http';
import {inject} from "@angular/core";
import {OAuthModuleConfig, OAuthStorage} from "angular-oauth2-oidc";

export const oauthInterceptor: HttpInterceptorFn = (req, next) => {
  let authStorage = inject(OAuthStorage);
  let moduleConfig = inject(OAuthModuleConfig);

  let url = req.url.toLowerCase();
  if (!moduleConfig) return next(req);
  if (!moduleConfig.resourceServer) return next(req);
  if (!moduleConfig.resourceServer.allowedUrls) return next(req);
  if (checkUrl(url, moduleConfig)) return next(req);

  let sendAccessToken = moduleConfig.resourceServer.sendAccessToken

  if (sendAccessToken) {
    let token = authStorage.getItem('access_token');
    let header = 'Bearer ' + token;
    let headers = req.headers.set('Authorization', header);
    req = req.clone({headers: headers});
  }

  return next(req);
};

export function checkUrl(url: string, config: OAuthModuleConfig): boolean {
  let found = config?.resourceServer?.allowedUrls?.find(u => u.indexOf(url) > -1);
  return !!found;
}
