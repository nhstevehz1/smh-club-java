import {ComponentFixture, TestBed, tick} from '@angular/core/testing';
import {provideNoopAnimations} from '@angular/platform-browser/animations';
import {TranslateModule} from '@ngx-translate/core';

import {AuthService} from '@app/core/auth/services/auth.service';

import {asyncData} from '@app/shared/testing/test-helpers';
import {ColumnDef} from '@app/shared/components/sortable-pageable-table/models';
import {MockTestService} from '@app/shared/components/base-table-component/testing/services/mock-test.service';
import {MockTestTableService} from '@app/shared/components/base-table-component/testing/services/mock-test-table.service';
import {
  MockTestEditDialogService
} from '@app/shared/components/base-table-component/testing/services/mock-test-edit-dialog.service';
import {
  generateTableModelPagedData,
  generateTableModelColumnDefs,
  generateTableModel,
  generateTestDialogInput
} from '@app/shared/components/base-table-component/testing/test-support';
import {MockTestListComponent} from '@app/shared/components/base-table-component/testing/list-test/mock-test-list.component';
import {TableModel} from '@app/shared/components/base-table-component/testing/models/test-models';
import {PagedData, PageRequest} from '@app/shared/services/api-service/models';
import {throwError, of} from 'rxjs';
import {EditEvent, EditDialogInput, EditDialogResult, EditAction} from '@app/shared/components/edit-dialog/models';
import {MockTableEditor} from '@app/shared/components/base-table-component/testing/mock-editor/mock-table-editor';

describe('BaseTableComponent', () => {
  let component: MockTestListComponent;
  let fixture: ComponentFixture<MockTestListComponent>;

  let authMock: jasmine.SpyObj<AuthService>;
  let svcMock: jasmine.SpyObj<MockTestService>;
  let tableSvcMock: jasmine.SpyObj<MockTestTableService>;
  let dialogSvcMock: jasmine.SpyObj<MockTestEditDialogService>;

  let data: PagedData<TableModel>;
  let columnDefs: ColumnDef<TableModel>[];

  beforeEach(async () => {
    authMock = jasmine.createSpyObj('AuthService', ['hasPermission']);
    svcMock = jasmine.createSpyObj('MockTestService', ['getPagedData', 'update', 'delete']);
    tableSvcMock = jasmine.createSpyObj('MockTestTableService', ['getColumnDefs']);
    dialogSvcMock = jasmine.createSpyObj('MockTestEditDialogService', ['generateForm', 'generateDialogInput']);
    authMock.hasPermission.and.returnValue(true);

    await TestBed.configureTestingModule({
      imports: [
        MockTestListComponent,
        TranslateModule.forRoot({})
      ],
      providers: [
        {provide: AuthService, useValue: {}},
        {provide: MockTestService, useValue: {}},
        {provide: MockTestTableService, useValue: {}},
        {provide: MockTestEditDialogService, useValue: {}},
        provideNoopAnimations()
      ]
    }).overrideProvider(AuthService, {useValue: authMock})
      .overrideProvider(MockTestService, {useValue: svcMock})
      .overrideProvider(MockTestTableService, {useValue: tableSvcMock})
      .overrideProvider(MockTestEditDialogService, {useValue: dialogSvcMock})
      .compileComponents();

    fixture = TestBed.createComponent(MockTestListComponent);
    component = fixture.componentInstance;


  });

  describe('test component', () => {
    beforeEach(() => {
      data = generateTableModelPagedData(0, 5, 1);
      columnDefs = generateTableModelColumnDefs();
      tableSvcMock.getColumnDefs.and.returnValue(columnDefs);
      svcMock.getPagedData.and.returnValue(asyncData(data));
    });

    it('should create', async () => {
      fixture.detectChanges();
      await fixture.whenStable();
      expect(component).toBeTruthy();
    });
  });

  describe('test service interactions on init', ()=> {
    beforeEach(() => {
      const data = generateTableModelPagedData(0, 5,1);
      svcMock.getPagedData.and.returnValue(asyncData(data));
    });

    it('should call TableService.getColumnDefs', async () => {
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

    describe('test ApiService.getPagedData interactions', () => {
      beforeEach(() => {
        tableSvcMock.getColumnDefs.and.returnValue(columnDefs);
      });

      it('should call AddressService.getPagedData', async () => {
        const data = generateTableModelPagedData(0, 5, 100);
        svcMock.getPagedData.and.returnValue(asyncData(data));

        fixture.detectChanges();
        await fixture.whenStable();

        const request = PageRequest.of(0, 5);
        expect(svcMock.getPagedData).toHaveBeenCalledWith(request);//With(request);
      });

      it('should set the correct data length', async () => {
        const data = generateTableModelPagedData(0, 5, 100);
        svcMock.getPagedData.and.returnValue(asyncData(data));

        fixture.detectChanges();
        await fixture.whenStable();

        expect(component.resultsLength()).toEqual(data.page.totalElements);
      });

      it('should set correct datasource.data', async () => {
        const data = generateTableModelPagedData(0, 5, 2);
        svcMock.getPagedData.and.returnValue(asyncData(data));

        fixture.detectChanges();
        await fixture.whenStable();

        expect(component.datasource().data).toEqual(data._content);
      });

      it('datasource.data should be empty when ApiService.getPaged data returns an error', async () => {
        svcMock.getPagedData.and.returnValue(throwError(() => 'error'));

        fixture.detectChanges();
        await fixture.whenStable();

        expect(component.datasource().data).toEqual([]);
      });

    });
  });

  describe('test dialog interactions', () => {
    let editEvent: EditEvent<TableModel>;
    let dialogInput: EditDialogInput<TableModel, MockTableEditor>;
    let dialogResult: EditDialogResult<TableModel>;

    beforeEach(() => {
      const data = generateTableModelPagedData(0, 5, 1);
      svcMock.getPagedData.and.returnValue(asyncData(data));
      tableSvcMock.getColumnDefs.and.returnValue(columnDefs);

      editEvent = {
        idx: 0,
        data: generateTableModel()
      }
      const model = generateTableModel();
      dialogInput = generateTestDialogInput(model, EditAction.Edit);
      dialogResult = {context: model, action: EditAction.Cancel};
    });

    xdescribe('openDialog with EventAction.Delete', () => {
      xit('should call DialogService.generateDialogInput', () => {
        dialogResult.action = EditAction.Cancel;
        const spy =
          dialogSvcMock.openDialog.and.returnValue(of(dialogResult));

        component.onDeleteClick(editEvent);
        tick();

        expect(spy).toHaveBeenCalled();
      });
    });
  });
});
