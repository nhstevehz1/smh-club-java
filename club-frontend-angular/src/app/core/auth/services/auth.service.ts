import {Injectable} from '@angular/core';
import {filter} from "rxjs";
import {OAuthService} from "angular-oauth2-oidc";
import {authCodeFlowConfig} from "../../../auth.config";
import {jwtDecode} from "jwt-decode";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private oauthService: OAuthService) {
    this.oauthService.configure(authCodeFlowConfig);

    this.oauthService.loadDiscoveryDocumentAndLogin();
    this.oauthService.setupAutomaticSilentRefresh();

    this.oauthService.events
        .pipe(filter(event => event.type === 'token_received'))
        .subscribe(() => this.oauthService.loadUserProfile());

  }

  signIn() {
    return this.oauthService.initLoginFlow();
  }

  signOut() {
    return this.oauthService.logOut();
  }

  get email(): string {
    const claims = this.oauthService.getIdentityClaims();
    if (!claims) return 'Email claim not found';
    return claims['email'];
  }

  get userName(): string {
    const claims = this.oauthService.getIdentityClaims();
    if (!claims) return 'given name claim not found';
    return claims['given_name'];
  }

  get isAuthenticated(): boolean {
    return this.oauthService.hasValidIdToken();
  }

  get idToken(): string {
    const token = this.oauthService.getIdToken();
    if (token) {
      return jwtDecode(token); //this.decodeAndStringifyToken(token);
    }
    return 'id token not found';
  }

  get refreshToken(): string {
    const token = this.oauthService.getRefreshToken();
    if (token) {
      return jwtDecode(token); //this.decodeAndStringifyToken(token);
    }
    return 'refresh token not found';
  }

  get accessToken(): string {
    const token = this.oauthService.getAccessToken();
    if (token) {
      return jwtDecode(token);
    }
    return 'access token not found';
  }

  private decodeAndStringifyToken(token: any) {
    return JSON.stringify(jwtDecode(token), null, 2);
  }
}
