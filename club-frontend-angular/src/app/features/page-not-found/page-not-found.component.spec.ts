import {PageNotFoundComponent} from './page-not-found.component';
import {MockBuilder, MockRender} from "ng-mocks";

describe('PageNotFoundComponent', () => {
  beforeEach(() => {
    return MockBuilder(PageNotFoundComponent)
  });

  it('should create', () => {
    const fixture = MockRender(PageNotFoundComponent);
    const component = fixture.componentInstance;
    expect(component).toBeTruthy();
  });
});
