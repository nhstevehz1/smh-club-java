import {CrudService} from './crud-service';
import {Observable} from 'rxjs';
import {PagedData, PageRequest} from '../../models';
import {HttpClient} from '@angular/common/http';

export abstract class BaseApiService<L, C, T> implements CrudService<L, C, T> {

  readonly baseUri: string;

  protected constructor(baseUri: string,
                        private http: HttpClient) {
    this.baseUri = baseUri;
  }

  getPagedData(pageRequest: PageRequest): Observable<PagedData<L>> {
    const query = pageRequest.createQuery();
    const uri = query == null ? this.baseUri: this.baseUri + query;

    return this.http.get<PagedData<L>>(uri);
  }

  create(create: C): Observable<T> {
    return this.http.post<T>(this.baseUri, create);
  }

  update(id: number, update: T): Observable<T> {
    return this.http.put<T>(`${this.baseUri}/${id}`, update );
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUri}/${id}`);
  }
}
