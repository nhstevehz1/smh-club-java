import {TestBed} from '@angular/core/testing';
import {EmailService} from "./email.service";
import {HttpClient} from "@angular/common/http";
import {MockBuilder} from "ng-mocks";

describe('EmailServiceService', () => {

  beforeEach(() => {
    return MockBuilder(EmailService)
        .mock(HttpClient)
  });

  it('should be created', () => {
    const service = TestBed.inject(EmailService);
    expect(service).toBeTruthy();
  });
});
