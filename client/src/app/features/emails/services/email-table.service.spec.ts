import { TestBed } from '@angular/core/testing';

import { EmailTableService } from '@app/features/emails';

describe('EmailTableService', () => {
  let service: EmailTableService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [EmailTableService]
    });
    service = TestBed.inject(EmailTableService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
