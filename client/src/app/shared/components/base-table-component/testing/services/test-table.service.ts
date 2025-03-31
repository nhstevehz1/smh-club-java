import { Injectable } from '@angular/core';

import {BaseTableService} from '@app/shared/services/table-service';
import {ColumnDef} from '@app/shared/components/sortable-pageable-table';

import {TestModel} from '@app/shared/components/base-table-ex/testing';


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
