import {TestBed} from '@angular/core/testing';
import {EmailService} from "./email.service";
import {provideHttpClientTesting} from "@angular/common/http/testing";
import {HttpClient, provideHttpClient} from "@angular/common/http";

describe('EmailServiceService', () => {
  let service: EmailService;
  let httpClient: HttpClient;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        EmailService,
        provideHttpClient(),
        provideHttpClientTesting()]
    });
    service = TestBed.inject(EmailService);
    httpClient = TestBed.inject(HttpClient);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
    expect(httpClient).toBeTruthy();
  });
});
