import {ComponentFixture, TestBed} from '@angular/core/testing';

import {AuthService} from '../../services/auth.service';
import {HarnessLoader} from '@angular/cdk/testing';
import {TestbedHarnessEnvironment} from '@angular/cdk/testing/testbed';
import {MatListHarness} from '@angular/material/list/testing';

import {TranslateModule} from '@ngx-translate/core';

describe('ProfileComponent', () => {
  let component: ProfileComponent;
  let fixture: ComponentFixture<ProfileComponent>;
  let authSvcMock: jasmine.SpyObj<AuthService>;
  let loader: HarnessLoader;
  const currUser: AuthUser = {
    preferredUserName: 'user',
    givenName: 'First',
    familyName: 'Last',
    fullName: 'First Last',
    email: 'user@user.com',
    roles: ['role1', 'role2']
  }

  beforeEach(async () => {
    authSvcMock = jasmine.createSpyObj('AuthService', ['getCurrentUser']);

    await TestBed.configureTestingModule({
      imports: [
          ProfileComponent,
          TranslateModule.forRoot({})
      ],
      providers: [
        {provide: AuthService, userValue: {}}
      ]
    }).overrideComponent(ProfileComponent,
        { set: {providers: [{provide: AuthService, useValue: authSvcMock}]}}
    ).compileComponents();

    fixture = TestBed.createComponent(ProfileComponent);
    component = fixture.componentInstance;
    loader = TestbedHarnessEnvironment.loader(fixture);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call authSvc.currentUser', async () => {
    const spy = authSvcMock.getCurrentUser.and.stub();
    fixture.detectChanges()
    await fixture.whenStable();

    expect(spy).toHaveBeenCalled();
  });

  it('should show profile data list', async () => {
    authSvcMock.getCurrentUser.and.returnValue(currUser);
    const listHarness = await loader.getHarness(MatListHarness);

    expect(listHarness).toBeTruthy();
  });

  it('should show profile data list', async () => {
    authSvcMock.getCurrentUser.and.returnValue(currUser);
    const listHarness = await loader.getHarness(MatListHarness);
    const items = await  listHarness.getItems();

    expect(items.length).toEqual(4);
  });

  it('list should contain Username', async () => {
    authSvcMock.getCurrentUser.and.returnValue(currUser);
    const listHarness = await loader.getHarness(MatListHarness);
    const items = await  listHarness.getItems({title: 'auth.profile.list.username'});

    expect(items.length).toEqual(1);
    const txt = await items[0].getSecondaryText();
    expect(txt).toBe(currUser.preferredUserName);
  });

  it('list should contain Full name', async () => {
    authSvcMock.getCurrentUser.and.returnValue(currUser);
    const listHarness = await loader.getHarness(MatListHarness);
    const items = await  listHarness.getItems({title: 'auth.profile.list.fullName'});

    expect(items.length).toEqual(1);
    const txt = await items[0].getSecondaryText();
    expect(txt).toBe(currUser.fullName);
  });

  it('list should contain Email', async () => {
    authSvcMock.getCurrentUser.and.returnValue(currUser);
    const listHarness = await loader.getHarness(MatListHarness);
    const items = await  listHarness.getItems({title: 'auth.profile.list.email'});

    expect(items.length).toEqual(1);
    const txt = await items[0].getSecondaryText();
    expect(txt).toBe(currUser.email);
  });

  it('list should contain Roles', async () => {
    authSvcMock.getCurrentUser.and.returnValue(currUser);
    const listHarness = await loader.getHarness(MatListHarness);
    const items = await  listHarness.getItems({title: 'auth.profile.list.roles'});

    expect(items.length).toEqual(1);
    const txt = await items[0].getSecondaryText();
    expect(txt).toBe(currUser.roles.toString());
  });

  it('list should contain default message when user undefined', async () => {
    authSvcMock.getCurrentUser.and.returnValue(undefined);
    const listHarness = await loader.getHarness(MatListHarness);
    const items = await  listHarness.getItems();

    expect(items.length).toEqual(1);
    const txt = await items[0].getFullText();
    expect(txt).toBe('auth.profile.list.defaultMessage');
  });
});
