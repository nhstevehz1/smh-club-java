import {ColumnDef} from '../models';

export interface TableService<T> {
  getColumnDefs(): ColumnDef<T>[];
}
