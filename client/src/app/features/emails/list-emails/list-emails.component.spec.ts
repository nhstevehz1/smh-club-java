import {ComponentFixture, fakeAsync, TestBed, tick} from '@angular/core/testing';
import {provideHttpClient} from '@angular/common/http';
import {provideHttpClientTesting} from '@angular/common/http/testing';
import {provideNoopAnimations} from '@angular/platform-browser/animations';

import {of} from 'rxjs';
import {TranslateModule} from '@ngx-translate/core';

import {AuthService} from '@app/core/auth/services/auth.service';
import {asyncData} from '@app/shared/testing/test-helpers';

import {EmailTest} from '@app/features/emails/testing/test-support';

import {ListEmailsComponent} from './list-emails.component';
import {EmailMember, Email} from '@app/features/emails/models/email';
import {EmailTableService} from '@app/features/emails/services/email-table.service';
import {EmailEditDialogService} from '@app/features/emails/services/email-edit-dialog.service';
import {EmailService} from '@app/features/emails/services/email.service';
import {EditAction, EditEvent, EditDialogInput, EditDialogResult} from '@app/shared/components/base-edit-dialog/models';
import {EmailEditorComponent} from '@app/features/emails/email-editor/email-editor.component';

describe('ListEmailsComponent', () => {
  let fixture: ComponentFixture<ListEmailsComponent>;
  let component: ListEmailsComponent;

  let emailSvcMock: jasmine.SpyObj<EmailService>;
  let tableSvcMock: jasmine.SpyObj<EmailTableService>
  let authSvcMock: jasmine.SpyObj<AuthService>;
  let dialogSvcMock: jasmine.SpyObj<EmailEditDialogService>;

  const columnDefs = EmailTest.generateColumDefs();
  const data =  EmailTest.generatePagedData(0, 5, 1);

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

    emailSvcMock.getPagedData.and.returnValue(asyncData(data));
    tableSvcMock.getColumnDefs.and.returnValue(columnDefs);
  });

  it('should create', async () => {
    fixture.detectChanges();
    await fixture.whenStable();
    expect(component).toBeTruthy();
  });

  describe('test dialog interactions', () => {
    let editEvent: EditEvent<EmailMember>;
    let dialogInput: EditDialogInput<Email, EmailEditorComponent>;
    let dialogResult: EditDialogResult<Email>;

    beforeEach(() => {
      editEvent = {
        idx: 0,
        data: EmailTest.generateEmailMember(0)
      }
      dialogInput = EmailTest.generateDialogInput(editEvent.data, EditAction.Edit);
      dialogResult = {context: editEvent.data, action: EditAction.Cancel};
    });

    describe('onDelete', () => {
      it('onDeleteClick should call EmailService.generateEmailDialogInput', fakeAsync(() => {
        dialogSvcMock.openDialog.and.returnValue(of(dialogResult));
        const spy =
          dialogSvcMock.generateDialogInput.and.returnValue(dialogInput);

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

      it('onDeleteClick should NOT call EmailService.delete when open dialog returns cancel', fakeAsync(() => {
        dialogResult.action = EditAction.Cancel;
        dialogSvcMock.openDialog.and.returnValue(of(dialogResult));
        const spy = emailSvcMock.delete.and.stub();

        component.onDeleteClick(editEvent);
        tick();

        expect(spy).toHaveBeenCalledTimes(0);
      }));

      it('onDeleteClick should call EmailService.delete when open dialog returns delete', fakeAsync(() => {
        dialogResult.action = EditAction.Delete;
        dialogSvcMock.openDialog.and.returnValue(of(dialogResult));
        const spy = emailSvcMock.delete.and.returnValue(of(void 0));

        component.onDeleteClick(editEvent);
        tick();

        expect(spy).toHaveBeenCalled();
      }));
    });

    describe('onEditClick', () => {
      it('onEditClick should call EmailDialogService..generateDialogInput', fakeAsync(() => {
        dialogSvcMock.openDialog.and.returnValue(of(dialogResult));
        const spy =
          dialogSvcMock.generateDialogInput.and.returnValue(dialogInput);

        component.onEditClick(editEvent);
        tick();

        expect(spy).toHaveBeenCalled();
      }));

      it('onEditClick should call EmailEditDialogService.openDialog', fakeAsync(() => {
        const spy =
          dialogSvcMock.openDialog.and.returnValue(of({context: EmailTest.generateEmail(), action: EditAction.Cancel}));

        component.onEditClick(editEvent);
        tick();

        expect(spy).toHaveBeenCalled();
      }));

      it('onEditClick should NOT call EmailService.delete when open dialog returns cancel', fakeAsync(() => {

          dialogResult.action = EditAction.Cancel;
          dialogSvcMock.openDialog.and.returnValue(of(dialogResult));
          const spy = emailSvcMock.delete.and.stub();

          component.onEditClick(editEvent);
          tick();

          expect(spy).toHaveBeenCalledTimes(0);
        }));

      it('onEditClick should call EmailService.updateEMail when open dialog returns edit', fakeAsync(() => {

        dialogResult.action = EditAction.Edit;
        dialogSvcMock.openDialog.and.returnValue(of(dialogResult));
        const spy =
          emailSvcMock.update.and.returnValue(of(dialogResult.context));

        component.onEditClick(editEvent);
        tick();

        expect(spy).toHaveBeenCalled();
      }));
    });
  });
});
