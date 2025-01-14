import {TestBed} from '@angular/core/testing';

import {MembersService} from './members.service';
import {provideHttpClient} from "@angular/common/http";
import {MockBuilder} from "ng-mocks";
import {HttpTestingController, provideHttpClientTesting} from "@angular/common/http/testing";
import {PageRequest} from "../../../shared/models/page-request";

describe('MembersService', () => {

  beforeEach(() => {
    return MockBuilder(MembersService)
        .provide(provideHttpClient())
        .provide(provideHttpClientTesting());
  });

  it('should be created', () => {
    const service = TestBed.inject(MembersService);
    expect(service).toBeTruthy();
  });

  it('should return paged data when page request is empty', () => {
    const service = TestBed.inject(MembersService);
    let actual: any;

    let pageRequest: PageRequest = PageRequest.of(undefined, undefined);
    service.getMembers(pageRequest).subscribe(value => actual = value);

    const httpMock = TestBed.inject(HttpTestingController);
    const req = httpMock.expectOne('/api/v1/members');

    expect(req.request.method).toBe('GET');
    req.flush([false, true, false]);
    httpMock.verify();
  });

  it('should return paged data when page request is populated', () => {
    const service = TestBed.inject(MembersService);
    let actual: any;

    let pageRequest: PageRequest = PageRequest.of(0, 0);
    const uri = '/api/v1/members' + pageRequest.createQuery();
    service.getMembers(pageRequest).subscribe(value => actual = value);

    const httpMock = TestBed.inject(HttpTestingController);
    const req = httpMock.expectOne(uri);

    expect(req.request.method).toBe('GET');
    req.flush([false, true, false]);
    httpMock.verify();
  })

});
