import {Observable} from 'rxjs';
import {Updatable} from '@app/shared/models/updatable';

export interface CrudService<T extends Updatable>  {
  readonly baseUri: string;
  get(id: number): Observable<T>;
  getAll(): Observable<T[]>;
  create(create: T): Observable<T>;
  update(update: T): Observable<T>;
  delete(id: number): Observable<void>;
}
