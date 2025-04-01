import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';

import {BaseApiService} from '@app/shared/services/api-service';

import {TestCreate, TestModel} from '@app/shared/components/base-table-component/testing';

@Injectable()
export class TestService extends BaseApiService<TestModel, TestCreate, TestModel> {

  constructor(http: HttpClient) {
    super('/api/vi/test', http);
}
}
