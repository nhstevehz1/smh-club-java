import {TestBed} from '@angular/core/testing';

import {PhoneService} from './phone.service';
import {HttpTestingController, provideHttpClientTesting} from "@angular/common/http/testing";
import {provideHttpClient} from "@angular/common/http";
import {MockBuilder} from "ng-mocks";
import {PageRequest} from "../../../shared/models/page-request";

describe('PhoneService', () => {

  beforeEach(() => {
    return MockBuilder(PhoneService)
        .provide(provideHttpClient())
        .provide(provideHttpClientTesting());
  });

  it('should be created', () => {
    const service = TestBed.inject(PhoneService);
    expect(service).toBeTruthy();
  });

  it('should return paged data when page request is empty', () => {
    const service = TestBed.inject(PhoneService);
    let actual: any;

    let pageRequest: PageRequest = PageRequest.of(undefined, undefined);
    service.getPhones(pageRequest).subscribe(value => actual = value);

    const httpMock = TestBed.inject(HttpTestingController);
    const req = httpMock.expectOne('/api/v1/phones');

    expect(req.request.method).toBe('GET');
    req.flush([false, true, false]);
    httpMock.verify();
  });

  it('should return paged data when page request is populated', () => {
    const service = TestBed.inject(PhoneService);
    let actual: any;

    let pageRequest: PageRequest = PageRequest.of(0, 0);
    const uri = '/api/v1/phones' + pageRequest.createQuery();
    service.getPhones(pageRequest).subscribe(value => actual = value);

    const httpMock = TestBed.inject(HttpTestingController);
    const req = httpMock.expectOne(uri);

    expect(req.request.method).toBe('GET');
    req.flush([false, true, false]);
    httpMock.verify();
  })
});
