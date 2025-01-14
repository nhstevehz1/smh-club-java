import {TestBed} from '@angular/core/testing';

import {RenewalService} from './renewal.service';
import {provideHttpClient} from "@angular/common/http";
import {MockBuilder} from "ng-mocks";
import {HttpTestingController, provideHttpClientTesting} from "@angular/common/http/testing";
import {PageRequest} from "../../../shared/models/page-request";

describe('RenewalService', () => {

  beforeEach(() => {
    return MockBuilder(RenewalService)
        .provide(provideHttpClient())
        .provide(provideHttpClientTesting());
  });

  it('should be created', () => {
    const service = TestBed.inject(RenewalService);
    expect(service).toBeTruthy();
  });

  it('should return paged data when page request is empty', () => {
    const service = TestBed.inject(RenewalService);
    let actual: any;

    let pageRequest: PageRequest = PageRequest.of(undefined, undefined);
    service.getRenewals(pageRequest).subscribe(value => actual = value);

    const httpMock = TestBed.inject(HttpTestingController);
    const req = httpMock.expectOne('/api/v1/renewals');

    expect(req.request.method).toBe('GET');
    req.flush([false, true, false]);
    httpMock.verify();
  });

  it('should return paged data when page request is populated', () => {
    const service = TestBed.inject(RenewalService);
    let actual: any;

    let pageRequest: PageRequest = PageRequest.of(0, 0);
    const uri = '/api/v1/renewals' + pageRequest.createQuery();
    service.getRenewals(pageRequest).subscribe(value => actual = value);

    const httpMock = TestBed.inject(HttpTestingController);
    const req = httpMock.expectOne(uri);

    expect(req.request.method).toBe('GET');
    req.flush([false, true, false]);
    httpMock.verify();
  })
});
