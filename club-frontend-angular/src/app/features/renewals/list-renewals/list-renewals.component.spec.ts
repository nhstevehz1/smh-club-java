import {ListRenewalsComponent} from './list-renewals.component';
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
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
