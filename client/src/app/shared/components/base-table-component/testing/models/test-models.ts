import {Updatable} from '@app/shared/models/updatable';

export interface TableModel extends Updatable {
  tableField: string,
}
