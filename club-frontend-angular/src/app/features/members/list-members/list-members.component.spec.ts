import {ListMembersComponent} from './list-members.component';
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {MockBuilder, MockRender} from "ng-mocks";
import {
    SortablePageableTableComponent
} from "../../../shared/components/sortable-pageable-table/sortable-pageable-table.component";

describe('ListMembersComponent', () => {

  beforeEach(() => {
    return MockBuilder(ListMembersComponent)
        .keep(SortablePageableTableComponent)
        .keep(BrowserAnimationsModule);
  });

  it('should create', () => {
  const fixture = MockRender(ListMembersComponent);
    const component = fixture.componentInstance;
    expect(component).toBeTruthy();
  });
});
