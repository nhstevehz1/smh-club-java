import {ComponentFixture, fakeAsync, TestBed, tick} from '@angular/core/testing';
import {provideHttpClient} from '@angular/common/http';
import {provideHttpClientTesting} from '@angular/common/http/testing';
import {provideNoopAnimations} from '@angular/platform-browser/animations';

import {of} from 'rxjs';
import {TranslateModule} from '@ngx-translate/core';

import {AuthService} from '@app/core/auth/services/auth.service';

import {asyncData} from '@app/shared/testing';
import {EditAction, EditDialogInput, EditDialogResult, EditEvent} from '@app/shared/components/base-edit-dialog/models';

import {PhoneTest} from '@app/features/phones/testing';

import {ListPhonesComponent} from '@app/features/phones/list-phones/list-phones.component';
import {PhoneService} from '@app/features/phones/services/phone.service';
import {PhoneEditDialogService} from '@app/features/phones/services/phone-edit-dialog.service';
import {PhoneTableService} from '@app/features/phones/services/phone-table.service';
import {PhoneMember, Phone} from '@app/features/phones/models/phone';
import {PhoneEditorComponent} from '@app/features/phones/phone-editor/phone-editor.component';

describe('ListPhonesComponent', () => {
  let fixture: ComponentFixture<ListPhonesComponent>;
  let component: ListPhonesComponent;

  let phoneSvcMock: jasmine.SpyObj<PhoneService>;
  let tableSvcMock: jasmine.SpyObj<PhoneTableService>;
  let authSvcMock: jasmine.SpyObj<AuthService>;
  let dialogSvcMock: jasmine.SpyObj<PhoneEditDialogService>;

  const columnDefs = PhoneTest.generatePhoneColumnDefs();
  const data = PhoneTest.generatePageData(0, 5, 1);

  beforeEach(async () => {
    phoneSvcMock = jasmine.createSpyObj('PhoneService',
      ['getPagedData', 'create', 'update', 'delete']);

    dialogSvcMock = jasmine.createSpyObj('PhoneEditDialogService',
      ['openDialog', 'generateDialogInput']);

    tableSvcMock = jasmine.createSpyObj('PhoneTableService', ['getColumnDefs']);
    authSvcMock = jasmine.createSpyObj('AuthService', ['hasPermission']);

    await TestBed.configureTestingModule({
      imports: [
          ListPhonesComponent,
          TranslateModule.forRoot({})
      ],
      providers: [
        {provide: PhoneService, useValue: {}},
        {provide: AuthService, useValue: {}},
        {provide: PhoneTableService, useValue: {}},
        {provide: PhoneEditDialogService, useValue: {}},
        provideHttpClient(),
        provideHttpClientTesting(),
        provideNoopAnimations()
      ]
    }).overrideProvider(PhoneService, {useValue: phoneSvcMock})
      .overrideProvider(AuthService, {useValue: authSvcMock})
      .overrideProvider(PhoneTableService, {useValue: tableSvcMock})
      .overrideProvider(PhoneEditDialogService, {useValue: dialogSvcMock})
      .compileComponents();

    fixture = TestBed.createComponent(ListPhonesComponent);
    component = fixture.componentInstance;

    phoneSvcMock.getPagedData.and.returnValue(asyncData(data));
    tableSvcMock.getColumnDefs.and.returnValue(columnDefs);
  });

  it('should create', async () => {
    fixture.detectChanges();
    await fixture.whenStable();
    expect(component).toBeTruthy();
  });

  describe('test dialog interactions', ()=> {
    let editEvent: EditEvent<PhoneMember>;
    let dialogInput: EditDialogInput<Phone, PhoneEditorComponent>;
    let dialogResult: EditDialogResult<Phone>;

    beforeEach(() => {
      editEvent = {
        idx: 0,
        data: PhoneTest.generatePhoneMember(0)
      }
      const phone = PhoneTest.generatePhone();
      dialogInput = PhoneTest.generateDialogInput(phone, EditAction.Edit);
      dialogResult = {context: phone, action: EditAction.Cancel};
    });

    describe('onEditClick', () => {
      it('onEditClick should call PhoneDialogService.generateDialogInput', fakeAsync(() => {
        dialogSvcMock.openDialog.and.returnValue(of(dialogResult));
        const spy =
          dialogSvcMock.generateDialogInput.and.returnValue(dialogInput);

        component.onEditClick(editEvent);
        tick();

        expect(spy).toHaveBeenCalled();
      }));

      it('onEditClick should call PhoneDialogService.openDialog', fakeAsync(() => {
        dialogResult.action = EditAction.Cancel;
        const spy =
          dialogSvcMock.openDialog.and.returnValue(of(dialogResult));

        component.onEditClick(editEvent);
        tick();

        expect(spy).toHaveBeenCalled();
      }));

      it('onEditClick should NOT call PhoneService.update when dialog service returns cancel', fakeAsync(() => {
        dialogResult.action = EditAction.Cancel;
        dialogSvcMock.openDialog.and.returnValue(of(dialogResult));
        const spy = phoneSvcMock.update.and.stub();

        component.onEditClick(editEvent);
        tick();

        expect(spy).toHaveBeenCalledTimes(0);
      }));


      it('onEditClick should call PhoneService.update when dialog services returns edit', fakeAsync(() => {
        dialogResult.action = EditAction.Edit;
        dialogSvcMock.openDialog.and.returnValue(of(dialogResult));
        const spy = phoneSvcMock.update.and.stub();

        component.onEditClick(editEvent);
        tick();

        expect(spy).toHaveBeenCalled();
      }));

    });

    describe('onDeleteClick', () => {
      it('onDeleteClick should call PhoneDialogService.generateDialogInput', fakeAsync(() => {
        dialogSvcMock.openDialog.and.returnValue(of(dialogResult));
        const spy =
          dialogSvcMock.generateDialogInput.and.returnValue(dialogInput);

        component.onDeleteClick(editEvent);
        tick();

        expect(spy).toHaveBeenCalled();
      }));

      it('onDeleteClick should call PhoneDialogService.openDialog', fakeAsync(() => {
        dialogResult.action = EditAction.Cancel;
        const spy =
          dialogSvcMock.openDialog.and.returnValue(of(dialogResult));

        component.onDeleteClick(editEvent);
        tick();

        expect(spy).toHaveBeenCalled();
      }));

      it('onDeleteClick should NOT call PhoneService.delete when dialog service returns cancel', fakeAsync(() => {
        dialogResult.action = EditAction.Cancel;
        dialogSvcMock.openDialog.and.returnValue(of(dialogResult));
        const spy = phoneSvcMock.delete.and.stub();

        component.onDeleteClick(editEvent);
        tick();

        expect(spy).toHaveBeenCalledTimes(0);
      }));


      it('onDeleteClick should call PhoneService.delete when dialog services returns delete', fakeAsync(() => {
        dialogResult.action = EditAction.Delete;
        dialogSvcMock.openDialog.and.returnValue(of(dialogResult));
        const spy = phoneSvcMock.delete.and.returnValue(of(void 0));

        component.onDeleteClick(editEvent);
        tick();

        expect(spy).toHaveBeenCalled();
      }));

    });
  });
});
