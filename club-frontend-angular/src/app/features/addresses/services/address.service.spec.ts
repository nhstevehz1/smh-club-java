import {TestBed} from '@angular/core/testing';

import {AddressService} from './address.service';
import {HttpClient} from "@angular/common/http";
import {MockBuilder} from "ng-mocks";

describe('AddressService', () => {

  beforeEach(() => {
    return MockBuilder(AddressService)
        .mock(HttpClient);
  });

  it('should be created', () => {
    const service = TestBed.inject(AddressService);
    expect(service).toBeTruthy();
  });
});
