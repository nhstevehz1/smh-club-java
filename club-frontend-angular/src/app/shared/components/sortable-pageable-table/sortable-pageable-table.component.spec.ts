import {SortablePageableTableComponent} from './sortable-pageable-table.component';
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
