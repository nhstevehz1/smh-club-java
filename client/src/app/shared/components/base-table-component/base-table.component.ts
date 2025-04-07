import {AfterViewInit, Component, computed, OnInit, signal, ViewChild} from '@angular/core';
import {HttpErrorResponse} from '@angular/common/http';

import {MatTableDataSource} from '@angular/material/table';
import {SortDirection} from '@angular/material/sort';

import {first, merge, Observable} from 'rxjs';
import {startWith, switchMap} from 'rxjs/operators';

import {AuthService} from '@app/core/auth/services/auth.service';
import {PermissionType} from '@app/core/auth/models/permission-type';

import {ColumnDef} from '@app-shared/components/sortable-pageable-table/models';
import {Updatable} from '@app-shared/models/updatable';
import {BaseTableService} from '@app-shared/services/table-service/base-table.service';
import {BaseEditDialogService} from '@app-shared/services/dialog-edit-service/base-edit-dialog.service';
import {EditDialogInput, EditDialogResult} from '@app/shared/components/base-edit-dialog/models';
import {
  SortablePageableTableComponent
} from '@app/shared/components/sortable-pageable-table/sortable-pageable-table.component';
import {BaseApiService} from '@app/shared/services/api-service/base-api.service';
import {PageRequest, SortDef, PagedData} from '@app/shared/services/api-service/models';
import {Editor} from '@app/shared/components/base-editor/editor';


@Component({
  selector: 'app-base-table',
  imports: [],
  template: ``
})
export abstract class BaseTableComponent<C, T extends Updatable, L extends Updatable, E extends Editor<T>> implements OnInit, AfterViewInit {

  @ViewChild(SortablePageableTableComponent, {static: true})
  table!: SortablePageableTableComponent<L>;

  readonly resultsLength = signal(0);
  readonly datasource = signal(new MatTableDataSource<L>());
  readonly columns = signal<ColumnDef<L>[]>([]);
  readonly hasWriteRole = computed(() => this.auth.hasPermission(PermissionType.write));
  readonly errors = signal<string | undefined>(undefined);

  protected constructor(protected auth: AuthService,
                        protected apiSvc: BaseApiService<L, C, T>,
                        protected tableSvc: BaseTableService<L>,
                        protected dialogSvc: BaseEditDialogService<T, E>) {
  }

  ngOnInit() {
    this.columns.set(this.tableSvc.getColumnDefs());
  }

  ngAfterViewInit() {
    merge(this.table.sort.sortChange, this.table.paginator.page)
      .pipe(
        startWith({}),
        switchMap(() => this.getCurrentPage())
      ).subscribe({
      // set the data source with the new page
      next: data => this.processPageData(data),
      error: (err: HttpErrorResponse) => this.processRequestError(err)
    });
  }

  protected openEditDialog(dialogInput: EditDialogInput<T, E>): Observable<EditDialogResult<T>> {
    return this.dialogSvc.openDialog(dialogInput);
  }

  protected updateItem(entity: L) {
    this.datasource.update((ds) => {
      const idx = ds.data.findIndex(item => item.id === entity.id);
      ds.data[idx] = entity;
      return ds;
    });
  }

  protected deleteItem(id: number) {
    this.datasource.update((ds) => {
      const idx = ds.data.findIndex(item => item.id === id);
       ds.data.splice(idx, 1);
      return ds;
    });
  }

  private getPageRequest(pageIndex?: number, pageSize?: number,
                           sort?: string, direction?: SortDirection ): PageRequest {

    const pr = PageRequest.of(
      pageIndex,
      pageSize,
    )

    // The dynamic page request supports multiple sort fields.
    // currently, our implementation supports only single column sort
    if(sort !== undefined) {
      const sortDef = SortDef.of(sort, direction);
      pr.addSort(sortDef);
    }

    return  pr;
  }

  private getCurrentPage(): Observable<PagedData<L>> {
    const pr = this.getPageRequest(
      this.table.paginator.pageIndex, this.table.paginator.pageSize,
      this.table.sort.active, this.table.sort.direction);

    return this.apiSvc.getPagedData(pr).pipe(first());
  }

  protected processPageData(data: PagedData<L>): void {
    this.resultsLength.update(() => data.page.totalElements);
    this.datasource.update((ds) => {
      ds.data = data._content;
      return ds;
    });
  }

  protected processRequestError(err: HttpErrorResponse): void {
    console.debug(err);
  }
}
