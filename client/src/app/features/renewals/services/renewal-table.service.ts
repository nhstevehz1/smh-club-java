import { Injectable } from '@angular/core';
import {BaseTableService} from '../../../shared/components/sortable-pageable-table/services';
import {RenewalMember} from '../models';
import {ColumnDef} from '../../../shared/components/sortable-pageable-table/models';

@Injectable({
  providedIn: 'root'
})
export class RenewalTableService extends BaseTableService<RenewalMember>{

  constructor() {
    super();
  }

  getColumnDefs(): ColumnDef<RenewalMember>[] {
    return [{
        columnName: 'renewal_date',
        displayName: 'renewals.list.columns.renewalDate',
        isSortable: true,
        cell: (element: RenewalMember) => `${element.renewal_date}`,
      }, {
        columnName: 'renewal_year',
        displayName: 'renewals.list.columns.renewalYear',
        isSortable: true,
        cell: (element: RenewalMember) => `${element.renewal_year}`
      }, {
        columnName: 'full_name',
        displayName: 'renewals.list.columns.fullName',
        isSortable: true,
        cell: (element: RenewalMember) => this.getFullName(element.full_name)
      }
    ];
  }
}
