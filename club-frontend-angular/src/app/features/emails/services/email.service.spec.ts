import {TestBed} from '@angular/core/testing';
import {EmailService} from "./email.service";
import {provideHttpClient} from "@angular/common/http";
import {HttpTestingController, provideHttpClientTesting} from "@angular/common/http/testing";
import {PageRequest} from "../../../shared/models/page-request";

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
    let pageRequest: PageRequest = PageRequest.of(undefined, undefined);

    service.getEmails(pageRequest).subscribe(() => {});

    const req = controller.expectOne(baseUri);
    expect(req.request.method).toBe('GET');

    req.flush([false, true, false]);
    controller.verify();
  });

  it('should call api with parameters when page request is populated', () => {
    let pageRequest: PageRequest = PageRequest.of(0, 0);
    const uri = baseUri + pageRequest.createQuery();

    service.getEmails(pageRequest).subscribe(() => {});

    const req = controller.expectOne(uri);
    expect(req.request.method).toBe('GET');

    req.flush([false, true, false]);
    controller.verify();
  })
});
