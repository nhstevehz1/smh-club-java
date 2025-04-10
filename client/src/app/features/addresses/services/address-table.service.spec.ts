import { TestBed } from '@angular/core/testing';

import { AddressTableService } from './address-table.service';

describe('AddressTableService', () => {
  let service: AddressTableService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [AddressTableService]
    });
    service = TestBed.inject(AddressTableService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
