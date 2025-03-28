import {TestBed} from '@angular/core/testing';
import {EmailService} from "./email.service";
import {provideHttpClient} from "@angular/common/http";
import {HttpTestingController, provideHttpClientTesting} from "@angular/common/http/testing";
import {PageRequest} from "../../../shared/models/page-request";
import {generateEmailUpdate} from "../test/email-test";

describe('EmailServiceService', () => {
  let service: EmailService;
  let controller: HttpTestingController;
  const baseUri = '/api/v1/emails';

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        EmailService,
        provideHttpClient(),
        provideHttpClientTesting(),
      ]
    });

    service = TestBed.inject(EmailService);
    controller = TestBed.inject(HttpTestingController);
  });

  it('should be created', () => {
    const service = TestBed.inject(EmailService);
    expect(service).toBeTruthy();
  });

  it('should call api with no parameters when page request is empty', () => {
    const pageRequest: PageRequest = PageRequest.of(undefined, undefined);

    service.getEmails(pageRequest).subscribe();

    const req = controller.expectOne(baseUri);
    expect(req.request.method).toBe('GET');

    req.flush([false, true, false]);
    controller.verify();
  });

  it('should call api with parameters when page request is populated', () => {
    const pageRequest: PageRequest = PageRequest.of(0, 0);
    const uri = baseUri + pageRequest.createQuery();

    service.getEmails(pageRequest).subscribe();

    const req = controller.expectOne(uri);
    expect(req.request.method).toBe('GET');

    req.flush([false, true, false]);
    controller.verify();
  });

  it('should return email create form', () => {
    const form = service.generateCreateForm();
    expect(form).toBeTruthy();
  });

  it('should return email update form', () => {
    const form = service.generateUpdateForm(generateEmailUpdate());
    expect(form).toBeTruthy();
  });

  it('should return correct email update form', () => {
    const model = generateEmailUpdate();
    const val = service.generateUpdateForm(model).value;
    expect(val).toEqual(model);
  });
});
