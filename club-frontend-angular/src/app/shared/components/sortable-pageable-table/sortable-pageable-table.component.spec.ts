import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SortablePageableTableComponent } from './sortable-pageable-table.component';
import {provideAnimations} from "@angular/platform-browser/animations";
import {MockBuilder, MockRender} from "ng-mocks";

describe('SortablePageableTableComponent', () => {

  beforeEach(() => {
    return MockBuilder(SortablePageableTableComponent);
  });

  it('should create', () => {
    const fixture = MockRender(SortablePageableTableComponent);
    const component = fixture.componentInstance;
    expect(component).toBeTruthy();
  });
});
