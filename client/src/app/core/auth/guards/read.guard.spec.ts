import {TestBed} from '@angular/core/testing';
import {ActivatedRouteSnapshot, CanActivateFn, provideRouter, Router} from '@angular/router';

import {Observable, Subject} from 'rxjs';

import {AuthService} from '@app/core/auth/services/auth.service';
import {PermissionType} from '@app/core/auth/models/permission-type';
import {readGuard} from './read.guard';

describe('readGuard', () => {
  const executeGuard: CanActivateFn = (...guardParameters) =>
      TestBed.runInInjectionContext(() => readGuard(...guardParameters));

  let authServiceMock: jasmine.SpyObj<AuthService>;
  let routerMock: jasmine.SpyObj<Router>;

  let readIsAuthedSubject$: Subject<boolean>;
  let readIsAuthedEvent$: Observable<boolean>

  const route = new ActivatedRouteSnapshot();
  // suppressing es-lint.  any is acceptable.  state is not really used.
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  const state: any = {};

  beforeEach(async () => {
    authServiceMock = jasmine.createSpyObj('AuthService', ['hasPermission']);
    routerMock = jasmine.createSpyObj('Router', ['navigate']);

    readIsAuthedSubject$ = new Subject<boolean>;
    readIsAuthedEvent$ = readIsAuthedSubject$.asObservable();

    TestBed.configureTestingModule({
      providers: [
        provideRouter([]),
        {provide: AuthService, useValue: authServiceMock},
        {provide: Router, useValue: routerMock}
      ]
    });

    authServiceMock.isAuthenticated$ = readIsAuthedEvent$;
  });

  it('should be created', () => {
    expect(executeGuard).toBeTruthy();
  });

  it('readGuard should call AuthService.hasPermissions to be called when isAuthenticated is true', async  () => {
    const spy = authServiceMock.hasPermission.and.returnValue(true);
    routerMock.navigate.and.returnValue(Promise.resolve(true));

    const result = executeGuard(route, state) as Observable<boolean>;

    result.subscribe(() => expect(spy).toHaveBeenCalledWith(PermissionType.read));

    readIsAuthedSubject$.next(true);
  });

  it('readGuard should call NOT AuthService.hasPermissions when isAuthenticated is false', async () => {
    const spy = authServiceMock.hasPermission.and.stub();
    routerMock.navigate.and.returnValue(Promise.resolve(true));

    const result = executeGuard(route, state) as Observable<boolean>;

    result.subscribe(() => expect(spy).not.toHaveBeenCalled());

    readIsAuthedSubject$.next(false);
  });

  it('readGuard should call router.navigate with p/login when AuthService.isAuthenticated is false', async () => {
    const spy = routerMock.navigate.and.returnValue(Promise.resolve(true));
    const result = executeGuard(route,state) as Observable<boolean>;

    result.subscribe(() => expect(spy).toHaveBeenCalledWith(['p/login']));

    readIsAuthedSubject$.next(false);
  });

  it('readGuard should call router.navigate with p/access-denied when AuthService.hasPermission returns false', async () => {
    const spy = routerMock.navigate.and.returnValue(Promise.resolve(true))
    authServiceMock.hasPermission.and.returnValue(false);

    const result = executeGuard(route,state) as Observable<boolean>;

    result.subscribe(() => expect(spy).toHaveBeenCalledWith(['p/access-denied']));

    readIsAuthedSubject$.next(true);
  });
});
