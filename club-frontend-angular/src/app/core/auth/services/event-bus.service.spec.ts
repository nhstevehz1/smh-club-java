import {TestBed} from '@angular/core/testing';

import {EventBusService} from './event-bus.service';
import {MockBuilder} from "ng-mocks";

describe('EventBusService', () => {
  let service: EventBusService;

  beforeEach(() => {
    return MockBuilder(EventBusService);
  });

  it('should be created', () => {
    service = TestBed.inject(EventBusService);
    expect(service).toBeTruthy();
  });
});
