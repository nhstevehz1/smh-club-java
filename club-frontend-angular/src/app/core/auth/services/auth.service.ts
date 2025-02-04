import {Injectable} from '@angular/core';
import {BehaviorSubject, filter} from "rxjs";
import {OAuthErrorEvent, OAuthService} from "angular-oauth2-oidc";
import {authCodeFlowConfig} from "../../../auth.config";
import {jwtDecode} from "jwt-decode";
import {RealmAccess} from "../models/realm-access";
import {PermissionType} from "../models/permission-type";
import {Roles} from "../models/roles";
import {Router} from "@angular/router";


@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private permissionsMap: Map<PermissionType, string> = new Map();

  private isLoadedSubject$ = new BehaviorSubject<boolean>(false);
  public isLoaded$ = this.isLoadedSubject$.asObservable();

  constructor(private oauthService: OAuthService, private router: Router) {
    this.permissionsMap.set(PermissionType.read, Roles.USER);
    this.permissionsMap.set(PermissionType.write, Roles.ADMIN);
    this.initOauth();
  }

  logOut() {
    return this.oauthService.logOut();
  }

  getEmail(): string {
    const claims = this.oauthService.getIdentityClaims();
    if (!claims) return 'email claim not found';
    return claims['email'];
  }

  getGivenName(): string {
    const claims = this.oauthService.getIdentityClaims();
    if (!claims) return 'given_name claim not found';
    return claims['given_name'];
  }

  getRoles(): string[] {
    return this.parseRoles().filter(r => r.startsWith('club-'));
  }

  private hasRole(role?: string): boolean {
    const val = role || ''
    return this.getRoles().includes(val);
  }

  hasPermission(permission: PermissionType): boolean {
    return this.permissionsMap.has(permission) && this.hasRole(this.permissionsMap.get(permission));
  }

  isLoggedIn(): boolean {
    return this.oauthService.hasValidIdToken();
  }

  getIdToken(): string {
    const token = this.oauthService.getIdToken();
    if (token) {
      return jwtDecode(token);
    }
    return 'id token not found';
  }

  getRefreshToken(): string {
    const token = this.oauthService.getRefreshToken();
    if (token) {
      return jwtDecode(token);
    }
    return 'refresh token not found';
  }

  getAccessToken(): string {
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

  private initOauth(): void {
    this.oauthService.configure(authCodeFlowConfig);

    this.oauthService.events.subscribe(event => {
      if ( event instanceof OAuthErrorEvent) {
        console.error('OAuthErrorEvent', event);
      }
    });

    this.oauthService.setupAutomaticSilentRefresh();

    this.oauthService.events
        .pipe(filter(event => event.type === 'token_received'))
        .subscribe(() => this.oauthService.loadUserProfile());

    this.oauthService.loadDiscoveryDocumentAndLogin().then(() => {
      console.log('Login complete');
      this.isLoadedSubject$.next(true);
      this.router.navigate(['p/home']).then(() => {});
    });
  }
}
