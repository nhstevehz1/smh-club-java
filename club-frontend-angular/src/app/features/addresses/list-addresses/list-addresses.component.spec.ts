import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ListAddressesComponent} from './list-addresses.component';
import {provideHttpClient} from "@angular/common/http";
import {provideHttpClientTesting} from "@angular/common/http/testing";
import {AddressService} from "../services/address.service";
import {CommonModule} from "@angular/common";
import {BrowserAnimationsModule, provideAnimations} from "@angular/platform-browser/animations";
import {MockBuilder, MockRender} from "ng-mocks";
import {MatSort} from "@angular/material/sort";
import {
  SortablePageableTableComponent
} from "../../../shared/components/sortable-pageable-table/sortable-pageable-table.component";

describe('ListAddressesComponent', () => {

  beforeEach(() => {
    return MockBuilder(ListAddressesComponent)
        .keep(SortablePageableTableComponent)
        .keep(BrowserAnimationsModule)
  });

  it('should create', () => {
    const fixture = MockRender(ListAddressesComponent);
    const component = fixture.componentInstance
    expect(component).toBeTruthy();
  });
});
