import {AfterViewInit, Component, Input, ViewChild} from '@angular/core';
import {ColumnDef} from "./models/column-def";
import {
  MatCell, MatCellDef,
  MatColumnDef,
  MatHeaderCell,
  MatHeaderCellDef, MatHeaderRow, MatHeaderRowDef, MatRow, MatRowDef,
  MatTable,
  MatTableDataSource
} from "@angular/material/table";
import {MatSort, MatSortHeader} from "@angular/material/sort";
import {MatPaginator} from "@angular/material/paginator";

@Component({
  selector: 'app-sortable-pageable-table',
  imports: [
    MatTable,
    MatColumnDef,
    MatHeaderCell,
    MatSortHeader,
    MatCell,
    MatPaginator,
    MatHeaderRow,
    MatRow,
    MatSort,
    // do not remove list below.  Intellij IDE thinks they are not being used.
    MatHeaderCellDef,
    MatCellDef,
    MatHeaderRowDef,
    MatRowDef,
  ],
  templateUrl: './sortable-pageable-table.component.html',
  styleUrl: './sortable-pageable-table.component.scss'
})
export class SortablePageableTableComponent implements AfterViewInit {
  @Input() columns!: ColumnDef<any>[];
  @Input() dataSource!: MatTableDataSource<any>;
  @Input() pageSizes: number[] = [5,10,25,100];
  @Input() pageSize: number = 5;
  @Input() resultsLength: number = 0;

  @ViewChild(MatSort, {static: true})
  sort!: MatSort;

  @ViewChild(MatPaginator, {static: true})
  paginator!: MatPaginator;

  get columnNames(): string[] {
    return this.columns.map(column => column.name);
  }
  ngAfterViewInit() {
    // revert back to page 0 if it had been changed.
    this.sort!.sortChange.subscribe(() => this.paginator!.pageIndex = 0);
  }
}
