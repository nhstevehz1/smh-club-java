import {TestBed} from '@angular/core/testing';

import {MembersService} from './members.service';
import {HttpClient} from "@angular/common/http";
import {MockBuilder} from "ng-mocks";

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
