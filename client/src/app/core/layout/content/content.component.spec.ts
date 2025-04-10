import {ComponentFixture, TestBed} from '@angular/core/testing';
import {CUSTOM_ELEMENTS_SCHEMA} from '@angular/core';
import {provideNoopAnimations} from '@angular/platform-browser/animations';
import {HarnessLoader} from '@angular/cdk/testing';
import {TestbedHarnessEnvironment} from '@angular/cdk/testing/testbed';

import {provideRouter, Router} from '@angular/router';

import {MatSidenavModule} from '@angular/material/sidenav';
import {MatListModule} from '@angular/material/list';
import {MatIconModule} from '@angular/material/icon';
import {
  MatSidenavContainerHarness,
  MatSidenavContentHarness,
  MatSidenavHarness
} from '@angular/material/sidenav/testing';
import {MatNavListHarness} from '@angular/material/list/testing';
import {MatIconHarness} from '@angular/material/icon/testing';

import {TranslateModule} from '@ngx-translate/core';

import {NavItem} from '@app/core/layout/content/models/nav-item';
import {AuthService} from '@app/core/auth/services/auth.service';
import {PermissionType} from '@app/core/auth/models/permission-type';
import {ContentComponent} from './content.component';

describe('ContentComponent', () => {
  let fixture: ComponentFixture<ContentComponent>;
  let component: ContentComponent;
  let loader: HarnessLoader;
  let authServiceMock: jasmine.SpyObj<AuthService>
  const navListMock: NavItem[] = [
    {
      iconName: 'testIconRead',
      displayName: 'testRead',
      route: 'p/read',
      permission: PermissionType.read
    },
    {
      iconName: 'testIconWrite',
      displayName: 'testWrite',
      route: 'p/write',
      permission: PermissionType.write
    }
  ]

  beforeEach(async () => {
    authServiceMock = jasmine.createSpyObj('AuthServiceMock', ['hasPermission']);
    await TestBed.configureTestingModule({
     imports: [
       ContentComponent,
       MatSidenavModule,
       MatListModule,
       MatIconModule,
       TranslateModule.forRoot({})
     ],
     providers: [
       provideRouter([]),
       provideNoopAnimations(),
       {provide: AuthService, useValue: {}},
     ],
     schemas: [CUSTOM_ELEMENTS_SCHEMA]
    }).overrideComponent(ContentComponent,
        {set: {providers: [{provide: AuthService, useValue: authServiceMock}]}
    }).compileComponents();

   fixture = TestBed.createComponent(ContentComponent);
   component = fixture.componentInstance;
   loader = TestbedHarnessEnvironment.loader(fixture);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call with authService.hasPermission with correct permission type', () => {
    const spy = authServiceMock.hasPermission.and.callThrough();

    component.hasPermission(PermissionType.read);

    expect(spy).toHaveBeenCalledWith(PermissionType.read);
  });

  it('hasPermissions should return true when authService.hasPermissions returns true', () => {
    const spy = authServiceMock.hasPermission.and.returnValue(true);

    const result = component.hasPermission(PermissionType.read);

    expect(result).toBe(true);
    expect(spy).toHaveBeenCalled();
  });


  it('hasPermissions should return false when authService.hasPermissions returns false', () => {
    const spy = authServiceMock.hasPermission.and.returnValue(false);

    const result = component.hasPermission(PermissionType.read);

    expect(result).toBe(false);
    expect(spy).toHaveBeenCalled();
  });

  it('onMenuClicked should call router.Navigate', async () => {
    const router = TestBed.inject(Router);
    const spy =
        spyOn(router, 'navigate').and.callFake(() => Promise.resolve(true));

    component.onMenuClicked('test');

    expect(spy).toHaveBeenCalledWith(['test']);
  });

  it(`should contain a 'mat-sidenav-container`, async () => {
    const sideNavContainerHarness = await loader.getHarness(MatSidenavContainerHarness);
    expect(sideNavContainerHarness).toBeTruthy();
  });

  it('should contain mat-sidenav-content component', async () => {
    const sideNavContentHarness = await loader.getHarness(MatSidenavContentHarness);
    expect(sideNavContentHarness).toBeTruthy();
  });

  it('should contain mat-sidenav component', async () => {
    const sideNavHarness = await loader.getHarness(MatSidenavHarness);
    expect(sideNavHarness).toBeTruthy();
  });

  it('should contain mat-nav-list', async () => {
    const navListHarness = await loader.getHarness(MatNavListHarness);
    expect(navListHarness).toBeTruthy();
  });

  it('should contain router-outlet',  () => {
    const routerOutlet = fixture.nativeElement.querySelector('router-outlet');
    expect(routerOutlet).toBeTruthy();
  });

  it('should render all menu items', async () => {
    authServiceMock.hasPermission.and.returnValue(true);
    component.navList = navListMock;

    const menuHarness = await loader.getHarness(MatNavListHarness);
    const items = await menuHarness.getItems();

    expect(items.length).toEqual(navListMock.length);
  });

  it('should render only menu items with read permission', async () =>{
    authServiceMock.hasPermission.and
        .callFake((permission) => permission === PermissionType.read)
    component.navList = navListMock;

    const menuHarness = await loader.getHarness(MatNavListHarness);
    const items = await menuHarness.getItems();

    expect(items.length).toEqual(1);
  });

  it('should render only menu items with write permission', async () =>{
    authServiceMock.hasPermission.and
        .callFake((permission) => permission === PermissionType.write)
    component.navList = navListMock;

    const menuHarness = await loader.getHarness(MatNavListHarness);
    const items = await menuHarness.getItems();

    expect(items.length).toEqual(1);
  });

  it('mat-nav-list-item should render correct icon', async () =>{
    authServiceMock.hasPermission.and.returnValue(true)
    component.navList = navListMock;

    const menuHarness = await loader.getHarness(MatNavListHarness);
    const items = await menuHarness.getItems();
    const iconHarness = await items[0].getHarness(MatIconHarness);

    expect(iconHarness).toBeTruthy();
  });

  it('mat-nav-list-item should render correct display name', async () => {
    authServiceMock.hasPermission.and.returnValue(true)
    component.navList = navListMock;

    const menuHarness = await loader.getHarness(MatNavListHarness);
    const items = await menuHarness.getItems();

    const title = await items[0].getTitle();

    expect(title).toBe(navListMock[0].displayName);
  });

  it('mat-nav-list-item should call onMenuClicked', async () => {
    authServiceMock.hasPermission.and.returnValue(true)
    const spy = spyOn(component, 'onMenuClicked');
    component.navList = navListMock;

    const menuHarness = await loader.getHarness(MatNavListHarness);
    const items = await menuHarness.getItems();

    await items[0].click();

    expect(spy).toHaveBeenCalledWith(navListMock[0].route);
  });

  it(`side-nav mode should be 'over`, async ()=> {
    const sideNav = await loader.getHarness(MatSidenavHarness);
    const mode = await sideNav.getMode();

    expect(mode).toBe('over');
  });

  it(`side-nav state should be initially 'closed`, async ()=> {
    const sideNav = await loader.getHarness(MatSidenavHarness);
    const open = await sideNav.isOpen();

    expect(open).toBeFalse();
  });
});
