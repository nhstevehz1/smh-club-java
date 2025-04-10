import {FormControl, FormGroup} from '@angular/forms';

import {FullName} from '@app/shared/models';
import {ColumnDef} from '@app/shared/components/sortable-pageable-table/models';
import {FormModelGroup} from '@app/shared/components/base-editor/models';

export interface ApiCreateMode {
  apiModelField: string;
}

export interface ApiModel extends ApiCreateMode {
  id: number;
}

export interface ApiFullNameModel extends ApiModel{
  fullName: FullName;
}

export function generateApiCreateModel(): ApiCreateMode {
  return {
    apiModelField: 'test'
  }
}

export function generateApiModel(): ApiModel {
  return {
    id: 0,
    apiModelField: 'test'
  }
}

export function generateApiColumnDefs(): ColumnDef<ApiFullNameModel>[] {
  return [{
    columnName: 'test',
    displayName: 'test',
    isSortable: true,
    cell:(element: ApiFullNameModel) => `${element.apiModelField}`
  }, {
    columnName:'full name',
    displayName: 'full name',
    isSortable: true,
    cell: (element: ApiFullNameModel) => `${element.fullName}`
  }];
}

export function generateMockForm(): FormModelGroup<ApiModel> {
  return new FormGroup({
    id: new FormControl(0, {nonNullable: true}),
    apiModelField: new FormControl('', {nonNullable: true})
  })
}
