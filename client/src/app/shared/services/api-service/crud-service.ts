import {Observable} from 'rxjs';

import {PagedData, PageRequest} from '@app-shared/services/api-service/models';

export interface CrudService<L, C, T>  {
  readonly baseUri: string;
  getPagedData(pageRequest: PageRequest): Observable<PagedData<L>>;
  create(create: C): Observable<T>;
  update(id: number, update: T): Observable<T>;
  delete(id: number): Observable<void>;
}
