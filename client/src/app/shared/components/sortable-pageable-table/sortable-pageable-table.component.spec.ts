import {SortablePageableTableComponent} from './sortable-pageable-table.component';
import {ColumnDef} from "./models/column-def";
import {DateTime} from "luxon";
import {MatSortModule} from "@angular/material/sort";
import {MatPaginator} from "@angular/material/paginator";
import {MatTableDataSource, MatTableModule} from "@angular/material/table";
import {ComponentFixture, TestBed} from "@angular/core/testing";
import {HarnessLoader} from "@angular/cdk/testing";
import {TestbedHarnessEnvironment} from "@angular/cdk/testing/testbed";
import {provideNoopAnimations} from "@angular/platform-browser/animations";
import {MatHeaderCellHarness, MatHeaderRowHarness, MatTableHarness} from "@angular/material/table/testing";
import {MatSortHarness, MatSortHeaderHarness} from "@angular/material/sort/testing";
import {TranslateModule} from "@ngx-translate/core";

export interface TestModel {
  a_string: string;
  date_time: DateTime;
  a_number: number;
  a_boolean: boolean;
}

describe('SortablePageableTableComponent', () => {
  const columnDefs: ColumnDef<TestModel>[] = [
    {columnName: 'a_string', displayName: 'A String', isSortable: true,
      cell: (element:TestModel) => `${element.a_string}`},
    {columnName: 'date_time', displayName: 'A DateTime', isSortable: true,
      cell: (element:TestModel) => `${element.date_time}`},
    {columnName: 'a_number', displayName: 'A Number', isSortable: false,
      cell: (element:TestModel) => `${element.a_number}`},
    {columnName: 'a_boolean', displayName: 'A Boolean', isSortable: true,
      cell: (element:TestModel) => `${element.a_boolean}`},
  ] as ColumnDef<TestModel>[];

  const data: TestModel[] = [
    {a_string: "Field1", date_time: DateTime.now(), a_boolean: true, a_number: 1},
    {a_string: "Field2", date_time: DateTime.now(), a_boolean: false, a_number: 2},
    {a_string: "Field3", date_time: DateTime.now(), a_boolean: true, a_number: 3},
  ];

  let fixture: ComponentFixture<SortablePageableTableComponent<TestModel>>;
  let component: SortablePageableTableComponent<TestModel>;
  let loader: HarnessLoader;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        MatTableModule,
        MatSortModule,
        MatPaginator,
        TranslateModule.forRoot({})
      ],
      providers: [
        provideNoopAnimations()
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(SortablePageableTableComponent<TestModel>);
    component = fixture.componentInstance;
    loader = TestbedHarnessEnvironment.loader(fixture);
    fixture.componentRef.setInput('columns', columnDefs);
    fixture.componentRef.setInput('dataSource', new MatTableDataSource<TestModel>());
    fixture.detectChanges();
    await fixture.whenStable();
  });

  describe('test component inputs', () => {

    it('should create', () => {
      expect(component).toBeTruthy();
    });

    it('should input columns', () => {
      expect(component.column()).toEqual(columnDefs);

      columnDefs.forEach(columnDef => {
        expect(component.column().map((c => c.columnName))).toContain(columnDef.columnName);
      })
    });

    it('should return column names', () => {
      const columnNames = component.column().map(c => c.columnName);
      expect(component.columnNames()).toEqual(columnNames);
    });

    it('table should bind dataSource input', () => {
      expect(component.dataSource()).toBeTruthy();
    })

    it('table datasource should have items', async () => {
      fixture.componentRef.setInput('dataSource', new MatTableDataSource<TestModel>(data));
      fixture.detectChanges();
      await fixture.whenStable();
      expect(component.dataSource().data.length).toEqual(data.length);
    })

    it('should should bind paginator inputs', () =>  {
      expect(component.paginator).toBeTruthy();
    });

    it('paginator should contain default size options', () => {
      const defaultSizes = [5,10,25,100];
      expect(component.paginator.pageSizeOptions).toEqual(defaultSizes);
    });

    it('paginator should contain custom size options', async () => {
      const customSizes = [6,11,26,51];
      fixture.componentRef.setInput('pageSizes', customSizes);
      fixture.detectChanges();
      await fixture.whenStable();
      expect(component.paginator.pageSizeOptions).toEqual(customSizes);
    })

    it('paginator should contain default size', () => {
      fixture.detectChanges();
      expect(component.paginator.pageSize).toEqual(5);
    });

    it('paginator should contain default page index', () => {
      fixture.detectChanges();
      expect(component.paginator.pageIndex).toEqual(0);
    })
  });

  describe('render table',  () => {

    it('should render table', async () => {
      const tableHarness = await loader.getHarness(MatTableHarness);
      expect(tableHarness).toBeTruthy();
    });

    it('should render table with no columns', async () => {
      const tableHarness = await loader.getHarness(MatTableHarness);
      const cellTexts = await tableHarness.getCellTextByIndex();
      expect(cellTexts).toEqual([]);
    })

    it('should render table with correct column names and text', async () => {
      const columnHarnesses = await loader.getAllHarnesses(MatHeaderCellHarness);
      expect(columnHarnesses.length).toEqual(columnHarnesses.length);

      for (const harness of columnHarnesses) {
        const columnName = await harness.getColumnName();
        const columnText = await harness.getText();
        expect(columnDefs.map(c => c.columnName)).toContain(columnName);
        expect(columnDefs.map(c => c.displayName)).toContain(columnText);
      }
    });

    it('should render table with correct header row', async () => {
      const headerRowHarness = await loader.getHarness(MatHeaderRowHarness);
      expect(headerRowHarness).toBeTruthy();

      const cells = await headerRowHarness.getCellTextByIndex();
      expect(cells.length).toEqual(columnDefs.length);

      // cells should be in the correct order
      for(let ii = 0; ii < cells.length; ii++) {
        expect(cells[ii]).toEqual(columnDefs[ii].displayName);
      }
    });

    it('should render table with sortable columns', async () => {
      const sortHeaderHarness = await loader.getAllHarnesses(MatSortHeaderHarness);
      const sortableColumns = columnDefs.filter((columnDef) =>  columnDef.isSortable).length;
      expect(sortHeaderHarness.length).toEqual(sortableColumns);
    });

    it('should set active sort', async () => {
      fixture.detectChanges();

      // no initial active sort
      const sortHarness = await loader.getHarness(MatSortHarness);
      let active = await sortHarness.getActiveHeader();
      expect(active).toBeNull();

      const sortableColumns = columnDefs.filter((columnDef) =>  columnDef.isSortable);

      for (const cd of sortableColumns) {
        const harness = await loader.getHarness(MatSortHeaderHarness.with({label: cd.displayName}));
        expect(await harness.isActive()).toBeFalsy();
        await harness.click();

        active = await sortHarness.getActiveHeader();
        expect(active).toBeDefined();
        expect(await active?.getLabel() ).toEqual(cd.displayName);
        expect(await active?.getSortDirection() ).toEqual('asc');

        await harness.click();
        expect(await active?.getSortDirection() ).toEqual('desc');
      }
    });
  });

  describe('paginator', () => {
    it('should call paginator.pageIndex sort changes', async () => {
      component.ngAfterViewInit();
      component.paginator.pageIndex = 5;
      expect(component.paginator.pageIndex).toEqual(5);

      // get the first sort header
      const harness = await loader.getHarness(MatSortHeaderHarness);
      await harness.click();
      expect(component.paginator.pageIndex).toEqual(0);
    });
  });
});
