
import {ComponentFixture, fakeAsync, TestBed, tick} from '@angular/core/testing';
import {HttpErrorResponse, provideHttpClient} from '@angular/common/http';
import {provideHttpClientTesting} from '@angular/common/http/testing';
import {provideNoopAnimations} from '@angular/platform-browser/animations';

import {of, throwError} from 'rxjs';
import {TranslateModule} from '@ngx-translate/core';

import {AuthService} from '@app/core/auth/services/auth.service';
import {PagedData, PageRequest} from '@app/shared/services/api-service';
import {asyncData} from '@app/shared/testing/test-helpers';

import {
  generateEmail,
  generateEmailColumnDefs,
  generateEmailDialogInput,
  generateEmailMember,
  generateEmailPagedData
} from '@app/features/emails/testing/test-support';

import {ListEmailsComponent} from './list-emails.component';
import {EmailMember, Email} from '@app/features/emails/models/email';
import {EmailTableService} from '@app/features/emails/services/email-table.service';
import {EmailEditDialogService} from '@app/features/emails/services/email-edit-dialog.service';
import {EmailService} from '@app/features/emails/services/email.service';
import {EditAction, EditEvent, EditDialogInput, EditDialogResult} from '@app/shared/components/edit-dialog/models';

describe('ListEmailsComponent', () => {
  let fixture: ComponentFixture<ListEmailsComponent>;
  let component: ListEmailsComponent;

  let emailSvcMock: jasmine.SpyObj<EmailService>;
  let tableSvcMock: jasmine.SpyObj<EmailTableService>
  let authSvcMock: jasmine.SpyObj<AuthService>;
  let dialogSvcMock: jasmine.SpyObj<EmailEditDialogService>;

  const columnDefs = generateEmailColumnDefs()

  beforeEach(async () => {
    emailSvcMock = jasmine.createSpyObj('EmailService',
      ['getPagedData', 'create', 'update', 'delete']);

    dialogSvcMock = jasmine.createSpyObj('EditEmailDialogService',
      ['openDialog', 'generateDialogInput']);

    tableSvcMock = jasmine.createSpyObj('EmailTableService', ['getColumnDefs']);
    authSvcMock = jasmine.createSpyObj('AuthService', ['hasPermission']);

    await TestBed.configureTestingModule({
      imports: [
        ListEmailsComponent,
        TranslateModule.forRoot({})
      ],
      providers: [
        {provide: EmailService, useValue: {}},
        {provide: AuthService, useValue: {}},
        {provide: EmailTableService, useValue: {}},
        {provide: EmailEditDialogService, useValue: {}},
        provideHttpClient(),
        provideHttpClientTesting(),
        provideNoopAnimations()
      ],
    }).overrideProvider(EmailService, {useValue: emailSvcMock})
      .overrideProvider(AuthService, {useValue: authSvcMock})
      .overrideProvider(EmailTableService, {useValue: tableSvcMock})
      .overrideProvider(EmailEditDialogService, {useValue: dialogSvcMock})
      .compileComponents();

    fixture = TestBed.createComponent(ListEmailsComponent);
    component = fixture.componentInstance;
  });

  describe('test component', () => {
    it('should create', () => {
        expect(component).toBeTruthy();
    });
  });

  describe('test service interactions on init', ()=> {
    describe('EmailTableService', () => {
      beforeEach(() => {
        const data = generateEmailPagedData(0, 5, 1);
        emailSvcMock.getPagedData.and.returnValue(asyncData(data));
      });

      it('should call EmailTableService.getColumnDefs', async () => {
        const spy = tableSvcMock.getColumnDefs.and.returnValue(columnDefs);

        fixture.detectChanges();
        await fixture.whenStable();

        expect(spy).toHaveBeenCalled();
      });

      it('should create correct column list', async () => {
        tableSvcMock.getColumnDefs.and.returnValue(columnDefs);

        fixture.detectChanges();
        await fixture.whenStable();

        expect(component.columns()).toEqual(columnDefs);
      });
    });

    describe('test EmailService.getPagedData interaction', () => {
      beforeEach(() => {
        tableSvcMock.getColumnDefs.and.returnValue(columnDefs);
      });

      it('should call EmailService.getEmails', async () => {
        const data = generateEmailPagedData(0, 5, 100);
        emailSvcMock.getPagedData.and.returnValue(asyncData(data));

        fixture.detectChanges();
        await fixture.whenStable();

        const request = PageRequest.of(0, 5);
        expect(emailSvcMock.getPagedData).toHaveBeenCalledOnceWith(request)
      });

      it('should set the correct data length', async () => {
        const data = generateEmailPagedData(0, 5, 100);
        emailSvcMock.getPagedData.and.returnValue(asyncData(data));

        fixture.detectChanges();
        await fixture.whenStable();

        expect(component.resultsLength()).toEqual(data.page.totalElements);
      });

      it('should set correct datasource.data', async () => {
        const data = generateEmailPagedData(0, 5, 2);
        emailSvcMock.getPagedData.and.returnValue(asyncData(data));

        fixture.detectChanges();
        await fixture.whenStable();

        expect(component.datasource().data).toBe(data._content);
      });

      it('datasource.data should be empty when an error occurs while calling getPagedData', async () => {
        emailSvcMock.getPagedData.and.returnValue(throwError(() => 'error'));

        fixture.detectChanges();
        await fixture.whenStable();

        expect(component.datasource().data).toEqual([]);
      });
    });
  });

  describe('test dialog interactions', () => {
    let editEvent: EditEvent<EmailMember>;
    let dialogInput: EditDialogInput<Email>;
    let dialogResult: EditDialogResult<Email>;
    let pagedData: PagedData<EmailMember>;
    let error: HttpErrorResponse;

    beforeEach(() => {
      const data = generateEmailPagedData(0, 5, 1);
      emailSvcMock.getPagedData.and.returnValue(asyncData(data));
      tableSvcMock.getColumnDefs.and.returnValue(columnDefs);

      editEvent = {
        idx: 0,
        data: generateEmailMember(0)
      }
      pagedData = generateEmailPagedData(0, 5, 1);
      dialogInput = generateEmailDialogInput(editEvent.data, EditAction.Edit);
      dialogResult = {context: editEvent.data, action: EditAction.Cancel};
      error = new HttpErrorResponse({status: 403, error: {}});
    });

    describe('onDelete', () => {
      it('onDeleteClick should call EmailService.generateEmailDialogInput', fakeAsync(() => {
        dialogSvcMock.openDialog.and.returnValue(of(dialogResult));
        const spy
          = dialogSvcMock.generateDialogInput.and.returnValue(dialogInput);

        component.onDeleteClick(editEvent);
        tick();

        expect(spy).toHaveBeenCalled();
      }));

      it('onDeleteClick should call EmailEditDialogService.openDialog', fakeAsync(() => {
        dialogResult.action = EditAction.Cancel;
        const spy =
          dialogSvcMock.openDialog.and.returnValue(of(dialogResult));

        component.onDeleteClick(editEvent);
        tick();

        expect(spy).toHaveBeenCalled();
      }));

      it('onDeleteClick should NOT call EmailService.update when EditEmailDialogService.open return cancel', fakeAsync(() => {
        dialogResult.action = EditAction.Cancel;
        dialogSvcMock.openDialog.and.returnValue(of(dialogResult));
        const spy = emailSvcMock.update.and.stub();

        component.onDeleteClick(editEvent);
        tick();

        expect(spy).toHaveBeenCalledTimes(0);
      }));

      it('onDeleteClick should NOT call EmailService.deleteEmail when EditEmailDialogService.open return cancel', fakeAsync(() => {
          dialogResult.action = EditAction.Cancel;
          dialogSvcMock.openDialog.and.returnValue(of(dialogResult));
          const spy = emailSvcMock.delete.and.stub();

          component.onDeleteClick(editEvent);
          tick();

          expect(spy).toHaveBeenCalledTimes(0);
      }));

      it('onDeleteClick should call EmailService.delete when EditEmailDialogService.open returns delete', fakeAsync(() => {
        dialogResult.action = EditAction.Delete;
        dialogSvcMock.openDialog.and.returnValue(of(dialogResult));
        const spy = emailSvcMock.delete.and.returnValue(of(void 0));

        component.onDeleteClick(editEvent);
        tick();

        expect(spy).toHaveBeenCalled();
      }));

      it('onDeleteClick should call EmailService.getPagedData when EditEmailDialogService.open returns delete', fakeAsync(() => {
        dialogResult.action = EditAction.Delete;
        dialogSvcMock.openDialog.and.returnValue(of(dialogResult));
        emailSvcMock.delete.and.returnValue(of(void 0));
        const spy = emailSvcMock.getPagedData.and.returnValue(of(pagedData));

        component.onDeleteClick(editEvent);
        tick();

        expect(spy).toHaveBeenCalled();
      }));

      it('onDeleteEmail should set datasource', fakeAsync(() => {
        dialogResult.action = EditAction.Delete;
        dialogSvcMock.openDialog.and.returnValue(of(dialogResult));
        emailSvcMock.delete.and.returnValue(of(void 0));
        emailSvcMock.getPagedData.and.returnValue(of(pagedData));

        component.onDeleteClick(editEvent);
        tick();

        expect(component.datasource().data).toEqual(pagedData._content);
      }));

      it('onDeleteClick should NOT call EmailService.getPagedData when EmailService.deleteEmail returns error', fakeAsync(() => {
        dialogResult.action = EditAction.Delete;
        dialogSvcMock.openDialog.and.returnValue(of(dialogResult));
        emailSvcMock.delete.and.returnValue(throwError(() => error));
        const spy = emailSvcMock.getPagedData.and.returnValue(of(pagedData));

        component.onDeleteClick(editEvent);
        tick();

        expect(spy).toHaveBeenCalledTimes(0);
      }));

      it('onDeleteClick should NOT update datasource when EmailService.getPagedData returns error', fakeAsync(() => {
        dialogResult.action = EditAction.Delete;
        dialogSvcMock.openDialog.and.returnValue(asyncData(dialogResult));
        emailSvcMock.delete.and.returnValue(asyncData(void 0));
        const expected = component.datasource().data;
        emailSvcMock.getPagedData.and.returnValue(throwError(() => error));

        component.onDeleteClick(editEvent);
        tick();

        expect(component.datasource().data).toEqual(expected);
      }));
    });

    describe('onEditClick', () => {
      it('onEditClick should call EmailService.generate', fakeAsync(() => {
        dialogSvcMock.openDialog.and.returnValue(of(dialogResult));
        const spy = dialogSvcMock.generateDialogInput.and.returnValue(dialogInput);

        component.onEditClick(editEvent);
        tick();

        expect(spy).toHaveBeenCalled();
      }));

      it('onEditClick should call EmailEditDialogService.openDialog', fakeAsync(() => {
        const spy =
          dialogSvcMock.openDialog.and.returnValue(of({context: generateEmail(), action: EditAction.Cancel}));

        component.onEditClick(editEvent);
        tick();

        expect(spy).toHaveBeenCalled();
      }));

      it('onEditClick should NOT call EmailService.update when EditEmailDialogService.open return cancel',
        fakeAsync(() => {

          dialogResult.action = EditAction.Cancel;
          dialogSvcMock.openDialog.and.returnValue(of(dialogResult));
          const spy = emailSvcMock.update.and.stub();

          component.onEditClick(editEvent);
          tick();

          expect(spy).toHaveBeenCalledTimes(0);
        }));

      it('onEditClick should NOT call EmailService.deleteEmail when EditEmailDialogService.open return cancel',
        fakeAsync(() => {

          dialogResult.action = EditAction.Cancel;
          dialogSvcMock.openDialog.and.returnValue(of(dialogResult));
          const spy = emailSvcMock.delete.and.stub();

          component.onEditClick(editEvent);
          tick();

          expect(spy).toHaveBeenCalledTimes(0);
        }));

      it('onEditClick should call EmailService.updateEMail when EditEmailDialogService.open returns edit', fakeAsync(() => {

        dialogResult.action = EditAction.Edit;
        dialogSvcMock.openDialog.and.returnValue(of(dialogResult));
        const spy = emailSvcMock.update.and.returnValue(of(dialogResult.context));

        component.onEditClick(editEvent);
        tick();

        expect(spy).toHaveBeenCalled();
      }));

      it('onEditClick should call EmailService.getPagedData when EditEmailDialogService.open returns edit', fakeAsync(() => {
        dialogResult.action = EditAction.Edit;
        dialogSvcMock.openDialog.and.returnValue(of(dialogResult));
        emailSvcMock.update.and.returnValue(of(dialogResult.context));
        const spy = emailSvcMock.getPagedData.and.returnValue(of(pagedData));

        component.onEditClick(editEvent);
        tick();

        expect(spy).toHaveBeenCalled();
      }));

      it('onEditClick should set datasource', fakeAsync(() => {
        dialogResult.action = EditAction.Edit;
        dialogSvcMock.openDialog.and.returnValue(of(dialogResult));
        emailSvcMock.update.and.returnValue(of(dialogResult.context));
        emailSvcMock.getPagedData.and.returnValue(of(pagedData));

        component.onEditClick(editEvent);
        tick();

        expect(component.datasource().data).toEqual(pagedData._content);
      }));

      it('onEditClick should NOT call EmailService.getPagedData when EmailService.update returns error', fakeAsync(() => {
        dialogResult.action = EditAction.Edit;
        dialogSvcMock.openDialog.and.returnValue(of(dialogResult));
        emailSvcMock.update.and.returnValue(throwError(() => error));
        const spy = emailSvcMock.getPagedData.and.returnValue(of(pagedData));

        component.onEditClick(editEvent);
        tick();

        expect(spy).toHaveBeenCalledTimes(0);
      }));

      it('onEditClick should NOT update datasource when EmailService.getPagedData returns error', fakeAsync(() => {
        dialogResult.action = EditAction.Edit;
        dialogSvcMock.openDialog.and.returnValue(of(dialogResult));
        emailSvcMock.update.and.returnValue(of(dialogResult.context));
        const expected = component.datasource().data;
        emailSvcMock.getPagedData.and.returnValue(throwError(() => error));

        component.onEditClick(editEvent);
        tick();

        expect(component.datasource().data).toEqual(expected);
      }));
    });
  });
});
