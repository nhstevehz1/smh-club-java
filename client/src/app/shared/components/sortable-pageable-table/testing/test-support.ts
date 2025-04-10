import {DateTime} from 'luxon';
import {ColumnDef} from '@app/shared/components/sortable-pageable-table/models';

export interface TestModel {
  a_string: string;
  date_time: DateTime;
  a_number: number;
  a_boolean: boolean;
}

export class SortTableTest {

  static getColumnDefs(): ColumnDef<TestModel>[] {
     return [{
       columnName: 'a_string',
       displayName: 'A String',
       isSortable: true,
       cell: (element:TestModel) => `${element.a_string}`
     }, {
       columnName: 'date_time',
       displayName: 'A DateTime',
       isSortable: true,
       cell: (element:TestModel) => `${element.date_time}`
     }, {
       columnName: 'a_number',
       displayName: 'A Number',
       isSortable: false,
       cell: (element:TestModel) => `${element.a_number}`
     }, {
       columnName: 'a_boolean',
       displayName: 'A Boolean',
       isSortable: true,
       cell: (element:TestModel) => `${element.a_boolean}`}
      ]
  }

  static generateTestModelList(): TestModel[] {
    return [
      {a_string: 'Field1', date_time: DateTime.now(), a_boolean: true, a_number: 1},
      {a_string: 'Field2', date_time: DateTime.now(), a_boolean: false, a_number: 2},
      {a_string: 'Field3', date_time: DateTime.now(), a_boolean: true, a_number: 3},
    ]
  }
}
