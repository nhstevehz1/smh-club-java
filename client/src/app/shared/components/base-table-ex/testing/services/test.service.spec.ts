import { TestBed } from '@angular/core/testing';

import {TestService} from '@app/shared/components/base-table-ex/testing';

describe('TestService', () => {
  let service: TestService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TestService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
