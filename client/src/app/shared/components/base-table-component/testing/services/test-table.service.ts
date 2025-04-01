import { Injectable } from '@angular/core';

import {BaseTableService} from '@app/shared/services/table-service/base-table.service';
import {ColumnDef} from '@app/shared/components/sortable-pageable-table/models';

import {TestModel} from '@app/shared/components/base-table-component/testing/models/test-models';

@Injectable()
export class TestTableService extends BaseTableService<TestModel>{

  constructor() {
    super();
  }

  getColumnDefs(): ColumnDef<TestModel>[] {
    console.debug('table service get column defs');
    return [{
      columnName: 'test',
      displayName: 'text',
      isSortable: false,
      cell:(element: TestModel) => `${element.test}`
    }];
  }
}
