import {ComponentFixture, TestBed} from '@angular/core/testing';

import {MainLayoutComponent} from './main-layout.component';
import {provideHttpClient} from "@angular/common/http";
import {provideHttpClientTesting} from "@angular/common/http/testing";
import {provideRouter} from "@angular/router";
import {provideAnimations} from "@angular/platform-browser/animations";
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
