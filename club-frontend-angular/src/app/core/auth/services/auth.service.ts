import {Injectable} from '@angular/core';
import {filter} from "rxjs";
import {OAuthService} from "angular-oauth2-oidc";
import {authCodeFlowConfig} from "../../../auth.config";
import {jwtDecode} from "jwt-decode";
import {RealmAccess} from "../models/realm-access";
import {PermissionType} from "../models/permission-type";
import {Roles} from "../models/roles";


@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private permissionsMap: Map<PermissionType, string> = new Map();

  constructor(private oauthService: OAuthService) {
    this.initOauth().then(() => {
      this.permissionsMap.set(PermissionType.read, Roles.USER);
      this.permissionsMap.set(PermissionType.write, Roles.ADMIN);
    });
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

  get roles(): string[] {
    return this.parseRoles().filter(r => r.startsWith('club-'));
  }

  private hasRole(role?: string): boolean {
    const val = role || ''
    return this.roles.includes(val);
  }

  hasPermission(permission: PermissionType): boolean {
    return this.permissionsMap.has(permission) && this.hasRole(this.permissionsMap.get(permission));
  }

  get isLoggedIn(): boolean {
    return this.oauthService.hasValidIdToken();
  }

  get idToken(): string {
    const token = this.oauthService.getIdToken();
    if (token) {
      return jwtDecode(token);
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

  private parseRoles(): string[] {
    const token = this.oauthService.getAccessToken();
    if (token) {
      const jsonString = JSON.stringify(jwtDecode(token));
      const realmAccess: RealmAccess = JSON.parse(jsonString)['realm_access'];
      return realmAccess.roles;
    }
    return [];
  }

  private async initOauth(): Promise<void> {
    this.oauthService.configure(authCodeFlowConfig);

    await this.oauthService.loadDiscoveryDocumentAndLogin();
    this.oauthService.setupAutomaticSilentRefresh();

    this.oauthService.events
        .pipe(filter(event => event.type === 'token_received'))
        .subscribe(() => this.oauthService.loadUserProfile());
  }
}
