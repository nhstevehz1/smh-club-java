import {Injectable, OnDestroy, OnInit} from '@angular/core';
import {BehaviorSubject, Observable, Subscription, tap} from "rxjs";
import {AuthUser} from "../models/auth-user";
import {HttpClient} from "@angular/common/http";
import {EventBusService} from "./event-bus.service";
import {RoleType} from "../models/role-type";
import {DateTime} from "luxon";

const USER_KEY = 'auth-user';

@Injectable({
  providedIn: 'root'
})
export class AuthService implements OnInit, OnDestroy {

  private userSubject: BehaviorSubject<AuthUser | null>;
  private eventBusSub: Subscription | null = null;
  public user: Observable<AuthUser | null>;

  constructor(private http: HttpClient,
              private eventBusService: EventBusService) {

    this.userSubject
        = new BehaviorSubject<AuthUser | null>(<AuthUser>this.getUser());
    this.user = this.userSubject.asObservable();

    console.log("AuthService Created");
  }

  ngOnInit() {
    this.eventBusSub = this.eventBusService.on('logout', () => this.logout);
  }

  ngOnDestroy() {
    if (this.eventBusSub) {
      this.eventBusSub.unsubscribe();
    }
  }

  public get userValue(): AuthUser | null {
    return this.userSubject.value;
  }

  getUser(): AuthUser | null {
    /*const fromStorage = sessionStorage.getItem(USER_KEY);
    if (fromStorage) {
      return JSON.parse(fromStorage);
    }
    return null;*/

    return {
      id: 'abc',
      username: 'user',
      roles: [
        RoleType.Admin,
        RoleType.Manager,
        RoleType.User,
      ],
      lastLogin: DateTime.now().toISODate()
    }
  }

  logout(): Observable<any> {
    // remove user from local storage
    //var logoutRequest = { userId: id };

    console.info("AuthService: logout called");
    return this.http.post<string>('/api/auth/logout', null )
        .pipe(tap(() => {
          sessionStorage.clear();
          this.userSubject.next(null);
        }));

  }

  isInRoles(role: RoleType): boolean {
    if (this.userValue && this.userValue.roles && role) {
      return this.userValue.roles.includes(role);
    } else {
      return false;
    }
  }

  isAuthenticated(): boolean {
    //return true;
    return (this.getUser() != null);
  }
}
