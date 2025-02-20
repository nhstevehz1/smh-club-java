import {Injectable} from '@angular/core';
import {BehaviorSubject, combineLatest, filter, Observable} from "rxjs";
import {OAuthErrorEvent, OAuthService, OAuthSuccessEvent, ReceivedTokens} from "angular-oauth2-oidc";
import {authCodeFlowConfig} from "../../../auth.config";
import {jwtDecode} from "jwt-decode";
import {RealmAccess} from "../models/realm-access";
import {PermissionType} from "../models/permission-type";
import {Roles} from "../models/roles";
import {Router} from "@angular/router";
import {map} from "rxjs/operators";


@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private userRoles?: string[];

  private permissionsMap: Map<PermissionType, string[]> = new Map();

  private isLoadedSubject$ = new BehaviorSubject<boolean>(false);
  public isLoaded$ = this.isLoadedSubject$.asObservable();

  private isAuthenticatedSubject$ = new BehaviorSubject(false)
  public isAuthenticated$ = this.isAuthenticatedSubject$.asObservable();

  public canActivateRoutes$: Observable<boolean> = combineLatest([
      this.isAuthenticated$,
      this.isLoaded$
  ]).pipe(map(values => values.every(b=> b)));

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
    //this.isLoadedSubject$.next(false);
    //this.isAuthenticatedSubject$.next(false);
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
    return this.userRoles || [];
  }

  private hasRole(roles?: string[]): boolean {
    if (roles) {
      return this.getRoles().some((val) => roles.includes(val));
    } else {
      return false;
    }
  }

  hasPermission(permission: PermissionType): boolean {
    return this.permissionsMap.has(permission) && this.hasRole(this.permissionsMap.get(permission));
  }

  isLoggedIn(): boolean {
    return this.oauthService.hasValidIdToken();
  }

  public startupLoginSequence(): Promise<void> {
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

  private needsInteraction(result: { reason: { error: string; }; }): boolean {
  const reasons = [
      'interaction_required',
      'login_required',
      'account_selection_required',
      'consent_required',
    ];
    return result
        && result.reason
        && reasons.includes(result.reason.error);
  }

  // For debugging only - start
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
  // for debugging only - end

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
    console.log('initializing OAUTH');
    this.oauthService.configure(authCodeFlowConfig);

    this.oauthService.events.subscribe(event => {
      if ( event instanceof OAuthErrorEvent) {
        console.error('OAuthErrorEvent: ', event);
      } else {
        console.warn('OauthEvent: ', event)
      }
    });

    window.addEventListener('storage', (event) => {
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

    this.oauthService.events.subscribe(() => {
      console.log('inside hasValidAccessToken: ', this.oauthService.hasValidAccessToken());
      this.isAuthenticatedSubject$.next(this.oauthService.hasValidAccessToken());
    });

    console.log('outside hasValidAccessToken: ', this.oauthService.hasValidAccessToken());
    this.isAuthenticatedSubject$.next(this.oauthService.hasValidAccessToken());

    this.oauthService.events
        .pipe(filter(event => ['token_received'].includes(event.type)))
        .subscribe(() => {
          console.log('token received, loading user profile');
          //this.userRoles = this.parseRoles().filter(r => r.startsWith('club-'));
          //console.log('user roles: ', this.userRoles);
          this.oauthService.loadUserProfile().then((claims) => {
            console.log('profile claims: ', claims);
            // TODO: populate user profile object.
          });
        });

    this.oauthService.events
        .pipe(filter(event => ['session_terminated', 'session_error'].includes(event.type)))
        .subscribe(event => this.navigateToLogin());

    this.oauthService.setupAutomaticSilentRefresh();

  }

  private navigateToLogin(): void {
    this.router.navigateByUrl('p/login').then();
  }
}
