import {ContentComponent} from './content.component';
import {ComponentFixture, TestBed} from "@angular/core/testing";
import {CUSTOM_ELEMENTS_SCHEMA} from "@angular/core";
import {provideNoopAnimations} from "@angular/platform-browser/animations";
import {HarnessLoader} from "@angular/cdk/testing";
import {TestbedHarnessEnvironment} from "@angular/cdk/testing/testbed";

describe('ContentComponent', () => {
  let fixture: ComponentFixture<ContentComponent>;
  let component: ContentComponent;
  let loader: HarnessLoader;

  beforeEach(async () => {
   await TestBed.configureTestingModule({
     providers: [
         ContentComponent,
         provideNoopAnimations()
     ],
     schemas: [CUSTOM_ELEMENTS_SCHEMA]
   }).compileComponents();
   fixture = TestBed.createComponent(ContentComponent);
   component = fixture.componentInstance;
   loader = TestbedHarnessEnvironment.loader(fixture);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it(`should contain a 'mat-sidenav-container`, () => {
    const navContainer = fixture.nativeElement.querySelector('mat-sidenav-container');
    expect(navContainer).toBeTruthy();
  });

  it('should contain mat-sidenav component', () => {
    const nav = fixture.nativeElement.querySelector('mat-sidenav');
    expect(nav).toBeTruthy();
  });

  it('should contain mat-sidenav component', () => {
    const nav = fixture.nativeElement.querySelector('mat-sidenav');
    expect(nav).toBeTruthy();
  });

  it('should contain mat-nav-list', () => {
    const navList = fixture.nativeElement.querySelector('mat-nav-list');
    expect(navList).toBeTruthy();
  });

  it(`'isUser' should return true`, () => {
    const isUser = fixture.componentInstance.isUser();
    expect(isUser).toBeTrue();
  });

  it(`'isManager' should return true`, () => {
    const isManager = fixture.componentInstance.isManager();
    expect(isManager).toBeTrue();
  });

  it(`'isAdmin' should return false`, () => {
    const isAdmin = fixture.componentInstance.isAdmin();
    expect(isAdmin).toBeFalse();
  });

  /*it('should open and close side nav', async () => {
    const nav = await loader.getHarness(MatSidenavHarness);

    // side nav should be closed initially
    expect(await nav.isOpen()).toBeFalse();

    component.toggleSideNav();
    expect(await nav.isOpen()).toBeTrue();

    component.toggleSideNav();
    expect(await nav.isOpen()).toBeFalse();
  });*/

});
