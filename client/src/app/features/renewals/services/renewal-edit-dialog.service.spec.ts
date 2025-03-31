import { TestBed } from '@angular/core/testing';

import { RenewalEditDialogService } from '@app/features/renewals';

describe('RenewalEditDialogService', () => {
  let service: RenewalEditDialogService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RenewalEditDialogService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
