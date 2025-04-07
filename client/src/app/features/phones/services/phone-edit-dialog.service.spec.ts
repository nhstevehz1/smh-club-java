import { TestBed } from '@angular/core/testing';

import { PhoneEditDialogService } from './phone-edit-dialog.service';

describe('PhoneEditDialogService', () => {
  let service: PhoneEditDialogService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [PhoneEditDialogService]
    });
    service = TestBed.inject(PhoneEditDialogService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
