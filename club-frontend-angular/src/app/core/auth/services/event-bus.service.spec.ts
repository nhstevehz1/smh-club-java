import {TestBed} from '@angular/core/testing';

import {EventBusService} from './event-bus.service';
import {MockBuilder} from "ng-mocks";
import {AuthService} from "./auth.service";

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
