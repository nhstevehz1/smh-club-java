import {TestBed} from '@angular/core/testing';

import {EmailEditDialogService} from './email-edit-dialog.service';

describe('EmailEditDialogService', () => {
  let service: EmailEditDialogService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [EmailEditDialogService]
    });
    service = TestBed.inject(EmailEditDialogService);
  });

  it('should create', () => {
    expect(service).toBeTruthy();
  });
})
