import {FullName} from '../../../models';
import {ColumnDef} from '../../../components/sortable-pageable-table/models';
import {FormModelGroup} from '../../../components/base-editor/form-model-group';
import {FormControl, FormGroup} from '@angular/forms';

export interface MockCreateModel {
  test: string;
}

export interface MockModel extends MockCreateModel {
  id: number;
}

export interface MockFullNameModel extends MockModel{
  fullName: FullName;
}

export function generateMockCreate(): MockCreateModel {
  return {
    test: 'test'
  }
}

export function generateMockModel(): MockModel {
  return {
    id: 0,
    test: 'test'
  }
}

export function generateMockColumnDefs(): ColumnDef<MockFullNameModel>[] {
  return [{
    columnName: 'test',
    displayName: 'test',
    isSortable: true,
    cell:(element: MockFullNameModel) => `${element.test}`
  }, {
    columnName:'full name',
    displayName: 'full name',
    isSortable: true,
    cell: (element: MockFullNameModel) => `${element.fullName}`
  }];
}

export function generateMockForm(): FormModelGroup<MockModel> {
  return new FormGroup({
    id: new FormControl(0, {nonNullable: true}),
    test: new FormControl('', {nonNullable: true})
  })
}
