import { ComponentFixture, TestBed } from '@angular/core/testing';
import {provideNoopAnimations} from '@angular/platform-browser/animations';
import {TranslateModule} from '@ngx-translate/core';

import {AuthService} from '@app/core/auth/services/auth.service';

import {asyncData} from '@app/shared/testing/test-helpers';
import {ColumnDef} from '@app/shared/components/sortable-pageable-table/models';
import {TestService} from '@app/shared/components/base-table-component/testing/services/test.service';
import {TestTableService} from '@app/shared/components/base-table-component/testing/services/test-table.service';
import {
  TestEditDialogService
} from '@app/shared/components/base-table-component/testing/services/test-edit-dialog.service';
import {
  generateTestModelPagedData,
  generateTestModelColumnDefs
} from '@app/shared/components/base-table-component/testing/test-support';
import {ListTestComponent} from '@app/shared/components/base-table-component/testing/list-test/list-test.component';
import {TestModel} from '@app/shared/components/base-table-component/testing/models/test-models';
import {PagedData} from '@app/shared/services/api-service/models';

describe('ListTestComponent', () => {
  let component: ListTestComponent;
  let fixture: ComponentFixture<ListTestComponent>;

  let authMock: jasmine.SpyObj<AuthService>;
  let svcMock: jasmine.SpyObj<TestService>;
  let tableSvcMock: jasmine.SpyObj<TestTableService>;
  let dialogSvcMock: jasmine.SpyObj<TestEditDialogService>;

  let data: PagedData<TestModel>;
  let columnDefs: ColumnDef<TestModel>[];

  beforeEach(async () => {
    authMock = jasmine.createSpyObj('AuthService', ['hasPermission']);
    svcMock = jasmine.createSpyObj('TestService', ['getPagedData', 'update', 'delete']);
    tableSvcMock = jasmine.createSpyObj('TestTableService', ['getColumnDefs']);
    dialogSvcMock = jasmine.createSpyObj('TestEditDialogService', ['generateForm', 'generateDialogInput']);
    authMock.hasPermission.and.returnValue(true);

    await TestBed.configureTestingModule({
      imports: [
        ListTestComponent,
        TranslateModule.forRoot({})
      ],
      providers: [
        {provide: AuthService, useValue: {}},
        {provide: TestService, useValue: {}},
        {provide: TestTableService, useValue: {}},
        {provide: TestEditDialogService, useValue: {}},
        provideNoopAnimations()
      ]
    }).overrideProvider(AuthService, {useValue: authMock})
      .overrideProvider(TestService, {useValue: svcMock})
      .overrideProvider(TestTableService, {useValue: tableSvcMock})
      .overrideProvider(TestEditDialogService, {useValue: dialogSvcMock})
      .compileComponents();

    fixture = TestBed.createComponent(ListTestComponent);
    component = fixture.componentInstance;

    data = generateTestModelPagedData(0, 5, 1);
    columnDefs = generateTestModelColumnDefs();
    tableSvcMock.getColumnDefs.and.returnValue(columnDefs);
    svcMock.getPagedData.and.returnValue(asyncData(data));
  });

  fit('should create', async () => {
    fixture.detectChanges();
    await fixture.whenStable();

    expect(component).toBeTruthy();
  });
});
