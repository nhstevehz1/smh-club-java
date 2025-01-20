import {AfterViewInit, Component, Input, OnInit, ViewChild} from '@angular/core';
import {ColumnDef} from "./models/column-def";
import {
  MatCell, MatCellDef,
  MatColumnDef,
  MatHeaderCell, MatHeaderCellDef,
  MatHeaderRow, MatHeaderRowDef,
  MatRow, MatRowDef,
  MatTable,
  MatTableDataSource, MatTableModule
} from "@angular/material/table";
import {MatSort, MatSortHeader, MatSortModule} from "@angular/material/sort";
import {MatPaginator, MatPaginatorModule} from "@angular/material/paginator";
import {CdkColumnDef} from "@angular/cdk/table";

@Component({
  selector: 'app-sortable-pageable-table',
  imports: [
    MatTableModule,
    MatSortModule,
    MatPaginatorModule,
  ],
  templateUrl: './sortable-pageable-table.component.html',
  styleUrl: './sortable-pageable-table.component.scss'
})
export class SortablePageableTableComponent implements AfterViewInit {
  @Input() columns: Array<ColumnDef<any>> = []
  @Input() dataSource!: MatTableDataSource<any>;
  @Input() pageSizes: number[] = [5,10,25,100];
  @Input() pageSize: number = 5;
  @Input() resultsLength: number = 0;

  @ViewChild(MatSort, {static: true})
  sort!: MatSort;

  @ViewChild(MatPaginator, {static: true})
  paginator!: MatPaginator;

  get getColumnNames(): string[] {
    return this.columns!.map(column => column.columnName);
  }

  ngAfterViewInit() {
    // revert back to page 0 if it had been changed.
    this.sort!.sortChange.subscribe(() => this.paginator!.pageIndex = 0);
  }

}
