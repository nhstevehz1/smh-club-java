import {ListEmailsComponent} from './list-emails.component';
import {ComponentFixture, fakeAsync, TestBed, tick} from "@angular/core/testing";
import {EmailService} from "../services/email.service";
import {HttpErrorResponse, provideHttpClient} from "@angular/common/http";
import {provideHttpClientTesting} from "@angular/common/http/testing";
import {provideNoopAnimations} from "@angular/platform-browser/animations";
import {
  generateColumnDefs,
  generateEmail,
  generateEmailDialogInput,
  generateEmailMember,
  generateEmailPagedData
} from "../test/email-test";
import {asyncData} from "../../../shared/test-helpers/test-helpers";
import {PageRequest} from "../../../shared/models/page-request";
import {of, throwError} from "rxjs";
import {TranslateModule} from "@ngx-translate/core";
import {AuthService} from '../../../core/auth/services/auth.service';
import {Email, EmailMember} from '../models/email';
import {EditAction, EditDialogInput, EditDialogResult, EditEvent} from '../../../shared/components/edit-dialog/models';
import {EmailEditDialogService} from '../services/email-edit-dialog.service';
import createSpyObj = jasmine.createSpyObj;
import {PagedData} from '../../../shared/models/paged-data';

describe('ListEmailsComponent', () => {
  let fixture: ComponentFixture<ListEmailsComponent>;
  let component: ListEmailsComponent;

  let emailSvcMock: jasmine.SpyObj<EmailService>;
  let authSvcMock: jasmine.SpyObj<AuthService>;
  let dialogSvcMock: jasmine.SpyObj<EmailEditDialogService>;

  const columnDefs = generateColumnDefs()

  beforeEach(async () => {
    emailSvcMock = jasmine.createSpyObj('EmailService',
      ['getPagedData', 'getColumnDefs',
        'generateEmailForm', 'updateEmail',
        'deleteEmail', 'generateEmailDialogInput']);

    authSvcMock = jasmine.createSpyObj('AuthService', ['hasPermission']);
    dialogSvcMock = createSpyObj('EditEmailDialogService', ['openDialog']);

    await TestBed.configureTestingModule({
      imports: [
        ListEmailsComponent,
        TranslateModule.forRoot({})
      ],
      providers: [
        {provide: EmailService, useValue: {}},
        {provide: AuthService, useValue: {}},
        {provide: EmailEditDialogService, useValue: {}},
        provideHttpClient(),
        provideHttpClientTesting(),
        provideNoopAnimations()
      ],
    }).overrideProvider(EmailService, {useValue: emailSvcMock})
      .overrideProvider(AuthService, {useValue: authSvcMock})
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

  describe('test EmailService Interactions', ()=> {
    describe('test EmailService.getColumnDefs interaction', () => {
      beforeEach(() => {
        const data = generateEmailPagedData(0, 5, 1);
        emailSvcMock.getPagedData.and.returnValue(asyncData(data));
      });

      it('should call EmailService.getColumnDefs', async () => {
        const spy = emailSvcMock.getColumnDefs.and.returnValue(columnDefs);

        fixture.detectChanges();
        await fixture.whenStable();

        expect(spy).toHaveBeenCalled();
      });
    });

    describe('test EmailService.getEmails interaction', () => {
      beforeEach(async () => {
        emailSvcMock.getColumnDefs.and.returnValue(columnDefs);
      });

      it('should call EmailService.getEmails on init', async () => {
        const data = generateEmailPagedData(0, 5, 100);
        emailSvcMock.getPagedData.and.returnValue(asyncData(data));

        fixture.detectChanges();
        await fixture.whenStable();

        const request = PageRequest.of(0, 5);
        expect(emailSvcMock.getPagedData).toHaveBeenCalledOnceWith(request)
      });

      it('length should be set on init', async () => {
        const data = generateEmailPagedData(0, 5, 100);
        emailSvcMock.getPagedData.and.returnValue(asyncData(data));

        fixture.detectChanges();
        await fixture.whenStable();

        expect(component.resultsLength()).toEqual(data.page.totalElements);
      });

      it('datasource.data should be set on init', async () => {
        const data = generateEmailPagedData(0, 5, 2);
        emailSvcMock.getPagedData.and.returnValue(asyncData(data));

        fixture.detectChanges();
        await fixture.whenStable();

        expect(component.datasource().data).toBe(data._content);
      });

      it('datasource.data should be empty when an error occurs while calling getEmails', async () => {
        emailSvcMock.getPagedData.and.returnValue(throwError(() => 'error'));

        fixture.detectChanges();
        await fixture.whenStable();

        expect(component.datasource().data).toEqual([]);
      });
    });
  });

  describe('test MatDialog interactions', () => {
    let editEvent: EditEvent<EmailMember>;
    let dialogInput: EditDialogInput<Email>;
    let dialogResult: EditDialogResult<Email>;
    let pagedData: PagedData<EmailMember>;
    let error: HttpErrorResponse;

    beforeEach(() => {
      const data = generateEmailPagedData(0, 5, 1);
      emailSvcMock.getPagedData.and.returnValue(asyncData(data));
      emailSvcMock.getColumnDefs.and.returnValue(columnDefs);


      editEvent = {
        idx:0,
        data: generateEmailMember(0)
      }
      pagedData = generateEmailPagedData(0, 5, 10);
      dialogInput = generateEmailDialogInput(editEvent.data, EditAction.Edit);
      dialogResult = {context: editEvent.data, action: EditAction.Cancel};
      error = new HttpErrorResponse({status: 403, error: {}});
    });

    describe('onDelete', () => {
      it('onEditClick should call EmailService.generate', fakeAsync(() => {
        dialogSvcMock.openDialog.and.returnValue(of(dialogResult));
        const spy
          = emailSvcMock.generateEmailDialogInput.and.returnValue(dialogInput);

        component.onDeleteClick(editEvent);
        tick();

        expect(spy).toHaveBeenCalled();
      }));

      it('onDeleteClick should call EmailEditDialogService.openDialog', fakeAsync(() => {
        const spy =
          dialogSvcMock.openDialog.and.returnValue(of({context: generateEmail(), action: EditAction.Cancel}));

        component.onDeleteClick(editEvent);
        tick();

        expect(spy).toHaveBeenCalled();
      }));

      it('onDeleteClick should NOT call EmailService.update when EditEmailDialogService.open return cancel',
        fakeAsync(() => {

          dialogResult.action = EditAction.Cancel;
          dialogSvcMock.openDialog.and.returnValue(of(dialogResult));
          const spy = emailSvcMock.updateEmail.and.stub();

          component.onDeleteClick(editEvent);
          tick();

          expect(spy).toHaveBeenCalledTimes(0);
        }));

      it('onDeleteClick should NOT call EmailService.deleteEmail when EditEmailDialogService.open return cancel', fakeAsync(() => {
          dialogResult.action = EditAction.Cancel;
          dialogSvcMock.openDialog.and.returnValue(of(dialogResult));
          const spy = emailSvcMock.deleteEmail.and.stub();

          component.onDeleteClick(editEvent);
          tick();

          expect(spy).toHaveBeenCalledTimes(0);
      }));

      it('onDeleteClick should call EmailService.deleteEmail when EditEmailDialogService.open returns delete', fakeAsync(() => {
        dialogResult.action = EditAction.Delete;
        dialogSvcMock.openDialog.and.returnValue(of(dialogResult));
        const spy = emailSvcMock.deleteEmail.and.returnValue(of(void 0));

        component.onDeleteClick(editEvent);
        tick();

        expect(spy).toHaveBeenCalled();
      }));

      it('onDeleteClick should call EmailService.getPagedData when EditEmailDialogService.open returns delete', fakeAsync(() => {
        dialogResult.action = EditAction.Delete;
        dialogSvcMock.openDialog.and.returnValue(of(dialogResult));
        emailSvcMock.deleteEmail.and.returnValue(of(void 0));
        const spy = emailSvcMock.getPagedData.and.returnValue(of(pagedData));

        component.onDeleteClick(editEvent);
        tick();

        expect(spy).toHaveBeenCalled();
      }));

      it('onDeleteEmail should set datasource', fakeAsync(() => {
        dialogResult.action = EditAction.Delete;
        dialogSvcMock.openDialog.and.returnValue(of(dialogResult));
        emailSvcMock.deleteEmail.and.returnValue(of(void 0));
        emailSvcMock.getPagedData.and.returnValue(of(pagedData));

        component.onDeleteClick(editEvent);
        tick();

        expect(component.datasource().data).toEqual(pagedData._content);
      }));

      it('onDeleteClick should NOT call EmailService.getPagedData when EmailService.deleteEmail returns error', fakeAsync(() => {
        dialogResult.action = EditAction.Delete;
        dialogSvcMock.openDialog.and.returnValue(of(dialogResult));
        emailSvcMock.deleteEmail.and.returnValue(throwError(() => error));
        const spy = emailSvcMock.getPagedData.and.returnValue(of(pagedData));

        component.onDeleteClick(editEvent);
        tick();

        expect(spy).toHaveBeenCalledTimes(0);
      }));

      it('onDeleteClick should NOT update datasource when EmailService.getPagedData returns error', fakeAsync(() => {
        dialogResult.action = EditAction.Delete;
        dialogSvcMock.openDialog.and.returnValue(asyncData(dialogResult));
        emailSvcMock.deleteEmail.and.returnValue(asyncData(void 0));
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
        const spy
          = emailSvcMock.generateEmailDialogInput.and.returnValue(dialogInput);

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
          const spy = emailSvcMock.updateEmail.and.stub();

          component.onEditClick(editEvent);
          tick();

          expect(spy).toHaveBeenCalledTimes(0);
        }));

      it('onEditClick should NOT call EmailService.deleteEmail when EditEmailDialogService.open return cancel',
        fakeAsync(() => {

          dialogResult.action = EditAction.Cancel;
          dialogSvcMock.openDialog.and.returnValue(of(dialogResult));
          const spy = emailSvcMock.deleteEmail.and.stub();

          component.onEditClick(editEvent);
          tick();

          expect(spy).toHaveBeenCalledTimes(0);
        }));

      it('onEditClick should call EmailService.updateEMail when EditEmailDialogService.open returns edit', fakeAsync(() => {

        dialogResult.action = EditAction.Edit;
        dialogSvcMock.openDialog.and.returnValue(of(dialogResult));
        const spy = emailSvcMock.updateEmail.and.returnValue(of(dialogResult.context));

        component.onEditClick(editEvent);
        tick();

        expect(spy).toHaveBeenCalled();
      }));

      it('onEditClick should call EmailService.getPagedData when EditEmailDialogService.open returns edit', fakeAsync(() => {
        dialogResult.action = EditAction.Edit;
        dialogSvcMock.openDialog.and.returnValue(of(dialogResult));
        emailSvcMock.updateEmail.and.returnValue(of(dialogResult.context));
        const spy = emailSvcMock.getPagedData.and.returnValue(of(pagedData));

        component.onEditClick(editEvent);
        tick();

        expect(spy).toHaveBeenCalled();
      }));

      it('onEditClick should set datasource', fakeAsync(() => {
        dialogResult.action = EditAction.Edit;
        dialogSvcMock.openDialog.and.returnValue(of(dialogResult));
        emailSvcMock.updateEmail.and.returnValue(of(dialogResult.context));
        emailSvcMock.getPagedData.and.returnValue(of(pagedData));

        component.onEditClick(editEvent);
        tick();

        expect(component.datasource().data).toEqual(pagedData._content);
      }));

      it('onEditClick should NOT call EmailService.getPagedData when EmailService.updateEmail returns error', fakeAsync(() => {
        dialogResult.action = EditAction.Edit;
        dialogSvcMock.openDialog.and.returnValue(of(dialogResult));
        emailSvcMock.updateEmail.and.returnValue(throwError(() => error));
        const spy = emailSvcMock.getPagedData.and.returnValue(of(pagedData));

        component.onEditClick(editEvent);
        tick();

        expect(spy).toHaveBeenCalledTimes(0);
      }));

      it('onEditClick should NOT update datasource when EmailService.getPagedData returns error', fakeAsync(() => {
        dialogResult.action = EditAction.Edit;
        dialogSvcMock.openDialog.and.returnValue(of(dialogResult));
        emailSvcMock.updateEmail.and.returnValue(of(dialogResult.context));
        const expected = component.datasource().data;
        emailSvcMock.getPagedData.and.returnValue(throwError(() => error));

        component.onEditClick(editEvent);
        tick();

        expect(component.datasource().data).toEqual(expected);
      }));
    });
  });
});
