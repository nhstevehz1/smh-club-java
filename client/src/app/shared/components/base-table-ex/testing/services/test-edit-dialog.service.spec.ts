import { TestBed } from '@angular/core/testing';

import { TestEditDialogService } from './test-edit-dialog.service';

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
