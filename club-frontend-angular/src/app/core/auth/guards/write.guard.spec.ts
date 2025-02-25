import { TestBed } from '@angular/core/testing';
import {CanActivateFn, provideRouter, Router} from '@angular/router';

import { writeGuard } from './write.guard';
import {AuthService} from "../services/auth.service";
import {Observable, Subject} from "rxjs";
import {PermissionType} from "../models/permission-type";

describe('writeGuard', () => {
  const executeGuard: CanActivateFn = (...guardParameters) => 
      TestBed.runInInjectionContext(() => writeGuard(...guardParameters));

  let authServiceMock: jasmine.SpyObj<AuthService>;
  let routerMock: jasmine.SpyObj<Router>;

  let rolesLoadedSubject$: Subject<boolean>;
  let rolesLoaded$: Observable<boolean>;

  const route: any = {};
  const state: any = {};

  beforeEach(() => {
    authServiceMock =
        jasmine.createSpyObj('AuthService', ['rolesLoaded$', 'isLoggedIn', 'hasPermission']);
    routerMock = jasmine.createSpyObj('Router', ['navigate']);

    rolesLoadedSubject$ = new Subject<boolean>;
    rolesLoaded$ = rolesLoadedSubject$.asObservable()

    TestBed.configureTestingModule({
      providers: [
        provideRouter([]),
        {provide: AuthService, useValue: authServiceMock},
        {provide: Router, useValue: routerMock}
      ]
    });

    authServiceMock.rolesLoaded$ = rolesLoaded$;
  });

  it('should be created', () => {
    expect(executeGuard).toBeTruthy();
  });

  it('expect AuthService.isLoggedIn to be called', async () => {
    const spy = authServiceMock.isLoggedIn.and.returnValue(true);
    authServiceMock.hasPermission.and.returnValue(true);

    const result = executeGuard(route, state) as Observable<boolean>;

    result.subscribe(() => {
      expect(spy).toHaveBeenCalled();
    });

    rolesLoadedSubject$.next(true);
  });

  it('expect AuthService.hasPermissions to be called', async () => {
    authServiceMock.isLoggedIn.and.returnValue(true);
    const spy = authServiceMock.hasPermission.and.returnValue(true);

    const result = executeGuard(route, state) as Observable<boolean>;

    result.subscribe(() => {
      expect(spy).toHaveBeenCalledWith(PermissionType.write);
    });

    rolesLoadedSubject$.next(true);
  });

  it('expect router.navigate to NOT be called', async () => {
    authServiceMock.isLoggedIn.and.returnValue(true);
    authServiceMock.hasPermission.and.returnValue(true);
    const spy = routerMock.navigate.and.stub();

    const result = executeGuard(route, state) as Observable<boolean>;

    result.subscribe(() => {
      expect(spy).not.toHaveBeenCalled();
    });

    rolesLoadedSubject$.next(true);
  });

  it('should return true', async () => {
    authServiceMock.isLoggedIn.and.returnValue(true);
    authServiceMock.hasPermission.and.returnValue(true);

    const result = executeGuard(route,state) as Observable<boolean>;

    result.subscribe(val => {
      expect(val).toBeTrue();
    });

    rolesLoadedSubject$.next(true);
  });

  it('should call router.navigate with p/login', async () => {
    const spyIsLoggedIn = authServiceMock.isLoggedIn.and.returnValue(false);
    const spyPermissions = authServiceMock.hasPermission.and.returnValue(true);
    const spyNav = routerMock.navigate.and.returnValue(Promise.resolve(true))
    const result = executeGuard(route,state) as Observable<boolean>;

    result.subscribe(val => {
      expect(val).toBeTrue()
      expect(spyNav).toHaveBeenCalledWith(['p/login']);
      expect(spyIsLoggedIn).toHaveBeenCalled();
      expect(spyPermissions).not.toHaveBeenCalled();
    });

    rolesLoadedSubject$.next(true);
  });

  it('should call router.navigate with p/access-denied', async () => {
    const spyIsLoggedIn = authServiceMock.isLoggedIn.and.returnValue(true);
    const spyPermissions = authServiceMock.hasPermission.and.returnValue(false);
    const spyNav = routerMock.navigate.and.returnValue(Promise.resolve(true))
    const result = executeGuard(route,state) as Observable<boolean>;

    result.subscribe(val => {
      expect(val).toBeTrue()
      expect(spyNav).toHaveBeenCalledWith(['p/access-denied']);
      expect(spyIsLoggedIn).toHaveBeenCalled();
      expect(spyPermissions).toHaveBeenCalled();
    });

    rolesLoadedSubject$.next(true);
  });
});
