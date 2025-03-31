import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

import {BaseApiService, PagedData, PageRequest} from '@app/shared/services';

import {
  MockCreateModel,
  MockFullNameModel,
  MockModel
} from '@app/shared/services/api-service/testing/mock-api-data';

@Injectable()
export class MockCrudService extends BaseApiService<MockFullNameModel, MockCreateModel, MockModel> {

  constructor(http: HttpClient) {
    super('/api/v1/mock', http);
  }

  override getPagedData(pageRequest: PageRequest): Observable<PagedData<MockFullNameModel>> {
    return super.getPagedData(pageRequest);
  }

  override create(create: MockCreateModel): Observable<MockModel> {
    return super.create(create);
  }

  override update(id: number, update: MockModel): Observable<MockModel> {
    return super.update(id, update);
  }

  override delete(id: number): Observable<void> {
    return super.delete(id);
  }
}
