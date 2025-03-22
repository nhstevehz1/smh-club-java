import {TestBed} from '@angular/core/testing';

import {MembersService} from './members.service';
import {provideHttpClient} from "@angular/common/http";
import {HttpTestingController, provideHttpClientTesting} from "@angular/common/http/testing";
import {PageRequest} from "../../../shared/models/page-request";
import {generateAddressCreateForm} from "../../addresses/test/address-test";
import {generateEmailCreateForm} from "../../emails/test/email-test";
import {generatePhoneCreateForm} from "../../phones/test/phone-test";
import {generateMemberUpdate} from "../test/member-test";

describe('MembersService', () => {
  let service: MembersService;
  let controller: HttpTestingController;
  const baseUri = '/api/v1/members';

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
          MembersService,
          provideHttpClient(),
          provideHttpClientTesting(),
      ]
    });
    service = TestBed.inject(MembersService);
    controller = TestBed.inject(HttpTestingController);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should call api with no parameters when page request is empty', () => {
    const pageRequest: PageRequest = PageRequest.of(undefined, undefined);

    service.getMembers(pageRequest).subscribe();

    const req = controller.expectOne(baseUri);
    expect(req.request.method).toBe('GET');

    req.flush([false, true, false]);
    controller.verify();
  });

  it('should call api with parameters when page request is populated', () => {
    const pageRequest: PageRequest = PageRequest.of(0, 0);
    const uri = baseUri + pageRequest.createQuery();

    service.getMembers(pageRequest).subscribe();

    const req = controller.expectOne(uri);
    expect(req.request.method).toBe('GET');

    req.flush([false, true, false]);
    controller.verify();
  });

  it('should return a member create form', () => {
    const form
        = service.generateCreateForm(
            generateAddressCreateForm(),
            generateEmailCreateForm(),
            generatePhoneCreateForm());

    expect(form).toBeTruthy();
  });

  it('should return a member update form', () => {
    const form = service.generateUpdateForm(generateMemberUpdate());
    expect(form).toBeTruthy();
  });

  it('should return correct member update form', () => {
    const model = generateMemberUpdate();
    const val = service.generateUpdateForm(model).value;
    expect(val).toEqual(model);
  });
});
