import { TestBed } from '@angular/core/testing';

import { PhoneEditDialogService } from '@app/features/phones';

describe('PhoneEditDialogService', () => {
  let service: PhoneEditDialogService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PhoneEditDialogService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
