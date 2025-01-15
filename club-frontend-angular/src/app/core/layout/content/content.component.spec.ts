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


  it(`should contain a 'mat-sidenav-container`, () => {
    const fixture = MockRender(ContentComponent);
    const navContainer = fixture.nativeElement.querySelector('mat-sidenav-container');
    expect(navContainer).toBeTruthy();
  });

  it('should contain mat-sidenav component', () => {
    const fixture = MockRender(ContentComponent);
    const nav = fixture.nativeElement.querySelector('mat-sidenav');
    expect(nav).toBeTruthy();
  });

  it('should contain mat-sidenav component', () => {
    const fixture = MockRender(ContentComponent);
    const nav = fixture.nativeElement.querySelector('mat-sidenav');
    expect(nav).toBeTruthy();
  });

  it(`'isUser' should return true`, () => {
    const fixture = MockRender(ContentComponent);
    const isUser = fixture.componentInstance.isUser();
    expect(isUser).toBeTrue();
  });

  it(`'isManager' should return true`, () => {
    const fixture = MockRender(ContentComponent);
    const isManager = fixture.componentInstance.isManager();
    expect(isManager).toBeTrue();
  });

  it(`'isAdmin' should return false`, () => {
    const fixture = MockRender(ContentComponent);
    var isAdmin = fixture.componentInstance.isAdmin();
    expect(isAdmin).toBeFalse();
  });

  /*


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
