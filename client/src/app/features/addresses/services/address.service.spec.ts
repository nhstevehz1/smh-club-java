import {TestBed} from '@angular/core/testing';

import {AddressService} from './address.service';
import {HttpTestingController, provideHttpClientTesting} from "@angular/common/http/testing";
import {PageRequest} from "../../../shared/models/page-request";
import {provideHttpClient} from "@angular/common/http";

describe('AddressService', () => {
  let service: AddressService;
  let controller: HttpTestingController;

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
    const pageRequest: PageRequest = PageRequest.of(undefined, undefined);
    service.getAddresses(pageRequest).subscribe();

    const req = controller.expectOne(service.BASE_API);
    expect(req.request.method).toBe('GET');

    req.flush([false, true, false]);
    controller.verify();
  });

  it('should call api with parameters when page request is populated', () => {
    const pageRequest: PageRequest = PageRequest.of(0, 0);
    const uri = service.BASE_API + pageRequest.createQuery();

    service.getAddresses(pageRequest).subscribe();

    const req = controller.expectOne(uri);
    expect(req.request.method).toBe('GET');

    req.flush([false, true, false]);
    controller.verify();
  });

  it('should return address create form', () => {
    const form = service.generateAddressForm();
    expect(form).toBeTruthy();
  });

  it('should return address update form when model is not null', () => {
    const form = service.generateAddressForm();

    expect(form).toBeTruthy();
  });
});
