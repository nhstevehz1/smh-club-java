import {computed, Directive, signal} from '@angular/core';
import {HttpErrorResponse} from '@angular/common/http';

import {SortDirection} from '@angular/material/sort';
import {MatTableDataSource} from '@angular/material/table';

import {AuthService, PermissionType} from '@app/core/auth';

import {ColumnDef} from '@app/shared/components/sortable-pageable-table';
import {PagedData, PageRequest, SortDef} from '@app/shared/services/api-service';
import {FullName} from '@app/shared/models';

@Directive()
export abstract class BaseTableComponent<T> {
  resultsLength = signal(0);
  datasource = signal(new MatTableDataSource<T>());
  hasWriteRole = computed(() => this.auth.hasPermission(PermissionType.write));
  columns= signal<ColumnDef<T>[]>([]);

  protected constructor(protected auth: AuthService) {}

  protected getPageRequest(pageIndex?: number, pageSize?: number,
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

  protected getFullName(fullName: FullName): string {
    const first = fullName.first_name;
    const last = fullName.last_name;
    const middle = fullName.middle_name || '';
    const suffix = fullName.suffix || '';

    const firstName = `${first} ${middle}`.trim();
    const lastName = `${last} ${suffix}`.trim();
    return `${lastName}, ${firstName}`;
  }

  protected processPageData(data: PagedData<T>): void {
    this.resultsLength.update(() => data.page.totalElements);
    this.datasource().data = data._content;
  }

  protected processRequestError(err: HttpErrorResponse): void {
    console.debug(err);
  }
}
