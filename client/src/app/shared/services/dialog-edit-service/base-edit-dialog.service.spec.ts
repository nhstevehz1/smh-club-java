import {TestBed, fakeAsync, tick} from '@angular/core/testing';
import {MatDialog, MatDialogRef} from '@angular/material/dialog';

import {MockEditDialogService} from '@app/shared/services/dialog-edit-service/testing/mock-edit-dialog.service';
import {
  MockServiceDialogComponent,
  DialogServiceModel,
  MockServiceEditorComponent,
  DialogServiceTest
} from '@app/shared/services/dialog-edit-service/testing/test-support';
import {EditDialogInput, EditDialogResult} from '@app/shared/components/base-edit-dialog/models';
import {asyncData} from '@app/shared/testing';

describe('BaseEditDialogService', () => {
  let service: MockEditDialogService;
  let dialogMock: jasmine.SpyObj<MatDialog>
  let dialogRefMock: jasmine.SpyObj<MatDialogRef<MockServiceDialogComponent,
    EditDialogInput<DialogServiceModel, MockServiceEditorComponent>>>;

  let dialogInput: EditDialogInput<DialogServiceModel, MockServiceEditorComponent>
  let dialogResult: EditDialogResult<DialogServiceModel>;

  beforeEach(() => {
    dialogMock = jasmine.createSpyObj('MatDialog', ['open']);
    dialogRefMock = jasmine.createSpyObj('DialogRef', ['afterClosed']);

    dialogInput = DialogServiceTest.generateDialogInput();
    dialogResult = {context: dialogInput.context, action: dialogInput.action};

    TestBed.configureTestingModule({
      providers: [
        MockEditDialogService,
        {provide: MatDialog, useValue: dialogMock},
      ]
    });
    service = TestBed.inject(MockEditDialogService);
  });

  it('should be created', () => {
    dialogMock.open.and.returnValue(dialogRefMock);
    dialogRefMock.afterClosed.and.returnValue(asyncData(dialogInput));

    expect(service).toBeTruthy();
  });

  it('should call MatDialog.open', ()=> {
    dialogRefMock.afterClosed.and.returnValue(asyncData(dialogInput));
    const spy = dialogMock.open.and.returnValue(dialogRefMock);

    service.openDialog(dialogInput);

    expect(spy).toHaveBeenCalled();
  });

  it('should call MatDialogRef.afterClosed', () => {
    dialogMock.open.and.returnValue(dialogRefMock);
    const spy = dialogRefMock.afterClosed.and.returnValue(asyncData(dialogInput));

    service.openDialog(dialogInput);

    expect(spy).toHaveBeenCalledWith();
  });

  it('openDialog should return correct EditDialogResult', fakeAsync(() => {
    dialogMock.open.and.returnValue(dialogRefMock);
    dialogRefMock.afterClosed.and.returnValue(asyncData(dialogInput));

    service.openDialog(dialogInput).subscribe({
      next: (data) => {
        expect(data).toEqual(dialogResult)
      }
    });
    tick();
  }));
});
