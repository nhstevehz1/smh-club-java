import { Injectable } from '@angular/core';

import {BaseTableService} from '@app/shared/services/table-service/base-table.service';
import {ColumnDef} from '@app/shared/components/sortable-pageable-table/models';

import {TableModel} from '@app/shared/components/base-table-component/testing/models/test-models';

@Injectable()
export class MockTableService extends BaseTableService<TableModel>{

  constructor() {
    super();
  }

  getColumnDefs(): ColumnDef<TableModel>[] {
    return [{
      columnName: 'test',
      displayName: 'text',
      isSortable: false,
      cell:(element: TableModel) => `${element.tableField}`
    }];
  }
}
