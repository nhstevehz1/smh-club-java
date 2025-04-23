import {Observable} from 'rxjs';

export interface FilterService<C, T extends C> {
  getAllByFilter(id: number): Observable<T[]>;
  create(create: C): Observable<T>;
}
