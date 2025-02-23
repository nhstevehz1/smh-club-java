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
    authSvcMock = jasmine.createSpyObj('AuthService', ['getCurrentUser']);

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

  fit('should create', () => {
    expect(component).toBeTruthy();
  });

  fit('should call authSvc.currentUser', async () => {
    const spy = authSvcMock.getCurrentUser.and.stub();
    fixture.detectChanges()
    await fixture.whenStable();

    expect(spy).toHaveBeenCalled();
  });

  fit('should show profile data list', async () => {
    authSvcMock.getCurrentUser.and.returnValue(currUser);
    const listHarness = await loader.getHarness(MatListHarness);

    expect(listHarness).toBeTruthy();
  });

  fit('should show profile data list', async () => {
    authSvcMock.getCurrentUser.and.returnValue(currUser);
    const listHarness = await loader.getHarness(MatListHarness);
    const items = await  listHarness.getItems();

    expect(items.length).toEqual(4);
  });

  fit('list should contain Username', async () => {
    authSvcMock.getCurrentUser.and.returnValue(currUser);
    const listHarness = await loader.getHarness(MatListHarness);
    const items = await  listHarness.getItems({title: 'Username'});

    expect(items.length).toEqual(1);
    const txt = await items[0].getSecondaryText();
    expect(txt).toBe(currUser.preferredUserName);
  });

  fit('list should contain Full name', async () => {
    authSvcMock.getCurrentUser.and.returnValue(currUser);
    const listHarness = await loader.getHarness(MatListHarness);
    const items = await  listHarness.getItems({title: 'Full name'});

    expect(items.length).toEqual(1);
    const txt = await items[0].getSecondaryText();
    expect(txt).toBe(currUser.fullName);
  });

  fit('list should contain Email', async () => {
    authSvcMock.getCurrentUser.and.returnValue(currUser);
    const listHarness = await loader.getHarness(MatListHarness);
    const items = await  listHarness.getItems({title: 'Email'});

    expect(items.length).toEqual(1);
    const txt = await items[0].getSecondaryText();
    expect(txt).toBe(currUser.email);
  });

  fit('list should contain Roles', async () => {
    authSvcMock.getCurrentUser.and.returnValue(currUser);
    const listHarness = await loader.getHarness(MatListHarness);
    const items = await  listHarness.getItems({title: 'Roles'});

    expect(items.length).toEqual(1);
    const txt = await items[0].getSecondaryText();
    expect(txt).toBe(currUser.roles.toString());
  });

  fit('list should contain default message when user undefined', async () => {
    authSvcMock.getCurrentUser.and.returnValue(undefined);
    const listHarness = await loader.getHarness(MatListHarness);
    const items = await  listHarness.getItems();

    expect(items.length).toEqual(1);
    const txt = await items[0].getFullText();
    expect(txt).toBe('Cannot read profile data!');
  });
});
