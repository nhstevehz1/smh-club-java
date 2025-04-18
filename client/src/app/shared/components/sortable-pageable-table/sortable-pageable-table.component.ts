import {AfterViewInit, Component, computed, input, output, ViewChild, booleanAttribute} from '@angular/core';

import {MatTableDataSource, MatTableModule} from '@angular/material/table';
import {MatSort, MatSortModule} from '@angular/material/sort';
import {MatPaginator, MatPaginatorIntl, MatPaginatorModule} from '@angular/material/paginator';
import {MatButtonModule} from '@angular/material/button';
import {MatIconModule} from '@angular/material/icon';

import {TranslatePipe} from '@ngx-translate/core';

import {EditEvent} from '@app/shared/components/base-edit-dialog/models';
import {
  CustomMatPaginatorIntlService
} from '@app/shared/components/sortable-pageable-table/services/custom-mat-paginator-intl.service';
import {ColumnDef} from '@app/shared/components/sortable-pageable-table/models';

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
  providers: [{
      provide: MatPaginatorIntl,
      useClass: CustomMatPaginatorIntlService
    }
  ],
  templateUrl: './sortable-pageable-table.component.html',
  styleUrl: './sortable-pageable-table.component.scss'
})
export class SortablePageableTableComponent<T> implements AfterViewInit {
  columns = input<ColumnDef<T>[]>([]);
  dataSource = input.required<MatTableDataSource<T>>();
  pageSizes = input<number[]>([5,10,25,100]);
  pageSize = input<number>(5);
  resultsLength = input<number>(0);
  hasWriteRole = input(false, {transform: booleanAttribute});
  showEditButton = input(false, {transform: booleanAttribute});
  showDeleteButton = input(false, {transform: booleanAttribute});
  showViewButton = input(false, {transform: booleanAttribute});
  shouldShowActions = computed(() =>
    this.showViewButton() || (this.hasWriteRole() && (this.showEditButton() || this.showDeleteButton()))
  );

  columnNames = computed<string[]>(() => {
    const names = this.columns().map(c => c.columnName);
    if (this.shouldShowActions()) {
      names.push('action');
    }
    return names;
  });

  editClicked = output<EditEvent<T>>();
  deleteClicked = output<EditEvent<T>>();
  viewClicked = output<EditEvent<T>>();

  @ViewChild(MatSort, {static: true})
  sort!: MatSort;

  @ViewChild(MatPaginator, {static: true})
  paginator!: MatPaginator;

  ngAfterViewInit() {
    // revert back to page 0 if sort changed.
    this.sort!.sortChange.subscribe(() => this.paginator!.pageIndex = 0);
  }

  onViewClick(element: T, index: number): void {
    this.viewClicked.emit({idx: index, data: element});
  }

  onEditClick(element: T, index: number): void {
    this.editClicked.emit({idx: index, data: element});
  }

  onDeleteClick(element: T, index: number) {
    this.deleteClicked.emit({idx: index, data: element});
  }
}
