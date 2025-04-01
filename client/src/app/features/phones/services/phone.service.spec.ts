import {TestBed} from '@angular/core/testing';
import {provideHttpClientTesting} from '@angular/common/http/testing';
import {provideHttpClient} from '@angular/common/http';

import {PhoneService} from './phone.service';

describe('PhoneService', () => {
  let service: PhoneService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
          PhoneService,
          provideHttpClient(),
          provideHttpClientTesting()
      ]
    });
    service = TestBed.inject(PhoneService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should set baseUri to /api/v1/phones', () => {
    const baseUri = '/api/v1/phones';
    expect(service.baseUri).toBe(baseUri);
  });
});
