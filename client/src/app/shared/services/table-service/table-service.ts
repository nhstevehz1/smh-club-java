import {ColumnDef} from '@app/shared/components/sortable-pageable-table';

export interface TableService<T> {
  getColumnDefs(): ColumnDef<T>[];
}
