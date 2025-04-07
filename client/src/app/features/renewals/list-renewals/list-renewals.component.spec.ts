import {ComponentFixture, TestBed, fakeAsync, tick} from '@angular/core/testing';
import {provideHttpClient} from '@angular/common/http';
import {provideHttpClientTesting} from '@angular/common/http/testing';
import {provideNoopAnimations} from '@angular/platform-browser/animations';

import {of} from 'rxjs';
import {TranslateModule} from '@ngx-translate/core';

import {asyncData} from '@app/shared/testing/test-helpers';

import {RenewalService} from '@app/features/renewals/services/renewal.service';
import {ListRenewalsComponent} from './/list-renewals.component';
import {AuthService} from '@app/core/auth/services/auth.service';
import {RenewalEditDialogService} from '@app/features/renewals/services/renewal-edit-dialog.service';
import {RenewalTableService} from '@app/features/renewals/services/renewal-table.service';
import {EditEvent, EditDialogInput, EditDialogResult, EditAction} from '@app/shared/components/base-edit-dialog/models';
import {RenewalTest} from '@app/features/renewals/testing/test-support';
import {RenewalMember, Renewal} from '@app/features/renewals/models';
import {RenewalEditorComponent} from '@app/features/renewals/renewal-editor/renewal-editor.component';


describe('ListRenewalsComponent', () => {
  let fixture: ComponentFixture<ListRenewalsComponent>;
  let component: ListRenewalsComponent;

  let renewalSvcMock: jasmine.SpyObj<RenewalService>;
  let authSvcMock: jasmine.SpyObj<AuthService>;
  let dialogSvcMock: jasmine.SpyObj<RenewalEditDialogService>;
  let tableSvcMock: jasmine.SpyObj<RenewalTableService>;

  const data = RenewalTest.generatePageData(0, 5, 1);
  const columnDefs = RenewalTest.generateColumnDefs();

  beforeEach(async () => {
    renewalSvcMock = jasmine.createSpyObj('RenewalService',
      ['getPagedData', 'update', 'create', 'delete']);

    dialogSvcMock = jasmine.createSpyObj('RenewalEditDialogService',
      ['openDialog', 'generateDialogInput']);

    authSvcMock = jasmine.createSpyObj('AuthService', ['hasPermission']);
    tableSvcMock = jasmine.createSpyObj('RenewalTableService', ['getColumnDefs']);

    await TestBed.configureTestingModule({
      imports: [
        ListRenewalsComponent,
        TranslateModule.forRoot({})
      ],
      providers: [
        {provide: RenewalService, useValue: {}},
        {provide: AuthService, useValue: {}},
        {provide: RenewalTableService, useValue: {}},
        {provide: RenewalEditDialogService, useValue: {}},
        provideHttpClient(),
        provideHttpClientTesting(),
        provideNoopAnimations(),
      ],
    }).overrideProvider(RenewalService, {useValue: renewalSvcMock})
      .overrideProvider(AuthService, {useValue: authSvcMock})
      .overrideProvider(RenewalTableService, {useValue: tableSvcMock})
      .overrideProvider(RenewalEditDialogService, {useValue: dialogSvcMock})
      .compileComponents();

    fixture = TestBed.createComponent(ListRenewalsComponent);
    component = fixture.componentInstance;

    renewalSvcMock.getPagedData.and.returnValue(asyncData(data));
    tableSvcMock.getColumnDefs.and.returnValue(columnDefs);
  });

  it('should create', async () => {
    fixture.detectChanges();
    await fixture.whenStable();
    expect(component).toBeTruthy();
  });

  describe('test dialog interactions', () => {
    let editEvent: EditEvent<RenewalMember>;
    let dialogInput: EditDialogInput<Renewal, RenewalEditorComponent>;
    let dialogResult: EditDialogResult<Renewal>;

    beforeEach(() => {
      editEvent = {
        idx: 0,
        data: RenewalTest.generateRenewalMember(0)
      }
      const renewal = RenewalTest.generateRenewal();
      dialogInput = RenewalTest.generateDialogInput(renewal, EditAction.Edit);
      dialogResult = {context: renewal, action: EditAction.Cancel};
    });

    describe('onDeleteClick', () => {
      it('onDeleteClick should call RenewalDialogService.generateDialogInput', fakeAsync(() => {
        dialogSvcMock.openDialog.and.returnValue(of(dialogResult));
        const spy =
          dialogSvcMock.generateDialogInput.and.returnValue(dialogInput);

        component.onDeleteClick(editEvent);
        tick();

        expect(spy).toHaveBeenCalled();
      }));

      it('onDeleteClick should call RenewalDialogService.openDialog', fakeAsync(() => {
        dialogResult.action = EditAction.Cancel;
        const spy =
          dialogSvcMock.openDialog.and.returnValue(of(dialogResult));

        component.onDeleteClick(editEvent);
        tick();

        expect(spy).toHaveBeenCalled();
      }));

      it('onDeleteClick should NOT call RenewalService.delete when open dialog return cancel', fakeAsync(() => {
        dialogResult.action = EditAction.Cancel;
        dialogSvcMock.openDialog.and.returnValue(of(dialogResult));
        const spy = renewalSvcMock.delete.and.stub();

        component.onDeleteClick(editEvent);
        tick();

        expect(spy).toHaveBeenCalledTimes(0);
      }));

      it('onDeleteClick should call RenewalService.delete when open dialog returns delete', fakeAsync(() => {
        dialogResult.action = EditAction.Delete;
        dialogSvcMock.openDialog.and.returnValue(of(dialogResult));
        const spy = renewalSvcMock.delete.and.stub();

        component.onDeleteClick(editEvent);
        tick();

        expect(spy).toHaveBeenCalledTimes(1);
      }));
    });

    describe('onEditClick', () => {
      it('onEditClick should call ARenewalDialogService.generateDialogInput', fakeAsync(() => {
        dialogSvcMock.openDialog.and.returnValue(of(dialogResult));
        const spy =
          dialogSvcMock.generateDialogInput.and.returnValue(dialogInput);

        component.onEditClick(editEvent);
        tick();

        expect(spy).toHaveBeenCalled();
      }));

      it('onEditClick should call RenewalEditDialogService.openDialog', fakeAsync(() => {
        const spy =
          dialogSvcMock.openDialog.and.returnValue(of(dialogResult));

        component.onEditClick(editEvent);
        tick();

        expect(spy).toHaveBeenCalled();
      }));

      it('onEditClick should NOT call RenewalService.update when EditAddressDialogService.open return cancel', fakeAsync(() => {
        dialogResult.action = EditAction.Cancel;
        dialogSvcMock.openDialog.and.returnValue(of(dialogResult));
        const spy = renewalSvcMock.update.and.stub();

        component.onEditClick(editEvent);
        tick();

        expect(spy).toHaveBeenCalledTimes(0);
      }));

      it('onDeleteClick should call RenewalService.update when open dialog returns edit', fakeAsync(() => {
        dialogResult.action = EditAction.Edit;
        dialogSvcMock.openDialog.and.returnValue(of(dialogResult));
        const spy = renewalSvcMock.update.and.stub();

        component.onEditClick(editEvent);
        tick();

        expect(spy).toHaveBeenCalledTimes(1);
      }));
    });
  });

});
