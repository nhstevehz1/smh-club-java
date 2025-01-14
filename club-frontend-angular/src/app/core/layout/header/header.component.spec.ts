import {TestBed} from '@angular/core/testing';

import {HeaderComponent} from './header.component';
import {ActivatedRoute} from "@angular/router";
import {MockBuilder} from "ng-mocks";
import {ContentComponent} from "../content/content.component";

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
