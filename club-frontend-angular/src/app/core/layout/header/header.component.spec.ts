import {ComponentFixture, TestBed} from '@angular/core/testing';

import {HeaderComponent} from './header.component';
import {provideAnimations} from "@angular/platform-browser/animations";
import {ActivatedRoute, provideRouter} from "@angular/router";
import {MockBuilder} from "ng-mocks";
import {ContentComponent} from "../content/content.component";
import {BrowserDynamicTestingModule} from "@angular/platform-browser-dynamic/testing";

describe('HeaderComponent', () => {
  beforeEach(() => {
    return MockBuilder(ContentComponent)
        .mock(ActivatedRoute);
  });

  it('should create', () => {
    const fixture = TestBed.createComponent(HeaderComponent);
    const component = fixture.componentInstance;
    expect(component).toBeTruthy();
  });
});
