import {ComponentFixture, TestBed} from '@angular/core/testing';
import {TranslateModule} from '@ngx-translate/core';

import {AuthService, LoginComponent} from '@app/core/auth';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;

  let authSvcMock: jasmine.SpyObj<AuthService>;

  beforeEach(async () => {
    authSvcMock = jasmine.createSpyObj('AuthService', ['login']);

    await TestBed.configureTestingModule({
      imports: [
          LoginComponent,
          TranslateModule.forRoot({})
      ],
      providers: [ {provide: AuthService, useValue: {}}]
    }).overrideComponent(LoginComponent,
        {set: {providers: [{provide: AuthService, useValue: authSvcMock}]}
    }).compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('onLogin should call AuthService.login', () => {
    const spy = authSvcMock.login.and.stub();

    component.onLogin();

    expect(spy).toHaveBeenCalledWith('p/home');
  });

  it('should contain app-ok-cancel component',  () => {
    const native = fixture.debugElement.nativeElement;
    const okCancel = native.querySelector('app-ok-cancel');

    expect(okCancel).toBeTruthy();
  });
});
