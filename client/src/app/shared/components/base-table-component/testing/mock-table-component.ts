import {Component} from '@angular/core';
import {SortDirection} from "@angular/material/sort";

import {AuthService} from '@app/core/auth';

import {TestType} from '@app/shared/components/base-table-component/testing/mock-table-data';
import {PageRequest} from '@app/shared/services/api-service';
import {BaseTableComponent} from '@app/shared/components/base-table-component/base-table-component';
import {FullName} from '@app/shared/models';


@Component({
  selector: 'app- mock-table-component',
  styles: ``,
  template: ``
})
export class MockTableComponent extends BaseTableComponent<TestType> {

  constructor(auth: AuthService) {
    super(auth);
  }
  processPageRequestExternal(pageIndex?: number, pageSize?: number,
                                sort?: string, direction?: SortDirection ): PageRequest {

    return this.getPageRequest(pageIndex, pageSize, sort, direction);
  }

  public getFullNameExternal(fullName: FullName): string {
    return this.getFullName(fullName);
  }

}
