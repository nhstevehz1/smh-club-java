import {PagedData} from '@app/shared/services/api-service';

import {ColumnDef} from '@app/shared/components/sortable-pageable-table';
import {generatePagedData} from '@app/shared/testing';
import {TestModel} from '@app/shared/components/base-table-component/testing';

export function generateTestModelPagedData(page: number, size: number, total: number): PagedData<TestModel> {
  const content = generateTestModelList(size);
  return generatePagedData(page, size, total, content);
}

export function generateTestModelList(size: number): TestModel[] {
  const list: TestModel[] = [];

  for(let ii = 0; ii < size; ii++) {
    list.push({id: ii, test: `${ii}test`});
  }

  return list;
}

export function generateTestModelColumnDefs(): ColumnDef<TestModel>[] {
  return [{
    columnName: 'test',
    displayName: 'test',
    isSortable: false,
    cell: (element: TestModel) => `${element.test}`
  }]
}
