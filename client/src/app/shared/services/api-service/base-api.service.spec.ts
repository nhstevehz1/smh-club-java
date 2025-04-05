import { TestBed } from '@angular/core/testing';
import {provideHttpClient} from '@angular/common/http';
import {HttpTestingController, provideHttpClientTesting} from '@angular/common/http/testing';

import {MockApiService} from '@app/shared/services/api-service/testing/mock-api-service';
import {PageRequest} from '@app/shared/services/api-service/models';
import {generateApiCreateModel, generateApiModel} from '@app/shared/services/api-service/testing/mock-api-data';

describe('BaseApiService', () => {
  let service: MockApiService;
  let controller: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        MockApiService,
        provideHttpClient(),
        provideHttpClientTesting()
      ]
    });
    service = TestBed.inject(MockApiService);
    controller = TestBed.inject(HttpTestingController)
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should call GET api with no parameters when page request is empty', () => {
    const pageRequest: PageRequest = PageRequest.of(undefined, undefined);

    service.getPagedData(pageRequest).subscribe();

    const req = controller.expectOne(service.baseUri);
    expect(req.request.method).toBe('GET');

    req.flush([false, true, false]);
    controller.verify();
  });

  it('should call GET api with parameters when page request is populated', () => {
    const pageRequest: PageRequest = PageRequest.of(0, 0);
    const uri = service.baseUri + pageRequest.createQuery();

    service.getPagedData(pageRequest).subscribe();

    const req = controller.expectOne(uri);
    expect(req.request.method).toBe('GET');

    req.flush([false, true, false]);
    controller.verify();
  });

  it('should call POST api', () =>{
    const mockCreate = generateApiCreateModel();

    service.create(mockCreate).subscribe();

    const req = controller.expectOne(service.baseUri);
    expect(req.request.method).toBe('POST');

    req.flush([false, true, false]);
    controller.verify();
  });

  it('should call PUT api', () =>{
    const model = generateApiModel();
    const uri = `${service.baseUri}/${model.id}`;

    service.update(model).subscribe();

    const req = controller.expectOne(uri);
    expect(req.request.method).toBe('PUT');

    req.flush([false, true, false]);
    controller.verify();
  });

  it('should call DELETE api', () =>{
    const uri = `${service.baseUri}/0`;

    service.delete(0).subscribe();

    const req = controller.expectOne(uri);
    expect(req.request.method).toBe('DELETE');

    req.flush([false, true, false]);
    controller.verify();
  });
});
