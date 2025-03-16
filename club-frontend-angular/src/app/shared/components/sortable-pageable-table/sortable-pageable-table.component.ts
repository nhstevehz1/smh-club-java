import {AfterViewInit, Component, Input, ViewChild} from '@angular/core';
import {ColumnDef} from "./models/column-def";
import {MatTableDataSource, MatTableModule} from "@angular/material/table";
import {MatSort, MatSortModule} from "@angular/material/sort";
import {MatPaginator, MatPaginatorIntl, MatPaginatorModule} from "@angular/material/paginator";
import {_, TranslatePipe, TranslateService} from "@ngx-translate/core";
import {map} from "rxjs/operators";
import {Observable, of} from "rxjs";
import {CustomMatPaginatorIntlService} from "./services/custom-mat-paginator-intl.service";

@Component({
  selector: 'app-sortable-pageable-table',
  imports: [
    MatTableModule,
    MatSortModule,
    MatPaginatorModule,
    TranslatePipe
  ],
  providers: [
    TranslateService,
    {
      provide: MatPaginatorIntl,
      useClass: CustomMatPaginatorIntlService
    }
  ],
  templateUrl: './sortable-pageable-table.component.html',
  styleUrl: './sortable-pageable-table.component.scss'
})
export class SortablePageableTableComponent<T> implements AfterViewInit {
  @Input({required: true}) columns!: Array<ColumnDef<T>>;
  @Input({required: true}) dataSource!: MatTableDataSource<T>;
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

  constructor(private ts: TranslateService) {
  }
  ngAfterViewInit() {
    // revert back to page 0 if it had been changed.
    this.sort!.sortChange.subscribe(() => this.paginator!.pageIndex = 0);
  }

  translateDisplayName(column: ColumnDef<T>) : Observable<string> {
    const translate = column.translateDisplayName || true;
    if(translate) {
      return this.ts.get(_(column.displayName)).pipe(
          map((t) => t as string)
      );
    } else {
      return of(column.displayName);
    }
  }

  translateInstant(column: ColumnDef<T>): string {
    const translate = column.translateDisplayName || true;
    if(translate) {
      const instant = this.ts.instant(_(column.displayName));
      console.log('instant', instant);
      return this.ts.instant(_(`'${column.displayName}'`));
    } else {
      return column.displayName;
    }
  }

  shouldTranslate(column: ColumnDef<T>): boolean {
    return column.translateDisplayName || true;
  }

}
