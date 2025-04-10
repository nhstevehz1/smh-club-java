import {TestBed} from '@angular/core/testing';
import {provideHttpClient} from '@angular/common/http';
import {provideHttpClientTesting} from '@angular/common/http/testing';

import {AddressService} from './address.service';

// http testing performed in the base class
describe('AddressService', () => {
  let service: AddressService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        AddressService,
        provideHttpClient(),
        provideHttpClientTesting()
      ]
    });
    service = TestBed.inject(AddressService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should set baseUri to /api/v1/addresses', () => {
    const baseUri = '/api/v1/addresses';
    expect(service.baseUri).toBe(baseUri);
  })
});
