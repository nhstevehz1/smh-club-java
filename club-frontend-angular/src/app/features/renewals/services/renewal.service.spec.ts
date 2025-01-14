import { TestBed } from '@angular/core/testing';

import { RenewalService } from './renewal.service';
import {provideHttpClientTesting} from "@angular/common/http/testing";
import {HttpClient, provideHttpClient} from "@angular/common/http";
import {MockBuilder} from "ng-mocks";

describe('RenewalService', () => {

  beforeEach(() => {
    return MockBuilder(RenewalService)
        .mock(HttpClient);
  });

  it('should be created', () => {
    const service = TestBed.inject(RenewalService);
    expect(service).toBeTruthy();
  });
});
