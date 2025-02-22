import {Inject, Injectable} from '@angular/core';
import {BehaviorSubject, filter} from "rxjs";
import {OAuthErrorEvent, OAuthService} from "angular-oauth2-oidc";
import {authCodeFlowConfig} from "../../../auth.config";
import {jwtDecode} from "jwt-decode";
import {RealmAccess} from "../models/realm-access";
import {PermissionType} from "../models/permission-type";
import {Roles} from "../models/roles";
import {Router} from "@angular/router";
import {AuthUser} from "../models/auth-user";


@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private user?: AuthUser;

  private permissionsMap: Map<PermissionType, string[]> = new Map();

  private isLoadedSubject$ = new BehaviorSubject<boolean>(false);
  public isLoaded$ = this.isLoadedSubject$.asObservable();

  private isAuthenticatedSubject$ = new BehaviorSubject(false)
  public isAuthenticated$ = this.isAuthenticatedSubject$.asObservable();

  private rolesLoadedSubject$ = new BehaviorSubject(false);
  public rolesLoaded$ = this.rolesLoadedSubject$.asObservable();


  public get currentUser(): AuthUser | undefined{
    return this.user;
  }

  constructor(private oauthService: OAuthService,
              private router: Router,
              private window: Window) {
    this.permissionsMap.set(PermissionType.read, [Roles.USER, Roles.ADMIN]);
    this.permissionsMap.set(PermissionType.write, [Roles.ADMIN]);
    this.initOauth();
  }

  login(url? : string): void {
    this.oauthService.initLoginFlow(url || this.router.url);
  }

  logOut() {
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
    console.log('inside startup login sequence');

    return this.oauthService.loadDiscoveryDocumentAndTryLogin()
        .then(success => {
          console.log('end tryLogin: ', success);
          if (!this.oauthService.hasValidAccessToken()) {
            this.navigateToLogin();
          }
          this.isLoadedSubject$.next(true);
        });
  }

  private loadCurrentUser(claims:  any): void {
    console.debug('claims: ', claims);
    let roles = this.parseRoles().filter(r => r.startsWith('club-'));
    this.user = {
      preferredUserName: claims["preferred_username"],
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
    console.log('initializing OAUTH');
    this.oauthService.configure(authCodeFlowConfig);

    // listen on any event.  Update the isAuthenticated subject
    this.oauthService.events.subscribe(() => {
      this.isAuthenticatedSubject$.next(this.oauthService.hasValidAccessToken());
    });

    this.oauthService.events
        .pipe(filter(event => ['token_received'].includes(event.type)))
        .subscribe(() => {
          console.log('token received, loading user profile');

          this.oauthService.loadUserProfile().then((claims) => {
            console.debug('profile claims: ', claims);
            this.loadCurrentUser(claims);
            this.rolesLoadedSubject$.next(true);
          });
        });

    this.oauthService.events
        .pipe(filter(event => ['session_terminated', 'session_error'].includes(event.type)))
        .subscribe(() => this.navigateToLogin());


    this.window.addEventListener('storage', (event) => {
      // The `key` is `null` if the event was caused by `.clear()`
      if (event.key !== 'access_token' && event.key !== null) {
        return;
      }

      console.warn('Noticed changes to access_token (most likely from another tab), updating isAuthenticated');
      this.isAuthenticatedSubject$.next(this.oauthService.hasValidAccessToken());

      if (!this.oauthService.hasValidAccessToken()) {
        this.navigateToLogin();
      }
    });

    // if a valid token is in storage, populate user roles.
    if(this.oauthService.hasValidAccessToken()) {
      const claims = this.oauthService.getIdentityClaims()
      this.loadCurrentUser(claims);
      this.rolesLoadedSubject$.next(true);
    }

    this.oauthService.setupAutomaticSilentRefresh();
  }
}
