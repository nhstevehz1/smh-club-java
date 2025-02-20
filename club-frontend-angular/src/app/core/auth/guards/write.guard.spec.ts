import { TestBed } from '@angular/core/testing';
import { CanActivateFn } from '@angular/router';

import { writeGuard } from './write.guard';

describe('writeGuard', () => {
  const executeGuard: CanActivateFn = (...guardParameters) => 
      TestBed.runInInjectionContext(() => writeGuard(...guardParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(executeGuard).toBeTruthy();
  });
});
