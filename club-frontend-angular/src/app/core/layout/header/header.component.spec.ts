import {ComponentFixture, TestBed} from '@angular/core/testing';

import {HeaderComponent} from './header.component';
import {provideRouter, RouterLink} from "@angular/router";
import {MatToolbarModule} from "@angular/material/toolbar";
import {MatMenuModule} from "@angular/material/menu";
import {MatIconModule} from "@angular/material/icon";
import {MatDividerModule} from "@angular/material/divider";
import {MatTooltipModule} from "@angular/material/tooltip";
import {MatSlideToggleChange, MatSlideToggleModule} from "@angular/material/slide-toggle";
import {provideNoopAnimations} from "@angular/platform-browser/animations";
import {HarnessLoader} from "@angular/cdk/testing";
import {DOCUMENT} from "@angular/common";
import {TestbedHarnessEnvironment} from "@angular/cdk/testing/testbed";
import {MatMenuHarness} from "@angular/material/menu/testing";
import {MatButtonHarness} from "@angular/material/button/testing";
import {MatIconHarness} from "@angular/material/icon/testing";
import {MatSlideToggleHarness} from "@angular/material/slide-toggle/testing";
import {By} from "@angular/platform-browser";
import {TranslateModule} from "@ngx-translate/core";

describe('HeaderComponent', () => {
  let fixture: ComponentFixture<HeaderComponent>;
  let component: HeaderComponent;

  beforeEach(async() => {
    await TestBed.configureTestingModule({
      imports: [
        HeaderComponent,
        MatToolbarModule,
        MatMenuModule,
        MatIconModule,
        MatDividerModule,
        RouterLink,
        MatTooltipModule,
        MatSlideToggleModule,
        TranslateModule.forRoot({})
      ],
      providers: [
        provideNoopAnimations(),
        provideRouter([])
      ]
    }).compileComponents();
    fixture = TestBed.createComponent(HeaderComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  describe('test event emitters', () => {
    it('should call profileClick.emit()', () => {
      const spy = spyOn(component.profileClickSignal, 'emit');
      component.profileHandler();
      expect(spy).toHaveBeenCalled();
    });

    it('should call logoutClick.emit()', () => {
      const spy = spyOn(component.logoutClickSignal, 'emit');
      component.logoutHandler();
      expect(spy).toHaveBeenCalled();
    });

    it('should call toggleSideNave.emit()', () => {
      const spy = spyOn(component.toggleSidenavSignal, 'emit');
      component.toggleSideNavHandler();
      expect(spy).toHaveBeenCalled();
    });
    
    it('should call document.body.classList.toggle(dark-mode, true)', () => {
      const classList = TestBed.inject(DOCUMENT).body.classList;
      const spy = spyOn(classList, 'toggle');
      const event = {checked: true};

      component.onThemeChanged(event as MatSlideToggleChange)

      component.toggleSideNavHandler();
      expect(spy).toHaveBeenCalledWith('dark-mode', true);
    });

    it('should call document.body.classList.toggle(dark-mode, false)', () => {
      const classList = TestBed.inject(DOCUMENT).body.classList;
      const spy = spyOn(classList, 'toggle');
      const event = {checked: false};

      component.onThemeChanged(event as MatSlideToggleChange)

      component.toggleSideNavHandler();
      expect(spy).toHaveBeenCalledWith('dark-mode', false);
    });
  });

  describe('test component interactions', () => {
    let loader: HarnessLoader;

    beforeEach(() => {
      loader = TestbedHarnessEnvironment.loader(fixture);
      fixture.componentRef.setInput('isLoggedIn', true);
    });

    it('should contain two mat-icon-buttons', async () => {
      const buttonHarnesses = await loader.getAllHarnesses(MatButtonHarness.with({variant: 'icon'}));

      expect(buttonHarnesses.length).toBe(2);
    });

    it('menu button should be first mat-icon-button', async () => {
      const buttonHarnesses = await loader.getAllHarnesses(MatButtonHarness.with({variant: 'icon'}));
      const buttonHarness = buttonHarnesses[0];
      const iconHarness = await buttonHarness.getHarnessOrNull(MatIconHarness.with({name: 'menu'}));

      expect(iconHarness).toBeTruthy();
    });

    it('account button should be second mat-icon-button', async () => {
      const buttonHarnesses = await loader.getAllHarnesses(MatButtonHarness.with({variant: 'icon'}));
      const buttonHarness = buttonHarnesses[1];
      const iconHarness = await buttonHarness.getHarnessOrNull(MatIconHarness.with({name: 'account_circle'}));

      expect(iconHarness).toBeTruthy();
    });

    it('menu button click should call toggleSideNavHandler()', async () => {
      const spy = spyOn(component, 'toggleSideNavHandler');
      const buttonHarnesses = await loader.getAllHarnesses(MatButtonHarness.with({variant: 'icon'}));
      const buttonHarness = buttonHarnesses[0];

      await buttonHarness.click();

      expect(spy).toHaveBeenCalled();
    });

    it('account button click should open menu when loggedIn is true', async () => {
      const buttonHarnesses = await loader.getAllHarnesses(MatButtonHarness.with({variant: 'icon'}));
      const buttonHarness = buttonHarnesses[1];
      const menuHarness = await loader.getHarness(MatMenuHarness);

      await buttonHarness.click();

      expect(await menuHarness.isOpen()).toBeTrue();
    });

    it('account button click should not open menu when loggedIn is false', async () => {
      fixture.componentRef.setInput('isLoggedIn', false);
      const buttonHarnesses = await loader.getAllHarnesses(MatButtonHarness.with({variant: 'icon'}));
      const buttonHarness = buttonHarnesses[1];
      const menuHarness = await loader.getHarness(MatMenuHarness);

      await buttonHarness.click();

      expect(await menuHarness.isOpen()).toBeFalsy();
    });

    it('menu should contain two menu items', async () => {
      const menuHarness = await loader.getHarness(MatMenuHarness);
      await menuHarness.open();
      const harnesses = await menuHarness.getItems();

      expect(menuHarness).toBeTruthy();
      expect(harnesses.length).toBe(2);
    });

    it('menu should contain Profile menuitem ', async  () => {
      const menuHarness = await loader.getHarness(MatMenuHarness);
      await menuHarness.open();

      const profileHarness = await menuHarness.getItems({text: 'personlayout.header.profile'});

      expect(profileHarness.length).toEqual(1);
    });

    it('menu should contain LogOut menu item', async  () => {
      const menuHarness = await loader.getHarness(MatMenuHarness);
      await menuHarness.open();

      const logoutHarness = await menuHarness.getItems({text: 'exit_to_applayout.header.logout'});

      expect(logoutHarness.length).toEqual(1);
    });

    it('should call profileHandler()', async  () => {
      const spy = spyOn(component, 'profileHandler');
      const menuHarness = await loader.getHarness(MatMenuHarness);
      await menuHarness.open();

      await menuHarness.clickItem({text: 'personlayout.header.profile'});

      expect(spy).toHaveBeenCalled();
    });

    it('should call logoutHandler()', async  () => {
      const spy = spyOn(component, 'logoutHandler');
      const menuHarness = await loader.getHarness(MatMenuHarness);
      await menuHarness.open();

      await menuHarness.clickItem({text: 'exit_to_applayout.header.logout'});

      expect(spy).toHaveBeenCalled();
    });

    it('should call onThemeChanged', async  () => {
      const spy = spyOn(component, 'onThemeChanged');
      const slideToggleHarness = await loader.getHarness(MatSlideToggleHarness);

      await slideToggleHarness.check();

      expect(spy).toHaveBeenCalled();
    });

    it('user name should be visible when isLogged in is true and userName is truthy', async () => {
      fixture.componentRef.setInput('isLoggedIn', true);
      fixture.componentRef.setInput('userName', 'USER');

      fixture.detectChanges();

      const element = fixture.debugElement.query(By.css(('.user-name')));
      expect(element).toBeTruthy();
      expect(element.nativeElement.textContent).toContain('USER');
    });

    it('user name should not be visible when isLogged in is true and userName is not set', async () => {
      fixture.componentRef.setInput('isLoggedIn', true);

      fixture.detectChanges();

      const element = fixture.debugElement.query(By.css(('.user-name')));
      expect(element).toBeFalsy();
    });

    it('user name should be visible when isLogged in is false and userName is truthy', async () => {
      fixture.componentRef.setInput('isLoggedIn', false);
      fixture.componentRef.setInput('userName', 'USER');

      fixture.detectChanges();

      const element = fixture.debugElement.query(By.css(('.user-name')));
      expect(element).toBeFalsy();
    });
  })
});
