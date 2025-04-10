import {TestBed} from '@angular/core/testing';
import {provideHttpClient} from '@angular/common/http';
import {provideHttpClientTesting} from '@angular/common/http/testing';

import {EmailService} from './email.service';

// http testing performed on the base class
describe('EmailService', () => {
  let service: EmailService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        EmailService,
        provideHttpClient(),
        provideHttpClientTesting(),
      ]
    });

    service = TestBed.inject(EmailService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should set baseUri to /api/v1/emails', () => {
    const baseUri = '/api/v1/emails';
    expect(service.baseUri).toBe(baseUri);
  })
});
