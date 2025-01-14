import {TestBed} from '@angular/core/testing';

import {MainLayoutComponent} from './main-layout.component';
import {MockBuilder} from "ng-mocks";

describe('MainLayoutComponent', () => {

  beforeEach(() => {
    return MockBuilder(MainLayoutComponent);
  });

  it('should create', () => {
    const fixture = TestBed.createComponent(MainLayoutComponent);
    const component = fixture.componentInstance;
    expect(component).toBeTruthy();
  });
});
