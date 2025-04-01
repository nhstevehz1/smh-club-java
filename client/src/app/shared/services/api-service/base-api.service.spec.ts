import { TestBed } from '@angular/core/testing';
import {provideHttpClient} from '@angular/common/http';
import {HttpTestingController, provideHttpClientTesting} from '@angular/common/http/testing';

import {MockCrudService} from '@app/shared/services/api-service/testing/mock-crud-service';
import {PageRequest} from '@app/shared/services/api-service/models';
import {generateMockCreate} from '@app/shared/services/api-service/testing/mock-api-data';
import {generateMockModel} from '@app/shared/services/dialog-edit-service/testing';

describe('BaseApiService', () => {
  let service: MockCrudService;
  let controller: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        MockCrudService,
        provideHttpClient(),
        provideHttpClientTesting()
      ]
    });
    service = TestBed.inject(MockCrudService);
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
    const mockCreate = generateMockCreate();

    service.create(mockCreate).subscribe();

    const req = controller.expectOne(service.baseUri);
    expect(req.request.method).toBe('POST');

    req.flush([false, true, false]);
    controller.verify();
  });

  it('should call PUT api', () =>{
    const mockModel = generateMockModel();
    const uri = `${service.baseUri}/${mockModel.id}`;

    service.update(mockModel.id, mockModel).subscribe();

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
