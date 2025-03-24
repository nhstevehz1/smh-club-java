import {AfterViewInit, Component, computed, input, output, ViewChild} from '@angular/core';
import {ColumnDef} from "./models/column-def";
import {MatTableDataSource, MatTableModule} from "@angular/material/table";
import {MatSort, MatSortModule} from "@angular/material/sort";
import {MatPaginator, MatPaginatorIntl, MatPaginatorModule} from "@angular/material/paginator";
import {CustomMatPaginatorIntlService} from "./services/custom-mat-paginator-intl.service";
import {TranslatePipe} from "@ngx-translate/core";
import {MatButtonModule} from '@angular/material/button';
import {MatIconModule} from '@angular/material/icon';
import {AuthService} from '../../../core/auth/services/auth.service';
import {PermissionType} from '../../../core/auth/models/permission-type';
import {EditEvent} from '../../models/edit-event';

@Component({
  selector: 'app-sortable-pageable-table',
  imports: [
    MatTableModule,
    MatSortModule,
    MatPaginatorModule,
    TranslatePipe,
    MatIconModule,
    MatButtonModule
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
  columns
      = input.required<ColumnDef<T>[]>();

  dataSource
      = input.required<MatTableDataSource<T>>();

  pageSizes
      = input<number[]>([5,10,25,100]);

  pageSize
      = input<number>(5);

  resultsLength
      = input<number>(0);

  columnNames= computed<string[]>(() => {
    const names = this.columns().map(c => c.columnName);
    if (this.auth.hasPermission(PermissionType.write)) {
      names.push('action');
    }
    return names;
  });

  hasWriteRole = input(false);

  editClicked = output<EditEvent<T>>();

  @ViewChild(MatSort, {static: true})
  sort!: MatSort;

  @ViewChild(MatPaginator, {static: true})
  paginator!: MatPaginator;

  constructor(private auth: AuthService) {}

  ngAfterViewInit() {
    // revert back to page 0 if it had been changed.
    this.sort!.sortChange.subscribe(() => this.paginator!.pageIndex = 0);
  }

  onEditClick(element: T, index: number) {
    console.debug('onEditClick element', element);
    this.editClicked.emit({idx: index, data: element});
  }
}
