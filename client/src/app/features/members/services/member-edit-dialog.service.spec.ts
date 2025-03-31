import { TestBed } from '@angular/core/testing';

import { MemberEditDialogService } from '@app/features/members';

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
