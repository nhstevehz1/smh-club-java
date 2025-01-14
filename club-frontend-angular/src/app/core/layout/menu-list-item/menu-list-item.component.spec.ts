import {MenuListItemComponent} from './menu-list-item.component';
import {MockBuilder, MockRender} from "ng-mocks";

describe('MenuListItemComponent', () => {

  beforeEach(async () => {
    return MockBuilder(MenuListItemComponent);
  });

  it('should create', () => {
    const fixture = MockRender(MenuListItemComponent);
    const component = fixture.componentInstance;
    expect(component).toBeTruthy();
  });
});
