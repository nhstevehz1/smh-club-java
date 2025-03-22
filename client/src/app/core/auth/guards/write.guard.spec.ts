import { TestBed } from '@angular/core/testing';
import {ActivatedRouteSnapshot, CanActivateFn, provideRouter, Router} from '@angular/router';

import { writeGuard } from './write.guard';
import {AuthService} from "../services/auth.service";
import {Observable, Subject} from "rxjs";
import {PermissionType} from "../models/permission-type";

describe('writeGuard', () => {
  const executeGuard: CanActivateFn = (...guardParameters) =>
      TestBed.runInInjectionContext(() => writeGuard(...guardParameters));

  let authServiceMock: jasmine.SpyObj<AuthService>;
  let routerMock: jasmine.SpyObj<Router>;

  let isAuthedSubject$: Subject<boolean>;
  let isAuthedEvent$: Observable<boolean>;

  const route = new ActivatedRouteSnapshot();
  // suppressing es-lint.  any is acceptable.  state is not really used.
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  const state: any = {};

  beforeEach(() => {
    authServiceMock =
        jasmine.createSpyObj('AuthService', ['hasPermission']);
    routerMock = jasmine.createSpyObj('Router', ['navigate']);

    isAuthedSubject$ = new Subject<boolean>;
    isAuthedEvent$ = isAuthedSubject$.asObservable()

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

  it('writeGuard should call AuthService.hasPermissions to be called when isAuthenticated is true', async () => {
    const spy = authServiceMock.hasPermission.and.returnValue(true);
    routerMock.navigate.and.returnValue(Promise.resolve(true));

    const result = executeGuard(route, state) as Observable<boolean>;

    result.subscribe(() => expect(spy).toHaveBeenCalledWith(PermissionType.write));

    isAuthedSubject$.next(true);
  });

  it('writeGuard should call NOT AuthService.hasPermissions when isAuthenticated is false', async () => {
    const spy = authServiceMock.hasPermission.and.stub();
    routerMock.navigate.and.returnValue(Promise.resolve(true));

    const result = executeGuard(route, state) as Observable<boolean>;

    result.subscribe(() => expect(spy).not.toHaveBeenCalled());

    isAuthedSubject$.next(false);
  });

  it('writeGuard should call router.navigate with p/login when AuthService.isAuthenticated is false', async () => {
    const spy = routerMock.navigate.and.returnValue(Promise.resolve(true));
    const result = executeGuard(route,state) as Observable<boolean>;

    result.subscribe(() => expect(spy).toHaveBeenCalledWith(['p/login']));

    isAuthedSubject$.next(false);
  });

  it('writeGuard should call router.navigate with p/access-denied when AuthService.hasPermission returns false', async () => {
    const spy = routerMock.navigate.and.returnValue(Promise.resolve(true))
    authServiceMock.hasPermission.and.returnValue(false);

    const result = executeGuard(route,state) as Observable<boolean>;

    result.subscribe(() => expect(spy).toHaveBeenCalledWith(['p/access-denied']));

    isAuthedSubject$.next(true);
  });
});
