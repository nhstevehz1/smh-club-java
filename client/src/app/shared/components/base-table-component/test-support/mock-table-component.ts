import {Component, Directive} from '@angular/core';
import {SortDirection} from "@angular/material/sort";
import {BaseTableComponent} from '../base-table-component';
import {AuthService} from '../../../../core/auth/services/auth.service';
import {FullName, PageRequest} from '../../../models';
import {TestType} from './mock-table-data';

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
