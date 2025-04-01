import { TestBed } from '@angular/core/testing';

import { MemberEditDialogService } from './member-edit-dialog.service';

describe('MemberEditDialogService', () => {
  let service: MemberEditDialogService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MemberEditDialogService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
