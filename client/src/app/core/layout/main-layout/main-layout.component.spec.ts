import {ComponentFixture, fakeAsync, TestBed, tick} from '@angular/core/testing';
import {provideRouter, Router} from '@angular/router';
import {provideNoopAnimations} from '@angular/platform-browser/animations';
import {provideHttpClient} from '@angular/common/http';
import {provideHttpClientTesting} from '@angular/common/http/testing';

import {MatDividerModule} from '@angular/material/divider';
import {TranslateModule} from '@ngx-translate/core';

import {AuthService} from '@app/core/auth/services/auth.service';
import {HeaderComponent} from '@app/core/layout/header/header.component';
import {FooterComponent} from '@app/core/layout/footer/footer.component';
import {ContentComponent} from '@app/core/layout/content/content.component';
import {MainLayoutComponent} from './main-layout.component';

describe('MainLayoutComponent', () => {
  let fixture: ComponentFixture<MainLayoutComponent>;
  let component: MainLayoutComponent;
  let authServiceMock: jasmine.SpyObj<AuthService>;

  beforeEach(async () => {
    authServiceMock = jasmine.createSpyObj<AuthService>('AuthService',
        ['logOut', 'isLoggedIn']);

    await TestBed.configureTestingModule({
      imports: [
        MainLayoutComponent,
        HeaderComponent,
        FooterComponent,
        ContentComponent,
        MatDividerModule,
        TranslateModule.forRoot({})
      ],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        provideNoopAnimations(),
        provideRouter([]),
        {provide: AuthService, useValue: {}},
      ]
    }).overrideComponent(MainLayoutComponent,
        {set: {providers: [{provide: AuthService, useValue: authServiceMock}]}
    }).compileComponents();

    fixture = TestBed.createComponent(MainLayoutComponent);
    component = fixture.componentInstance;
  });

  it('should create', async () => {
    expect(component).toBeTruthy();
  });

  it('should call authService.logOut()', () => {
    const spy = authServiceMock.logOut.and.callThrough();

    component.logoutHandler();

    expect(spy).toHaveBeenCalled();
  });

  it('isLoggedIn() should return true', () => {
    const spy = authServiceMock.isLoggedIn.and.returnValue(true);

    const result = component.isLoggedIn;

    expect(spy).toHaveBeenCalled();
    expect(result).toBe(true);
  });

  it('isLoggedIn() should return false', () => {
    const spy = authServiceMock.isLoggedIn.and.returnValue(false);

    const result = component.isLoggedIn;

    expect(spy).toHaveBeenCalled();
    expect(result).toBe(false);
  });

  it('should call content.toggleSlideNav', () => {
    const spy = spyOn(component.content, 'toggleSideNav');
    component.sideNavHandler();

    expect(spy).toHaveBeenCalled();
  });

  it('should call router.navigate([p/profile]', fakeAsync( () => {
    const router = TestBed.inject(Router);
    const spy = spyOn(router, 'navigate')
    spy.and.returnValue(Promise.resolve(true));

    component.profileHandler();
    tick();
    expect(spy).toHaveBeenCalledWith(['p/profile']);
  }));
});
