import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ProfileComponent} from './profile.component';
import {AuthService} from "../../services/auth.service";
import {HarnessLoader} from "@angular/cdk/testing";
import {TestbedHarnessEnvironment} from "@angular/cdk/testing/testbed";
import {MatListHarness} from "@angular/material/list/testing";
import {AuthUser} from "../../models/auth-user";

describe('ProfileComponent', () => {
  let component: ProfileComponent;
  let fixture: ComponentFixture<ProfileComponent>;
  let authSvcMock: jasmine.SpyObj<AuthService>;
  let loader: HarnessLoader;
  let currUser: AuthUser = {
    preferredUserName: 'user',
    givenName: 'First',
    familyName: 'Last',
    fullName: 'First Last',
    email: 'user@user.com',
    roles: ['role1', 'role2']
  }


  beforeEach(async () => {
    authSvcMock = jasmine.createSpyObj('AuthService', ['currentUser']);

    await TestBed.configureTestingModule({
      imports: [ProfileComponent],
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
    const spy = authSvcMock.currentUser.and.stub();
    fixture.detectChanges()
    await fixture.whenStable();

    expect(spy).toHaveBeenCalled();
  });

  it('should show profile data list', async () => {
    authSvcMock.currentUser.and.returnValue(currUser);
    const listHarness = await loader.getHarness(MatListHarness);

    expect(listHarness).toBeTruthy();
  });

  it('should show profile data list', async () => {
    authSvcMock.currentUser.and.returnValue(currUser);
    const listHarness = await loader.getHarness(MatListHarness);
    const items = await  listHarness.getItems();

    expect(items.length).toEqual(4);
  });

  it('list should contain Username', async () => {
    authSvcMock.currentUser.and.returnValue(currUser);
    const listHarness = await loader.getHarness(MatListHarness);
    const items = await  listHarness.getItems({title: 'Username'});

    expect(items.length).toEqual(1);
    const txt = await items[0].getSecondaryText();
    expect(txt).toBe(currUser.preferredUserName);
  });

  it('list should contain Full name', async () => {
    authSvcMock.currentUser.and.returnValue(currUser);
    const listHarness = await loader.getHarness(MatListHarness);
    const items = await  listHarness.getItems({title: 'Full name'});

    expect(items.length).toEqual(1);
    const txt = await items[0].getSecondaryText();
    expect(txt).toBe(currUser.fullName);
  });

  it('list should contain Email', async () => {
    authSvcMock.currentUser.and.returnValue(currUser);
    const listHarness = await loader.getHarness(MatListHarness);
    const items = await  listHarness.getItems({title: 'Email'});

    expect(items.length).toEqual(1);
    const txt = await items[0].getSecondaryText();
    expect(txt).toBe(currUser.email);
  });

  it('list should contain Roles', async () => {
    authSvcMock.currentUser.and.returnValue(currUser);
    const listHarness = await loader.getHarness(MatListHarness);
    const items = await  listHarness.getItems({title: 'Roles'});

    expect(items.length).toEqual(1);
    const txt = await items[0].getSecondaryText();
    expect(txt).toBe(currUser.roles.toString());
  });

  it('list should contain default message when user undefined', async () => {
    authSvcMock.currentUser.and.returnValue(undefined);
    const listHarness = await loader.getHarness(MatListHarness);
    const items = await  listHarness.getItems();

    expect(items.length).toEqual(1);
    const txt = await items[0].getFullText();
    expect(txt).toBe('Cannot read profile data!');
  });
});
