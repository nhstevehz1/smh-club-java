import {ListPhonesComponent} from './list-phones.component';
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {MockBuilder, MockRender} from "ng-mocks";
import {
  SortablePageableTableComponent
} from "../../../shared/components/sortable-pageable-table/sortable-pageable-table.component";

describe('ListPhonesComponent', () => {

  beforeEach(() => {
    return MockBuilder(ListPhonesComponent)
        .keep(SortablePageableTableComponent)
        .keep(BrowserAnimationsModule)
  });

  it('should create', () => {
    const fixture = MockRender(ListPhonesComponent);
    const component = fixture.componentInstance;
    expect(component).toBeTruthy();
  });
});
