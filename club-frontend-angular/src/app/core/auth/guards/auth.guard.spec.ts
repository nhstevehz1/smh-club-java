import {TestBed} from '@angular/core/testing';
import {CanActivateFn, provideRouter} from '@angular/router';

import {authGuard} from './auth.guard';
import {AuthService} from "../services/auth.service";
import {AddressService} from "../../../features/addresses/services/address.service";

describe('authGuard', () => {
  const executeGuard: CanActivateFn = (...guardParameters) => 
      TestBed.runInInjectionContext(() => authGuard(...guardParameters));
  let authServiceMock: jasmine.SpyObj<AuthService>;

  beforeEach(async () => {
    authServiceMock = jasmine.createSpyObj('AuthService', ['hasPermission']);
    TestBed.configureTestingModule({
      providers: [
        provideRouter([]),
        {provide: AddressService, useValue: authServiceMock},
      ]
    });
  });

  it('should be created', () => {
    expect(executeGuard).toBeTruthy();
  });
});
