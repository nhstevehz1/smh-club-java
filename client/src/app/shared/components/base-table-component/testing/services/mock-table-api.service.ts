import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';

import {BaseApiService} from '@app/shared/services/api-service/base-api.service';

import {TableModelCreate, TableModel} from '@app/shared/components/base-table-component/testing/models/test-models';

@Injectable()
export class MockTableApiService extends BaseApiService<TableModel, TableModelCreate, TableModel> {

  constructor(http: HttpClient) {
    super('/api/vi/test', http);
}
}
