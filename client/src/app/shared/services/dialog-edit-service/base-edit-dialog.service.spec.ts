import {TestBed} from '@angular/core/testing';
import {NonNullableFormBuilder} from '@angular/forms';
import {MatDialog} from '@angular/material/dialog';
import {DialogRef} from '@angular/cdk/dialog';

import {MockEditDialogService} from '@app/shared/services/dialog-edit-service/testing/mock-edit-dialog.service';

describe('BaseEditDialogService', () => {
  let service: MockEditDialogService;
  let dialogMock: jasmine.SpyObj<MatDialog>
  let dialogRef: jasmine.SpyObj<DialogRef<any, any>>;

  beforeEach(() => {
    dialogMock = jasmine.createSpyObj('MatDialog', ['open']);
    dialogRef = jasmine.createSpyObj('DialogRef', ['afterClosed']);

    TestBed.configureTestingModule({
      providers: [
        MockEditDialogService,
        NonNullableFormBuilder,
        {provide: MatDialog, useValue: dialogMock}
      ]
    });
    service = TestBed.inject(MockEditDialogService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
