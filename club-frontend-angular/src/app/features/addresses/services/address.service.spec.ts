import {TestBed} from '@angular/core/testing';

import {AddressService} from './address.service';
import {HttpTestingController, provideHttpClientTesting} from "@angular/common/http/testing";
import {PageRequest} from "../../../shared/models/page-request";
import {provideHttpClient} from "@angular/common/http";

describe('AddressService', () => {
  let service: AddressService;
  let controller: HttpTestingController;
  const baseUri = '/api/v1/addresses';

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
          AddressService,
          provideHttpClient(),
          provideHttpClientTesting(),
      ]
    });
    service = TestBed.inject(AddressService);
    controller = TestBed.inject(HttpTestingController);
  });


  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should call api with no parameters when page request is empty', () => {
    let pageRequest: PageRequest = PageRequest.of(undefined, undefined);

    service.getAddresses(pageRequest).subscribe(() => {});

    const req = controller.expectOne(baseUri);
    expect(req.request.method).toBe('GET');

    req.flush([false, true, false]);
    controller.verify();
  });

  it('should call api with parameters when page request is populated', () => {
    let pageRequest: PageRequest = PageRequest.of(0, 0);
    const uri = baseUri + pageRequest.createQuery();

    service.getAddresses(pageRequest).subscribe(() => {});

    const req = controller.expectOne(uri);
    expect(req.request.method).toBe('GET');

    req.flush([false, true, false]);
    controller.verify();
  });
});
