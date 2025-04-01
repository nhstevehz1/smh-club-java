import {TestBed} from '@angular/core/testing';
import {provideHttpClient} from '@angular/common/http';
import {provideHttpClientTesting} from '@angular/common/http/testing';

import {MemberService} from '@app/features/members/services';

describe('MembersService', () => {
  let service: MemberService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
          MemberService,
          provideHttpClient(),
          provideHttpClientTesting(),
      ]
    });
    service = TestBed.inject(MemberService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should set baseUri to /api/v1/members', () => {
    const baseUri = '/api/v1/members';
    expect(service.baseUri).toBe(baseUri);
  })
});
