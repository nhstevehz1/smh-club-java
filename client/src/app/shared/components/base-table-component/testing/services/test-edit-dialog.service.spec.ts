import { TestBed } from '@angular/core/testing';

import {TestEditDialogService} from '@app/shared/components/base-table-component/testing';

describe('TestEditDialogService', () => {
  let service: TestEditDialogService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TestEditDialogService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
