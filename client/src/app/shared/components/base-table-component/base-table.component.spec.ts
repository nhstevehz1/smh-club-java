import {ComponentFixture, TestBed, tick, fakeAsync} from '@angular/core/testing';
import {provideNoopAnimations} from '@angular/platform-browser/animations';
import {TranslateModule} from '@ngx-translate/core';

import {AuthService} from '@app/core/auth/services/auth.service';

import {asyncData} from '@app/shared/testing/test-helpers';
import {ColumnDef} from '@app/shared/components/sortable-pageable-table/models';
import {MockTableApiService} from '@app/shared/components/base-table-component/testing/services/mock-table-api.service';
import {MockTableService} from '@app/shared/components/base-table-component/testing/services/mock-table.service';
import {
  MockTableEditDialogService
} from '@app/shared/components/base-table-component/testing/services/mock-table-edit-dialog.service';
import {BaseTableTest} from '@app/shared/components/base-table-component/testing/test-support';
import {
  MockBaseTableComponent
} from '@app/shared/components/base-table-component/testing/mock-table/mock-base-table.component';
import {TableModel} from '@app/shared/components/base-table-component/testing/models/test-models';
import {PagedData, PageRequest} from '@app/shared/services/api-service/models';
import {throwError} from 'rxjs';
import {EditEvent, EditDialogResult, EditAction, EditDialogInput} from '@app/shared/components/base-edit-dialog/models';
import {MockTableEditorComponent} from '@app/shared/components/base-table-component/testing/mock-editor/mock-table-editor.component';

describe('BaseTableComponent', () => {
  let component: MockBaseTableComponent;
  let fixture: ComponentFixture<MockBaseTableComponent>;

  let authMock: jasmine.SpyObj<AuthService>;
  let apiSvcMock: jasmine.SpyObj<MockTableApiService>;
  let tableSvcMock: jasmine.SpyObj<MockTableService>;
  let dialogSvcMock: jasmine.SpyObj<MockTableEditDialogService>;

  let data: PagedData<TableModel>;
  let columnDefs: ColumnDef<TableModel>[];

  beforeEach(async () => {
    authMock = jasmine.createSpyObj('AuthService', ['hasPermission']);
    apiSvcMock = jasmine.createSpyObj('MockTableApiService', ['getPagedData']);
    tableSvcMock = jasmine.createSpyObj('MockTableService', ['getColumnDefs']);
    dialogSvcMock = jasmine.createSpyObj('MockTableEditDialogService', ['openDialog', 'generateDialogInput']);


    await TestBed.configureTestingModule({
      imports: [
        MockBaseTableComponent,
        TranslateModule.forRoot({})
      ],
      providers: [
        {provide: AuthService, useValue: {}},
        {provide: MockTableApiService, useValue: {}},
        {provide: MockTableService, useValue: {}},
        {provide: MockTableEditDialogService, useValue: {}},
        provideNoopAnimations()
      ]
    }).overrideProvider(AuthService, {useValue: authMock})
      .overrideProvider(MockTableApiService, {useValue: apiSvcMock})
      .overrideProvider(MockTableService, {useValue: tableSvcMock})
      .overrideProvider(MockTableEditDialogService, {useValue: dialogSvcMock})
      .compileComponents();

    fixture = TestBed.createComponent(MockBaseTableComponent);
    component = fixture.componentInstance;

    authMock.hasPermission.and.returnValue(true);
  });

  describe('test component', () => {
    beforeEach(() => {
      data = BaseTableTest.generatePagedData(0, 5, 1);
      columnDefs = BaseTableTest.generateColumnDefs();
      tableSvcMock.getColumnDefs.and.returnValue(columnDefs);
      apiSvcMock.getPagedData.and.returnValue(asyncData(data));
    });

    fit('should create', async () => {
      fixture.detectChanges();
      await fixture.whenStable();
      expect(component).toBeTruthy();
    });
  });

  describe('test service interactions on init', ()=> {
    beforeEach(() => {
      const data = BaseTableTest.generatePagedData(0, 5,1);
      apiSvcMock.getPagedData.and.returnValue(asyncData(data));
    });

    fit('should call TableService.getColumnDefs', async () => {
      const spy = tableSvcMock.getColumnDefs.and.returnValue(columnDefs);

      fixture.detectChanges();
      await fixture.whenStable();

      expect(spy).toHaveBeenCalled();
    });

    fit('should create correct column list', async () => {
      tableSvcMock.getColumnDefs.and.returnValue(columnDefs);

      fixture.detectChanges();
      await fixture.whenStable();

      expect(component.columns()).toEqual(columnDefs);
    });

    describe('test ApiService.getPagedData interactions', () => {
      beforeEach(() => {
        tableSvcMock.getColumnDefs.and.returnValue(columnDefs);
      });

      fit('should call AddressService.getPagedData', async () => {
        const data = BaseTableTest.generatePagedData(0, 5, 100);
        apiSvcMock.getPagedData.and.returnValue(asyncData(data));

        fixture.detectChanges();
        await fixture.whenStable();

        const request = PageRequest.of(0, 5);
        expect(apiSvcMock.getPagedData).toHaveBeenCalledWith(request);//With(request);
      });

      fit('should set the correct data length', async () => {
        const data = BaseTableTest.generatePagedData(0, 5, 100);
        apiSvcMock.getPagedData.and.returnValue(asyncData(data));

        fixture.detectChanges();
        await fixture.whenStable();

        expect(component.resultsLength()).toEqual(data.page.totalElements);
      });

      fit('should set correct datasource.data', async () => {
        const data = BaseTableTest.generatePagedData(0, 5, 2);
        apiSvcMock.getPagedData.and.returnValue(asyncData(data));

        fixture.detectChanges();
        await fixture.whenStable();

        expect(component.datasource().data).toEqual(data._content);
      });

      fit('datasource.data should be empty when ApiService.getPaged data returns an error', async () => {
        apiSvcMock.getPagedData.and.returnValue(throwError(() => 'error'));

        fixture.detectChanges();
        await fixture.whenStable();

        expect(component.datasource().data).toEqual([]);
      });
    });
  });

  describe('test dialog service interactions', () => {
    let editEvent: EditEvent<TableModel>;
    let dialogInput: EditDialogInput<TableModel, MockTableEditorComponent>;
    let dialogResult: EditDialogResult<TableModel>;

    beforeEach(async () => {
      const data = BaseTableTest.generatePagedData(0, 5, 2);
      apiSvcMock.getPagedData.and.returnValue(asyncData(data));
      tableSvcMock.getColumnDefs.and.returnValue(columnDefs);

      editEvent = {
        idx: 0,
        data: BaseTableTest.generateModel()
      }
      fixture.detectChanges();
      await fixture.whenStable();
    });

    describe('openDialog with EventAction.Delete', () => {
      beforeEach(() => {
        const model = BaseTableTest.generateModel();
        dialogInput = BaseTableTest.generateDialogInput(model, EditAction.Delete);
        dialogResult = {context: model, action: EditAction.Delete};
        dialogSvcMock.generateDialogInput.and.returnValue(dialogInput);
      });

      fit('should call DialogService.openDialog with action delete', fakeAsync(() => {
        const spy =
          dialogSvcMock.openDialog.and.returnValue(asyncData(dialogResult));

        component.onDeleteClick(editEvent);
        tick();

        expect(spy).toHaveBeenCalledWith(dialogInput);
      }));

      fit('deleteItem should remove item from data source data source',  () => {
        const deleted = component.datasource().data[0];

        component.deleteItemEx(deleted.id);

        const found = component.datasource().data.find(item => item.id == deleted.id);
        expect(found).toBeFalsy();
      });

      fit('deleteItem should decrease the datasource size by one',  () => {
        const size = component.datasource().data.length;

        component.deleteItemEx(0);

        expect(component.datasource().data.length).toEqual(size-1);
      });
    });

    describe('openDialog with EventAction.Edit', () => {
      beforeEach(() => {
        const model = BaseTableTest.generateModel();
        dialogInput = BaseTableTest.generateDialogInput(model, EditAction.Edit);
        dialogResult = {context: model, action: EditAction.Edit};
        dialogSvcMock.generateDialogInput.and.returnValue(dialogInput);
      });

      fit('should call DialogService.openDialog with action edit', fakeAsync(() => {
        const spy =
          dialogSvcMock.openDialog.and.returnValue(asyncData(dialogResult));

        component.onEditClick(editEvent);
        tick();

        expect(spy).toHaveBeenCalledWith(dialogInput);
      }));

      fit('updateItem should update item in data source',  () => {
        const item = BaseTableTest.generateModel();
        item.id = 0;
        item.tableField = 'updated table field';

        component.updateItemEx(item);
        const updated = component.datasource().data[0];

        expect(updated).toEqual(item);
      });

      fit('updateItem should NOT change the datasource size',  () => {
        const size = component.datasource().data.length;
        const item = BaseTableTest.generateModel();
        item.id = 0;
        item.tableField = 'updated table field';

        component.updateItemEx(item);

        expect(component.datasource().data.length).toEqual(size);
      });
    });
  });
});
