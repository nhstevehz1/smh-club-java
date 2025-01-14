import {ContentComponent} from './content.component';
import {MockBuilder, MockRender} from "ng-mocks";

describe('ContentComponent', () => {

  beforeEach(() => {
    return MockBuilder(ContentComponent)
  });

  it('should create', () => {
    const fixture = MockRender(ContentComponent);
    const component = fixture.componentInstance;
    expect(component).toBeTruthy();
  });

  /*it(`should contain a 'mat-sidenav-container`, () => {
    const nav = fixture.nativeElement.querySelector('mat-sidenav-container');
    expect(nav).toBeTruthy();
  });

  it(`'isUser' should return true`, () => {
    var isUser = component.isUser();
    expect(isUser).toBeTrue();
  });

  it(`'isManager' should return true`, () => {
    var isManager = component.isManager();
    expect(isManager).toBeTrue();
  });

  it(`'isAdmin' should return false`, () => {
    var isAdmin = component.isAdmin();
    expect(isAdmin).toBeFalse();
  });

  it('should open and close side nav', async () => {
    const nav = await loader.getHarness(MatSidenavHarness);

    // side nav should be closed initially
    expect(await nav.isOpen()).toBeFalse();

    component.toggleSideNav();
    expect(await nav.isOpen()).toBeTrue();

    component.toggleSideNav();
    expect(await nav.isOpen()).toBeFalse();
  });*/

});
