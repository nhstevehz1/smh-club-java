import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {PageRequest, PagedData} from '@app/shared/services/api-service/models';
import {Updatable} from '@app/shared/models/updatable';
import {PagedDataService} from '@app/shared/services/api-service/paged-data-service';
import {BaseCrudService} from '@app/shared/services/api-service/base-crud.service';

export abstract class BaseApiService<T extends Updatable, P extends T> extends BaseCrudService<T> implements PagedDataService<P> {

  protected constructor(baseUri: string,
                        http: HttpClient) {
    super(baseUri, http);
  }

  getPagedData(pageRequest: PageRequest): Observable<PagedData<P>> {
    const query = pageRequest.createQuery();
    const uri = query == null ? `${this.baseUri}/page`: `${this.baseUri}/page${query}`;

    return this.http.get<PagedData<P>>(uri);
  }
}
