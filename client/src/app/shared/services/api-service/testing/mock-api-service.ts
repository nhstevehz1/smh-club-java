import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

import {
  ApiCreateMode,
  ApiFullNameModel,
  ApiModel
} from '@app/shared/services/api-service/testing/mock-api-data';
import {BaseApiService} from '@app/shared/services/api-service/base-api.service';
import {PageRequest, PagedData} from '@app/shared/services/api-service/models';


@Injectable()
export class MockApiService extends BaseApiService<ApiFullNameModel, ApiCreateMode, ApiModel> {

  constructor(http: HttpClient) {
    super('/api/v1/mock', http);
  }

  override getPagedData(pageRequest: PageRequest): Observable<PagedData<ApiFullNameModel>> {
    return super.getPagedData(pageRequest);
  }

  override create(create: ApiCreateMode): Observable<ApiModel> {
    return super.create(create);
  }

  override update(update: ApiModel): Observable<ApiModel> {
    return super.update(update);
  }

  override delete(id: number): Observable<void> {
    return super.delete(id);
  }
}
