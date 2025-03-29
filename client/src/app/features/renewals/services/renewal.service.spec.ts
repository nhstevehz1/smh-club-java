import {TestBed} from '@angular/core/testing';
import {provideHttpClient} from '@angular/common/http';
import {provideHttpClientTesting} from '@angular/common/http/testing';
import {RenewalService} from './renewal.service';

// http testing performed on the base class
describe('RenewalService', () => {
  let service: RenewalService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
          RenewalService,
          provideHttpClient(),
          provideHttpClientTesting(),
      ]
    });
    service = TestBed.inject(RenewalService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should set baseUri to /api/v1/renewals', () => {
    const baseUri = '/api/v1/renewals';
    expect(service.baseUri).toBe(baseUri);
  });
});
