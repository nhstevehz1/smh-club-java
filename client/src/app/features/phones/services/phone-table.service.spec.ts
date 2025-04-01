import { TestBed } from '@angular/core/testing';

import { PhoneTableService } from './phone-table.service';

describe('PhoneTableService', () => {
  let service: PhoneTableService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [PhoneTableService]
    });
    service = TestBed.inject(PhoneTableService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
