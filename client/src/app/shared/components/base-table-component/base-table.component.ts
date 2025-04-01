import {AfterViewInit, Component, computed, OnInit, signal, ViewChild} from '@angular/core';
import {HttpErrorResponse} from '@angular/common/http';

import {MatTableDataSource} from '@angular/material/table';
import {SortDirection} from '@angular/material/sort';

import {first, merge, mergeMap, Observable, tap} from 'rxjs';
import {startWith, switchMap} from 'rxjs/operators';

import {AuthService} from '@app/core/auth/services/auth.service';
import {PermissionType} from '@app/core/auth/models/permission-type';

import {ColumnDef, SortablePageableTableComponent} from '@app-shared/components/sortable-pageable-table';
import {Updatable} from '@app-shared/models/updatable';
import {BaseTableService} from '@app-shared/services/table-service';
import {PagedData, PageRequest, SortDef, BaseApiService} from '@app-shared/services/api-service';
import {BaseEditDialogService} from '@app-shared/services/dialog-edit-service';
import {EditAction, EditDialogInput, EditDialogResult} from '@app/shared/components/edit-dialog';

@Component({
  selector: 'app-base-table-ex',
  imports: [],
  template: ``,
  styles: ``
})
export abstract class BaseTableComponent<C, T extends Updatable, L> implements OnInit, AfterViewInit {

  @ViewChild(SortablePageableTableComponent, {static: true})
  table!: SortablePageableTableComponent<L>;

  resultsLength = signal(0);
  datasource = signal(new MatTableDataSource<L>());
  columns = signal<ColumnDef<L>[]>([]);
  hasWriteRole = computed(() => this.auth.hasPermission(PermissionType.write));
  errors = signal<string | undefined>(undefined);

  protected constructor(protected auth: AuthService,
                        protected apiSvc: BaseApiService<L, C, T>,
                        protected tableSvc: BaseTableService<L>,
                        protected dialogSvc: BaseEditDialogService<T>) {
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

  protected openEditDialog(dialogInput: EditDialogInput<T>): Observable<EditDialogResult<T>> {
    return this.dialogSvc.openDialog(dialogInput).pipe(
      tap(result => {
        if(result.action == EditAction.Edit) {
          this.updateEntity(result.context)
        } else if(result.action == EditAction.Delete) {
          this.deleteEntity(result.context);
        }
      }),
    );
  }

  private updateEntity(entity: T) {
    this.apiSvc.update(entity.id, entity).pipe(
      mergeMap(() => this.getCurrentPage())
    ).subscribe({
      next: data => this.processPageData(data),
      error: (err: HttpErrorResponse) => this.processRequestError(err)
    });
  }

  private deleteEntity(entity: T) {
    this.apiSvc.delete(entity.id).pipe(
      mergeMap(() => this.getCurrentPage())
    ).subscribe({
      next: data => this.processPageData(data),
      error: (err: HttpErrorResponse) => this.processRequestError(err)
    })
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
    this.resultsLength.set(data.page.totalElements);
    this.datasource().data = data._content;
  }

  protected processRequestError(err: HttpErrorResponse): void {
    console.debug(err);
  }
}
