import {ListAddressesComponent} from './list-addresses.component';
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {MockBuilder, MockRender} from "ng-mocks";
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
