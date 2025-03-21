import {TestBed} from '@angular/core/testing';

import {RenewalService} from './renewal.service';
import {provideHttpClient} from "@angular/common/http";
import {HttpTestingController, provideHttpClientTesting} from "@angular/common/http/testing";
import {PageRequest} from "../../../shared/models/page-request";

describe('RenewalService', () => {
  let service: RenewalService;
  let controller: HttpTestingController;
  const baseUri = '/api/v1/renewals';

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
          RenewalService,
          provideHttpClient(),
          provideHttpClientTesting(),
      ]
    });
    service = TestBed.inject(RenewalService);
    controller = TestBed.inject(HttpTestingController)
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should call api with no parameters when page request is empty', () => {
    const pageRequest: PageRequest = PageRequest.of(undefined, undefined);
    service.getRenewals(pageRequest).subscribe((): void => {});

    const req = controller.expectOne(baseUri);
    expect(req.request.method).toBe('GET');
    req.flush([false, true, false]);
    controller.verify();
  });

  it('should call api with parameters when page request is populated', () => {
    const pageRequest: PageRequest = PageRequest.of(0, 0);
    const uri = baseUri + pageRequest.createQuery();

    service.getRenewals(pageRequest).subscribe((): void => {});

    const req = controller.expectOne(uri);
    expect(req.request.method).toBe('GET');

    req.flush([false, true, false]);
    controller.verify();
  });
});
