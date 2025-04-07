import { TestBed } from '@angular/core/testing';

import { MemberEditDialogService } from './member-edit-dialog.service';
import {MatDialog, MatDialogModule} from '@angular/material/dialog';
import {NonNullableFormBuilder} from '@angular/forms';

describe('MemberEditDialogService', () => {
  let service: MemberEditDialogService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [MemberEditDialogService]
    });
    service = TestBed.inject(MemberEditDialogService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
