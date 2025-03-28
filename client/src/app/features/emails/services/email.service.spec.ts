import {TestBed} from '@angular/core/testing';
import {EmailService} from "./email.service";
import {provideHttpClient} from "@angular/common/http";
import {HttpTestingController, provideHttpClientTesting} from "@angular/common/http/testing";
import {PageRequest} from "../../../shared/models/page-request";
import {generateEmail, generateEmailCreate} from '../test/email-test';

describe('EmailService', () => {
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

  it('should call GET api with no parameters when page request is empty', () => {
    const pageRequest: PageRequest = PageRequest.of(undefined, undefined);

    service.getPagedData(pageRequest).subscribe();

    const req = controller.expectOne(baseUri);
    expect(req.request.method).toBe('GET');

    req.flush([false, true, false]);
    controller.verify();
  });

  it('should call GET api with parameters when page request is populated', () => {
    const pageRequest: PageRequest = PageRequest.of(0, 0);
    const uri = baseUri + pageRequest.createQuery();

    service.getPagedData(pageRequest).subscribe();

    const req = controller.expectOne(uri);
    expect(req.request.method).toBe('GET');

    req.flush([false, true, false]);
    controller.verify();
  });

  it('should call POST api', () =>{
    const emailCreate = generateEmailCreate();

    service.createEmail(emailCreate).subscribe();

    const req = controller.expectOne(baseUri);
    expect(req.request.method).toBe('POST');

    req.flush([false, true, false]);
    controller.verify();
  });

  it('should call PUT api', () =>{
    const email = generateEmail();
    const uri = `${baseUri}/${email.id}`;

    service.updateEmail(email).subscribe();

    const req = controller.expectOne(uri);
    expect(req.request.method).toBe('PUT');

    req.flush([false, true, false]);
    controller.verify();
  });

  it('should call DELETE api', () =>{
    const uri = `${baseUri}/0`;

    service.deleteEmail(0).subscribe();

    const req = controller.expectOne(uri);
    expect(req.request.method).toBe('DELETE');

    req.flush([false, true, false]);
    controller.verify();
  });

  it('should return email form', () => {
    const form = service.generateEmailForm();
    expect(form).toBeTruthy();
  });

  it('should return columnDefs', () => {
    const columnDefs = service.getColumnDefs();
    expect(columnDefs).toBeTruthy();
  });
});
