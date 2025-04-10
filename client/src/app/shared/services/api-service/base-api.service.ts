import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

import {CrudService} from '@app/shared/services/api-service/crud-service';
import {PageRequest, PagedData} from '@app/shared/services/api-service/models';
import {Updatable} from '@app/shared/models/updatable';

export abstract class BaseApiService<L, C, T extends Updatable> implements CrudService<L, C, T> {

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

  get(id: number) : Observable<T> {
    return this.http.get<T>(`${this.baseUri}/${id}`);
  }

  create(create: C): Observable<T> {
    return this.http.post<T>(this.baseUri, create);
  }

  update(update: T): Observable<T> {
    return this.http.put<T>(`${this.baseUri}/${update.id}`, update );
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUri}/${id}`);
  }
}
