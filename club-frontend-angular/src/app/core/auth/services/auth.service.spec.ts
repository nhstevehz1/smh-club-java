import {TestBed} from '@angular/core/testing';

import {AuthService} from './auth.service';
import {MockBuilder} from "ng-mocks";
import {HttpClient} from "@angular/common/http";

describe('AuthService', () => {

  beforeEach(() => {
    return MockBuilder(AuthService)
        .mock(HttpClient);
  });

  it('should be created', () => {
    const service = TestBed.inject(AuthService);
    expect(service).toBeTruthy();
  });
});
