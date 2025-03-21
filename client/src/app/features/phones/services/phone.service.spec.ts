import {TestBed} from '@angular/core/testing';

import {PhoneService} from './phone.service';
import {HttpTestingController, provideHttpClientTesting} from "@angular/common/http/testing";
import {provideHttpClient} from "@angular/common/http";
import {PageRequest} from "../../../shared/models/page-request";
import {generatePhoneUpdate} from "../test/phone-test";

describe('PhoneService', () => {
  let service: PhoneService;
  let controller: HttpTestingController;
  const baseUri = '/api/v1/phones';

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
          PhoneService,
          provideHttpClient(),
          provideHttpClientTesting()
      ]
    });
    service = TestBed.inject(PhoneService);
    controller = TestBed.inject(HttpTestingController);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should call api with no parameters when page request is empty', () => {
    const pageRequest: PageRequest = PageRequest.of(undefined, undefined);

    service.getPhones(pageRequest).subscribe(():void => {});

    const req = controller.expectOne(baseUri);
    expect(req.request.method).toBe('GET');

    req.flush([false, true, false]);
    controller.verify();
  });

  it('should call api with parameters when page request is populated', () => {
    const pageRequest: PageRequest = PageRequest.of(0, 0);
    const uri = '/api/v1/phones' + pageRequest.createQuery();

    service.getPhones(pageRequest).subscribe((): void => {});

    const req = controller.expectOne(uri);
    expect(req.request.method).toBe('GET');

    req.flush([false, true, false]);
    controller.verify();
  });

  it('should return phone create form', () => {
    const form = service.generateCreateForm();
    expect(form).toBeTruthy();
  });

  it('should return phone update form', () => {
    const form = service.generateUpdateForm(generatePhoneUpdate());
    expect(form).toBeTruthy();
  });

  it('should return the correct phone update form', () => {
    const model = generatePhoneUpdate();
    const val = service.generateUpdateForm(model).value;
    expect(val).toEqual(model);
  });
});
