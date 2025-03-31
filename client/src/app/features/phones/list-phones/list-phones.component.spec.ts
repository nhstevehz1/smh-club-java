
import {ComponentFixture, fakeAsync, TestBed, tick} from '@angular/core/testing';
import {HttpErrorResponse, provideHttpClient} from '@angular/common/http';
import {provideHttpClientTesting} from '@angular/common/http/testing';
import {provideNoopAnimations} from '@angular/platform-browser/animations';

import {of, throwError} from 'rxjs';
import {TranslateModule} from '@ngx-translate/core';

import {AuthService} from '@app/core/auth';

import {asyncData} from '@app/shared/testing';
import {EditAction, EditDialogInput, EditDialogResult, EditEvent} from '@app/shared/components/edit-dialog';
import {PagedData, PageRequest} from '@app/shared/services/api-service';

import {
  generatePhone,
  generatePhoneColumnDefs,
  generatePhoneDialogInput,
  generatePhoneMember,
  generatePhonePageData
} from '@app/features/phones/testing';

import {
  ListPhonesComponent, Phone, PhoneMember,
  PhoneService, PhoneTableService, PhoneEditDialogService
} from '@app/features/phones';

describe('ListPhonesComponent', () => {
  let fixture: ComponentFixture<ListPhonesComponent>;
  let component: ListPhonesComponent;

  let phoneSvcMock: jasmine.SpyObj<PhoneService>;
  let tableSvcMock: jasmine.SpyObj<PhoneTableService>;
  let authSvcMock: jasmine.SpyObj<AuthService>;
  let dialogSvcMock: jasmine.SpyObj<PhoneEditDialogService>;

  const columnDefs = generatePhoneColumnDefs();

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
  });

  describe('test component', () => {
    it('should create', () => {
      expect(component).toBeTruthy();
    });
  });

  describe('test service interactions on init', () => {
    describe('PhoneTableService', () => {
      beforeEach(() => {
        const data = generatePhonePageData(0, 5, 1);
        phoneSvcMock.getPagedData.and.returnValue(asyncData(data));
      });

      it('should call PhoneTableService.getColumnDefs', async () => {
        const spy = tableSvcMock.getColumnDefs.and.returnValue(columnDefs);

        fixture.detectChanges();
        await fixture.whenStable();

        expect(spy).toHaveBeenCalled();
      });

      it('should create column list', async () => {
        const spy = tableSvcMock.getColumnDefs.and.returnValue(columnDefs);

        fixture.detectChanges();
        await fixture.whenStable();

        expect(component.columns().length).toEqual(3);
      });

      it('should create correct column list', async () => {
        tableSvcMock.getColumnDefs.and.returnValue(columnDefs);

        fixture.detectChanges();
        await fixture.whenStable();

        expect(component.columns()).toEqual(columnDefs);
      });
    });

    describe('PhoneService.getPagedData', ()=> {
      beforeEach(() => {
        tableSvcMock.getColumnDefs.and.returnValue(columnDefs);
      });

      it('should call PhoneService.getPagedData', async () => {
        const data = generatePhonePageData(0, 5, 100);
        phoneSvcMock.getPagedData.and.returnValue(asyncData(data));

        fixture.detectChanges();
        await fixture.whenStable();

        const request = PageRequest.of(0, 5);
        expect(phoneSvcMock.getPagedData).toHaveBeenCalledWith(request);
      });

      it('should set correct data length', async () => {
        const data = generatePhonePageData(0, 5, 2);
        phoneSvcMock.getPagedData.and.returnValue(asyncData(data));

        fixture.detectChanges();
        await fixture.whenStable();

        expect(component.datasource().data).toBe(data._content);
      });

      it('datasource.data should be empty when an error occurs while calling getPagedData', async () => {
        phoneSvcMock.getPagedData.and.returnValue(throwError(() => 'error'));

        fixture.detectChanges();
        await fixture.whenStable();

        expect(component.datasource().data).toEqual([]);
      });

    });
  });

  describe('test dialog interactions', ()=> {
    let editEvent: EditEvent<PhoneMember>;
    let dialogInput: EditDialogInput<Phone>;
    let dialogResult: EditDialogResult<Phone>;
    let pagedData: PagedData<PhoneMember>;
    let error: HttpErrorResponse;

    beforeEach(() => {
      const data = generatePhonePageData(0, 5, 1);
      phoneSvcMock.getPagedData.and.returnValue(asyncData(data));
      tableSvcMock.getColumnDefs.and.returnValue(columnDefs);

      editEvent = {
        idx: 0,
        data: generatePhoneMember(0)
      }

      const phone = generatePhone();
      dialogInput = generatePhoneDialogInput(phone, EditAction.Edit);
      dialogResult = {context: phone, action: EditAction.Cancel};
      pagedData = generatePhonePageData(0, 5, 1);
      error = new HttpErrorResponse(({status: 403, error: {}}));
    });

    describe('onDeleteClick', () => {
      it('should call PhoneDialogService.generateDialogInput', fakeAsync(() => {
        dialogSvcMock.openDialog.and.returnValue(of(dialogResult));
        const spy =
          dialogSvcMock.generateDialogInput.and.returnValue(dialogInput);

        component.onDeleteClick(editEvent);
        tick();

        expect(spy).toHaveBeenCalled();
      }));

      it('should call PhoneDialogService.openDialog', fakeAsync(() => {
        dialogResult.action = EditAction.Cancel;
        const spy =
          dialogSvcMock.openDialog.and.returnValue(of(dialogResult));

        component.onDeleteClick(editEvent);
        tick();

        expect(spy).toHaveBeenCalled();
      }));

      it('should NOT call PhoneService.update when dialog service returns cancel', fakeAsync(() => {
        dialogResult.action = EditAction.Cancel;
        dialogSvcMock.openDialog.and.returnValue(of(dialogResult));
        const spy = phoneSvcMock.update.and.stub();

        component.onDeleteClick(editEvent);
        tick();

        expect(spy).toHaveBeenCalledTimes(0);
      }));

      it('should NOT call PhoneService.delete when dialog service returns cancel', fakeAsync(() => {
        dialogResult.action = EditAction.Cancel;
        dialogSvcMock.openDialog.and.returnValue(of(dialogResult));
        const spy = phoneSvcMock.delete.and.stub();

        component.onDeleteClick(editEvent);
        tick();

        expect(spy).toHaveBeenCalledTimes(0);
      }));

      it('should call PhoneService.delete when dialog services returns delete', fakeAsync(() => {
        dialogResult.action = EditAction.Delete;
        dialogSvcMock.openDialog.and.returnValue(of(dialogResult));
        const spy = phoneSvcMock.delete.and.returnValue(of(void 0));

        component.onDeleteClick(editEvent);
        tick();

        expect(spy).toHaveBeenCalled();
      }));

      it('should call PhoneService.getPagedData when dialog services returns delete', fakeAsync(() => {
        dialogResult.action = EditAction.Delete;
        dialogSvcMock.openDialog.and.returnValue(of(dialogResult));
        phoneSvcMock.delete.and.returnValue(of(void 0));
        const spy = phoneSvcMock.getPagedData.and.returnValue(of(pagedData));

        component.onDeleteClick(editEvent);
        tick();

        expect(spy).toHaveBeenCalled();
      }));

      it('should set datasource', fakeAsync(() => {
        dialogResult.action = EditAction.Delete;
        dialogSvcMock.openDialog.and.returnValue(of(dialogResult));
        phoneSvcMock.delete.and.returnValue(of(void 0));
        phoneSvcMock.getPagedData.and.returnValue(of(pagedData));

        component.onDeleteClick(editEvent);
        tick();

        expect(component.datasource().data).toEqual(pagedData._content);
      }));

      it('should NOT call PhoneService.getPagedData when phone service delete returns an error', fakeAsync(() => {
        dialogResult.action = EditAction.Delete;
        dialogSvcMock.openDialog.and.returnValue(of(dialogResult));
        phoneSvcMock.delete.and.returnValue(throwError(() => error));
        const spy = phoneSvcMock.getPagedData.and.returnValue(of(pagedData));

        component.onDeleteClick(editEvent);
        tick();

        expect(spy).toHaveBeenCalledTimes(0);
      }));

      it('should NOT update datasource when PhoneService.getPagedData returns an error', fakeAsync(() => {
        dialogResult.action = EditAction.Delete;
        dialogSvcMock.openDialog.and.returnValue(asyncData(dialogResult));
        phoneSvcMock.delete.and.returnValue(asyncData(void 0));
        const expected = component.datasource().data;
        phoneSvcMock.getPagedData.and.returnValue(throwError(() => error));

        component.onDeleteClick(editEvent);
        tick();

        expect(component.datasource().data).toEqual(expected);
      }));
    });
  });
});
