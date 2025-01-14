import {TestBed} from '@angular/core/testing';

import {AddressService} from './address.service';
import {HttpClient, provideHttpClient} from "@angular/common/http";
import {provideHttpClientTesting} from "@angular/common/http/testing";

describe('AddressService', () => {
  let service: AddressService;
  let httpClient: HttpClient;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        AddressService,
        provideHttpClient(),
        provideHttpClientTesting()]
    });
    service = TestBed.inject(AddressService);
    httpClient = TestBed.inject(HttpClient);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
    expect(httpClient).toBeTruthy();
  });
});
