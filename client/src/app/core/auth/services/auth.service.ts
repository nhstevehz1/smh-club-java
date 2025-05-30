import {Injectable} from '@angular/core';
import {Router} from '@angular/router';
import {BehaviorSubject, filter} from 'rxjs';
import {OAuthService} from 'angular-oauth2-oidc';
import {jwtDecode} from 'jwt-decode';

import {authCodeFlowConfig} from '@app/auth.config';
import {AuthUser} from '@app/core/auth/models/auth-user';
import {PermissionType} from '@app/core/auth/models/permission-type';
import {RealmAccess} from '@app/core/auth/models/realm-access';
import {Roles} from '@app/core/auth/models/roles';


@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private user?: AuthUser;

  private permissionsMap = new Map<PermissionType, string[]>();

  private isLoadedSubject$ = new BehaviorSubject<boolean>(false);
  public isLoaded$ = this.isLoadedSubject$.asObservable();

  private isAuthenticatedSubject$ = new BehaviorSubject(false)
  public isAuthenticated$ = this.isAuthenticatedSubject$.asObservable();

  constructor(private oauthService: OAuthService,
              private router: Router,
              private window: Window) {
    this.permissionsMap.set(PermissionType.read, [Roles.USER, Roles.ADMIN]);
    this.permissionsMap.set(PermissionType.write, [Roles.ADMIN]);
    this.initOauth();
  }

  getCurrentUser(): AuthUser | undefined{
    return this.user;
  }

  login(url? : string): void {
    this.oauthService.initLoginFlow(url || this.router.url);
  }

  logOut(): void {
    return this.oauthService.logOut();
  }

  hasPermission(permission: PermissionType): boolean {
    return this.permissionsMap.has(permission)
        && this.hasRole(this.permissionsMap.get(permission));
  }

  isLoggedIn(): boolean {
    return this.oauthService.hasValidIdToken();
  }

   async startupLoginSequence(): Promise<void> {
    return this.oauthService.loadDiscoveryDocumentAndTryLogin()
        .then(() => {
          if (!this.oauthService.hasValidAccessToken()) {
            this.navigateToLogin();
          }
          this.isLoadedSubject$.next(true);
        });
  }

  private loadCurrentUser(): void {
    const roles = this.parseRoles().filter(r => r.startsWith('club-'));
    const claims = this.oauthService.getIdentityClaims();
    this.user = {
      preferredUserName: claims['preferred_username'],
      givenName: claims['given_name'],
      familyName: claims['family_name'],
      fullName: claims['name'],
      email: claims['email'],
      roles: roles
    }
  }

  private hasRole(roles?: string[]): boolean {
    if (roles && this.user) {
      return this.user.roles.some((val) => roles.includes(val));
    } else {
      return false;
    }
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

  private navigateToLogin(): void {
    this.router.navigateByUrl('p/login').then();
  }

  private initOauth(): void {
    this.oauthService.configure(authCodeFlowConfig);

    this.isAuthenticated$.pipe(filter(authed => authed))
        .subscribe(() => this.loadCurrentUser());

    // listen on any event.  Update the isAuthenticated subject
    this.oauthService.events.subscribe(() => {
      this.isAuthenticatedSubject$.next(this.oauthService.hasValidAccessToken());
    });

    this.oauthService.events
        .pipe(filter(event => ['session_terminated', 'session_error'].includes(event.type)))
        .subscribe(() => this.navigateToLogin());


    this.window.addEventListener('storage', (event) => {
      // The `key` is `null` if the event was caused by `.clear()`
      if (event.key !== 'access_token' && event.key !== null) {
        return;
      }

      this.isAuthenticatedSubject$.next(this.oauthService.hasValidAccessToken());

      if (!this.oauthService.hasValidAccessToken()) {
        this.navigateToLogin();
      }
    });

    // if a valid token is in storage, populate user roles.
    if(this.oauthService.hasValidAccessToken()) {
      this.loadCurrentUser();
      this.isAuthenticatedSubject$.next(true);
    }

    this.oauthService.setupAutomaticSilentRefresh();
  }
}
