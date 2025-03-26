import {AfterViewInit, Component, computed, input, output, ViewChild} from '@angular/core';
import {ColumnDef} from "./models/column-def";
import {MatTableDataSource, MatTableModule} from "@angular/material/table";
import {MatSort, MatSortModule} from "@angular/material/sort";
import {MatPaginator, MatPaginatorIntl, MatPaginatorModule} from "@angular/material/paginator";
import {CustomMatPaginatorIntlService} from "./services/custom-mat-paginator-intl.service";
import {TranslatePipe} from "@ngx-translate/core";
import {MatButtonModule} from '@angular/material/button';
import {MatIconModule} from '@angular/material/icon';
import {EditEvent} from '../edit-dialog/models/edit-event';

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
  columns = input.required<ColumnDef<T>[]>();
  dataSource = input.required<MatTableDataSource<T>>();
  pageSizes = input<number[]>([5,10,25,100]);
  pageSize = input<number>(5);
  resultsLength = input<number>(0);
  hasWriteRole = input(false);
  showEditButton = input(false);
  showDeleteButton = input(false);
  showViewButton = input(false);
  shouldShowActions = computed(() =>
    this.showViewButton() || (this.hasWriteRole() && (this.showEditButton() || this.showDeleteButton()))
  );

  columnNames= computed<string[]>(() => {
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
    // revert back to page 0 if it had been changed.
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
