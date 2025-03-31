import { TestBed } from '@angular/core/testing';

import { RenewalTableService } from '@app/features/renewals';

describe('RenewalTableService', () => {
  let service: RenewalTableService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [RenewalTableService]
    });
    service = TestBed.inject(RenewalTableService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
