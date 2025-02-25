import {TestBed} from '@angular/core/testing';
import {CanActivateFn, provideRouter, Router} from '@angular/router';

import {readGuard} from './read.guard';
import {AuthService} from "../services/auth.service";
import {Observable, Subject} from "rxjs";

describe('authGuard', () => {
  const executeGuard: CanActivateFn = (...guardParameters) => 
      TestBed.runInInjectionContext(() => readGuard(...guardParameters));

  let authServiceMock: jasmine.SpyObj<AuthService>;
  let routerMock: jasmine.SpyObj<Router>;

  let isAuthedSubject$: Subject<boolean>;
  let isAuthedEvent$: Observable<boolean>

  beforeEach(async () => {
    authServiceMock = jasmine.createSpyObj('AuthService', ['isAuthenticated$']);
    routerMock = jasmine.createSpyObj('Router', ['navigate']);

    isAuthedSubject$ = new Subject<boolean>;
    isAuthedEvent$ = isAuthedSubject$.asObservable();

    TestBed.configureTestingModule({
      providers: [
        provideRouter([]),
        {provide: AuthService, useValue: authServiceMock},
        {provide: Router, useValue: routerMock}
      ]
    });

    authServiceMock.isAuthenticated$ = isAuthedEvent$;
  });

  it('should be created', () => {
    expect(executeGuard).toBeTruthy();
  });

  it('should allow when AuthService.isAuthenticated  is true', async  () => {
    const spyNav =
        routerMock.navigate.and.returnValue(Promise.resolve(true));
    const spyAuth =
        spyOn(authServiceMock.isAuthenticated$, 'pipe').and.callThrough();

    const route: any = {};
    const state: any = {};

    const result = executeGuard(route,state) as Observable<boolean>;

    result.subscribe(val => {
      expect(val).toBeTrue();
      expect(spyAuth).toHaveBeenCalled()
      expect(spyNav).not.toHaveBeenCalled();
    });

    isAuthedSubject$.next(true);
  });

  it('should call navigate when AuthService.isAuthenticated is false', async () => {
    const spyNav =
        routerMock.navigate.and.returnValue(Promise.resolve(true));
    const spyAuth =
        spyOn(authServiceMock.isAuthenticated$, 'pipe').and.callThrough();

    const route: any = {};
    const state: any = {};

    const result = executeGuard(route, state) as Observable<boolean>

    result.subscribe(val => {
      expect(val).not.toBeTrue();
      expect(spyAuth).toHaveBeenCalled();
      expect(spyNav).toHaveBeenCalledWith(['p/login']);
    });

    isAuthedSubject$.next(false);
  });
});
