import {TestBed} from '@angular/core/testing';

import {MembersService} from './members.service';
import {provideHttpClientTesting} from "@angular/common/http/testing";
import {HttpClient, provideHttpClient} from "@angular/common/http";
import {MockBuilder, MockRender} from "ng-mocks";

describe('MembersService', () => {

  beforeEach(() => {
    return MockBuilder(MembersService)
        .mock(HttpClient);
  });

  it('should be created', () => {
    const service = TestBed.inject(MembersService);
    expect(service).toBeTruthy();
  });
});
