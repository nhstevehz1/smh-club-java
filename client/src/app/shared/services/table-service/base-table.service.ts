import {ColumnDef} from '@app/shared/components/sortable-pageable-table/models';
import {FullName} from '@app/shared/models';
import {TableService} from '@app/shared/services/table-service/table-service';

export abstract class BaseTableService<T> implements TableService<T> {
  abstract getColumnDefs(): ColumnDef<T>[];

  protected getFullName(fullName: FullName): string {
    const first = fullName.first_name;
    const last = fullName.last_name;
    const middle = fullName.middle_name || '';
    const suffix = fullName.suffix || '';

    const firstName = `${first} ${middle}`.trim();
    const lastName = `${last} ${suffix}`.trim();
    return `${lastName}, ${firstName}`;
  }
}
