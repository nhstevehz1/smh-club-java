import {Updatable} from '@app/shared/models/updatable';

export interface TableModelCreate {
  tableField: string,
}

export interface TableModel extends TableModelCreate, Updatable {}
