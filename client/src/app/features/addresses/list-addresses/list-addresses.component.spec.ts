import {ComponentFixture, fakeAsync, TestBed, tick} from '@angular/core/testing';
import {provideHttpClient} from '@angular/common/http';
import {provideHttpClientTesting} from '@angular/common/http/testing';
import {provideNoopAnimations} from '@angular/platform-browser/animations';
import {of, throwError} from 'rxjs';
import {TranslateModule} from '@ngx-translate/core';

import {AuthService} from '@app/core/auth/services/auth.service';
import {asyncData} from '@app/shared/testing';

import {AddressTest} from '@app/features/addresses/testing/test-support';

import {AddressMember, Address} from '@app/features/addresses/models/address';
import {AddressService} from '@app/features/addresses/services/address.service';
import {AddressEditDialogService} from '@app/features/addresses/services/address-edit-dialog.service';
import {AddressTableService} from '@app/features/addresses/services/address-table.service';
import {ListAddressesComponent} from './list-addresses.component';
import {EditEvent, EditDialogInput, EditDialogResult, EditAction} from '@app/shared/components/base-edit-dialog/models';
import {PageRequest} from '@app/shared/services/api-service/models';
import {AddressEditorComponent} from '@app/features/addresses/address-editor/address-editor.component';

describe('ListAddressesComponent', () => {
  let fixture: ComponentFixture<ListAddressesComponent>;
  let component: ListAddressesComponent;

  let addressSvcMock: jasmine.SpyObj<AddressService>;
  let authSvcMock: jasmine.SpyObj<AuthService>;
  let dialogSvcMock: jasmine.SpyObj<AddressEditDialogService>;
  let tableSvcMock: jasmine.SpyObj<AddressTableService>

  const columnDefs = AddressTest.generateColumnDefs();

  beforeEach(async () => {
    addressSvcMock = jasmine.createSpyObj('AddressService', [
      'getPagedData', 'update', 'create', 'delete']);

    dialogSvcMock = jasmine.createSpyObj('EditAddressDialogService',
      ['openDialog', 'generateDialogInput']);

    authSvcMock = jasmine.createSpyObj('AuthService', ['hasPermission']);
    tableSvcMock = jasmine.createSpyObj('AddressTableService', ['getColumnDefs']);

    await TestBed.configureTestingModule({
      imports: [
          ListAddressesComponent,
          TranslateModule.forRoot({})
      ],
        providers: [
          {provide: AddressService, useValue: {}},
          {provide: AuthService, useValue: {}},
          {provide: AddressTableService, useValue: {}},
          {provide: AddressEditDialogService, useValue: {}},
          provideHttpClient(),
          provideHttpClientTesting(),
          provideNoopAnimations()
      ],
    }).overrideProvider(AddressService, {useValue: addressSvcMock})
      .overrideProvider(AuthService, {useValue: authSvcMock})
      .overrideProvider(AddressTableService, {useValue: tableSvcMock})
      .overrideProvider(AddressEditDialogService, {useValue: dialogSvcMock})
      .compileComponents();

    fixture = TestBed.createComponent(ListAddressesComponent);
    component = fixture.componentInstance;
  });

  describe('test component', () => {
    it('should create', () => {
      expect(component).toBeTruthy();
    });
  });

  describe('test service interactions on init', () => {
    describe('AddressTableService', () => {
      beforeEach(() => {
        const data = AddressTest.generatePagedData(0, 5, 1);
        addressSvcMock.getPagedData.and.returnValue(asyncData(data));
      });

      it('should call AddressTableService.getColumnDefs', async () => {
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

    describe('test AddressService.getPagedData interactions', () => {
      beforeEach(() => {
        tableSvcMock.getColumnDefs.and.returnValue(columnDefs);
      });

      it('should call AddressService.getPagedData', async () => {
        const data = AddressTest.generatePagedData(0, 5, 100);
        addressSvcMock.getPagedData.and.returnValue(asyncData(data));

        fixture.detectChanges();
        await fixture.whenStable();

        const request = PageRequest.of(0, 5);
        expect(addressSvcMock.getPagedData).toHaveBeenCalledWith(request);//With(request);
      });

      it('should set the correct data length', async () => {
        const data = AddressTest.generatePagedData(0, 5, 100);
        addressSvcMock.getPagedData.and.returnValue(asyncData(data));

        fixture.detectChanges();
        await fixture.whenStable();

        expect(component.resultsLength()).toEqual(data.page.totalElements);
      });

      it('should set correct datasource.data', async () => {
        const data = AddressTest.generatePagedData(0, 5, 2);
        addressSvcMock.getPagedData.and.returnValue(asyncData(data));

        fixture.detectChanges();
        await fixture.whenStable();

        expect(component.datasource().data).toEqual(data._content);
      });

      it('datasource.data should be empty when an error occurs while calling getPagedData', async () => {
        addressSvcMock.getPagedData.and.returnValue(throwError(() => 'error'));

        fixture.detectChanges();
        await fixture.whenStable();

        expect(component.datasource().data).toEqual([]);
      });
    });
  });

  describe('test dialog interactions', () => {
    let editEvent: EditEvent<AddressMember>;
    let dialogInput: EditDialogInput<Address, AddressEditorComponent>;
    let dialogResult: EditDialogResult<Address>;

    beforeEach(() => {
      const data = AddressTest.generatePagedData(0, 5, 1);
      addressSvcMock.getPagedData.and.returnValue(asyncData(data));
      tableSvcMock.getColumnDefs.and.returnValue(columnDefs);

      editEvent = {
        idx: 0,
        data: AddressTest.generateAddressMember(0)
      }
      const address = AddressTest.generateAddress();
      dialogInput = AddressTest.generateDialogInput(address, EditAction.Edit);
      dialogResult = {context: address, action: EditAction.Cancel};
    });

    describe('onDeleteClick', () => {
      it('onDeleteClick should call AddressDialogService.generateDialogInput', fakeAsync(() => {
        dialogSvcMock.openDialog.and.returnValue(of(dialogResult));
        const spy =
          dialogSvcMock.generateDialogInput.and.returnValue(dialogInput);

        component.onDeleteClick(editEvent);
        tick();

        expect(spy).toHaveBeenCalled();
      }));

      it('onDeleteClick should call AddressEditDialogService.openDialog', fakeAsync(() => {
        dialogResult.action = EditAction.Cancel;
        const spy =
          dialogSvcMock.openDialog.and.returnValue(of(dialogResult));

        component.onDeleteClick(editEvent);
        tick();

        expect(spy).toHaveBeenCalled();
      }));

      it('onDeleteClick should NOT call AddressService.delete when open dialog return cancel', fakeAsync(() => {
        dialogResult.action = EditAction.Cancel;
        dialogSvcMock.openDialog.and.returnValue(of(dialogResult));
        const spy = addressSvcMock.delete.and.stub();

        component.onDeleteClick(editEvent);
        tick();

        expect(spy).toHaveBeenCalledTimes(0);
      }));

      it('onDeleteClick should call AddressService.delete when open dialog returns delete', fakeAsync(() => {
        dialogResult.action = EditAction.Delete;
        dialogSvcMock.openDialog.and.returnValue(of(dialogResult));
        const spy = addressSvcMock.delete.and.stub();

        component.onDeleteClick(editEvent);
        tick();

        expect(spy).toHaveBeenCalledTimes(1);
      }));
    });

    describe('onEditClick', () => {
      it('onEditClick should call AddressDialogService.generateDialogInput', fakeAsync(() => {
        dialogSvcMock.openDialog.and.returnValue(of(dialogResult));
        const spy =
          dialogSvcMock.generateDialogInput.and.returnValue(dialogInput);

        component.onEditClick(editEvent);
        tick();

        expect(spy).toHaveBeenCalled();
      }));

      it('onEditClick should call AddressEditDialogService.openDialog', fakeAsync(() => {
        const spy =
          dialogSvcMock.openDialog.and.returnValue(of(dialogResult));

        component.onEditClick(editEvent);
        tick();

        expect(spy).toHaveBeenCalled();
      }));

      it('onEditClick should NOT call AddressService.update when EditAddressDialogService.open return cancel', fakeAsync(() => {
        dialogResult.action = EditAction.Cancel;
        dialogSvcMock.openDialog.and.returnValue(of(dialogResult));
        const spy = addressSvcMock.update.and.stub();

        component.onEditClick(editEvent);
        tick();

        expect(spy).toHaveBeenCalledTimes(0);
      }));

      it('onDeleteClick should call AddressService.update when open dialog returns edit', fakeAsync(() => {
        dialogResult.action = EditAction.Edit;
        dialogSvcMock.openDialog.and.returnValue(of(dialogResult));
        const spy = addressSvcMock.update.and.stub();

        component.onEditClick(editEvent);
        tick();

        expect(spy).toHaveBeenCalledTimes(1);
      }));
    });
  });
});
