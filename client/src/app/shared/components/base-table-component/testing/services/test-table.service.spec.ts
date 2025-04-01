import { TestBed } from '@angular/core/testing';

import {TestTableService} from '@app/shared/components/base-table-component/testing';

describe('TestTableService', () => {
  let service: TestTableService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TestTableService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
