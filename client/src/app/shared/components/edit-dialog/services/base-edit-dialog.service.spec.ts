import {TestBed} from '@angular/core/testing';
import {MockEditDialogService} from './test-support/mock-edit-dialog.service';
import {NonNullableFormBuilder} from '@angular/forms';
import {MatDialog} from '@angular/material/dialog';

describe('BaseEditDialogService', () => {
  let service: MockEditDialogService;
  let dialogMock: jasmine.SpyObj<MatDialog>

  beforeEach(() => {
    dialogMock = jasmine.createSpyObj('MatDialog', ['open']);

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
