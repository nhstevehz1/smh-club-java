import {ColumnDef} from '@app/shared/components/sortable-pageable-table/models';

export interface TableService<T> {
  getColumnDefs(): ColumnDef<T>[];
}
