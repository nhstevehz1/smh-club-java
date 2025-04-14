import {PageRequest, PagedData} from '@app/shared/services/api-service/models';
import {Observable} from 'rxjs';

export interface PagedDataService<T> {
  getPagedData(pageRequest: PageRequest): Observable<PagedData<T>>;
}
