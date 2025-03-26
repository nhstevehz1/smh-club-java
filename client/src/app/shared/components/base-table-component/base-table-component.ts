import {ColumnDef} from "../sortable-pageable-table/models/column-def";
import {SortDirection} from "@angular/material/sort";
import {PageRequest, SortDef} from "../../models/page-request";
import {FullName} from "../../models/full-name";
import {computed, Directive, Signal, signal} from '@angular/core';
import {MatTableDataSource} from '@angular/material/table';
import {PagedData} from '../../models/paged-data';
import {HttpErrorResponse} from '@angular/common/http';
import {AuthService} from '../../../core/auth/services/auth.service';
import {PermissionType} from '../../../core/auth/models/permission-type';

@Directive()
export abstract class BaseTableComponent<T> {
  resultsLength = signal(0);
  datasource = signal(new MatTableDataSource<T>());
  columns = signal<ColumnDef<T>[]>([]);
  hasWriteRole: Signal<boolean> = signal(false);

  constructor(auth: AuthService) {
    this.hasWriteRole = computed(() => auth.hasPermission(PermissionType.write));
  }
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
