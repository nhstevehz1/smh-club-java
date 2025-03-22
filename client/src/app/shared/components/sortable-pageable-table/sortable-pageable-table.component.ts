import {AfterViewInit, Component, computed, input, ViewChild} from '@angular/core';
import {ColumnDef} from "./models/column-def";
import {MatTableDataSource, MatTableModule} from "@angular/material/table";
import {MatSort, MatSortModule} from "@angular/material/sort";
import {MatPaginator, MatPaginatorIntl, MatPaginatorModule} from "@angular/material/paginator";
import {CustomMatPaginatorIntlService} from "./services/custom-mat-paginator-intl.service";
import {TranslatePipe} from "@ngx-translate/core";

@Component({
  selector: 'app-sortable-pageable-table',
  imports: [
    MatTableModule,
    MatSortModule,
    MatPaginatorModule,
    TranslatePipe
  ],
  providers: [
    {
      provide: MatPaginatorIntl,
      useClass: CustomMatPaginatorIntlService
    }
  ],
  templateUrl: './sortable-pageable-table.component.html',
  styleUrl: './sortable-pageable-table.component.scss'
})
export class SortablePageableTableComponent<T> implements AfterViewInit {
  column
      = input.required<ColumnDef<T>[]>();

  dataSource
      = input.required<MatTableDataSource<T>>();

  pageSizes
      = input<number[]>([5,10,25,100]);

  pageSize
      = input<number>(5);

  resultsLength
      = input<number>(0);

  columnNames= computed<string[]>(() =>
      this.column().map(c => c.columnName));

  @ViewChild(MatSort, {static: true})
  sort!: MatSort;

  @ViewChild(MatPaginator, {static: true})
  paginator!: MatPaginator;

  ngAfterViewInit() {
    // revert back to page 0 if it had been changed.
    this.sort!.sortChange.subscribe(() => this.paginator!.pageIndex = 0);
  }

  shouldTranslate(column: ColumnDef<T>): boolean {
    return column.translateDisplayName || true;
  }
}
