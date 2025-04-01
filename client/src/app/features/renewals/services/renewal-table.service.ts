import { Injectable } from '@angular/core';

import {BaseTableService} from '@app/shared/services';
import {ColumnDef} from '@app/shared/components/sortable-pageable-table';

import {RenewalMember} from '@app/features/renewals/models/renewal';

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
