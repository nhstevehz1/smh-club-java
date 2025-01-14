import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListRenewalsComponent } from './list-renewals.component';
import {HttpClient, provideHttpClient} from "@angular/common/http";
import {provideHttpClientTesting} from "@angular/common/http/testing";
import {RenewalService} from "../services/renewal.service";
import {BrowserAnimationsModule, provideAnimations} from "@angular/platform-browser/animations";
import {MockBuilder, MockRender} from "ng-mocks";
import {
  SortablePageableTableComponent
} from "../../../shared/components/sortable-pageable-table/sortable-pageable-table.component";

describe('ListRenewalsComponent', () => {

  beforeEach(async () => {
    return MockBuilder(ListRenewalsComponent)
        .keep(SortablePageableTableComponent)
        .keep(BrowserAnimationsModule);
  });

  it('should create', () => {
    const fixture = MockRender(ListRenewalsComponent);
    const component = fixture.componentInstance;
    expect(component).toBeTruthy();
  });
});
